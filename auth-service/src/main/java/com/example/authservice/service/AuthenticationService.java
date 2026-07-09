package com.example.authservice.service;

import com.example.AppError;
import com.example.authservice.dto.request.*;
import com.example.authservice.dto.response.AuthenticationResponse;
import com.example.authservice.dto.response.ExchangeTokenResponse;
import com.example.authservice.dto.response.OutboundUserResponse;
import com.example.authservice.dto.response.UserResponse;
import com.example.authservice.helper.CreateBodyEmail;
import com.example.authservice.helper.JwtHelper;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.Role;
import com.example.authservice.model.User;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.repository.httpclient.OutboundIdentityClient;
import com.example.authservice.repository.httpclient.OutboundUserClient;
import com.example.authservice.repository.httpclient.ProfileClient;
import com.example.commondto.request.UserDetailRequest;
import com.example.commondto.response.IntrospectResponse;
import com.example.commonexception.exception.AppException;
import com.example.event.EmailNotificationEvent;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CreateBodyEmail createBodyEmail;
    private final JwtHelper jwtHelper;
    private final ProfileClient profileClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @Value("${outbound.identity.grant-types}")
    protected String GRANT_TYPES;

    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw AppException.builder().appError(AppError.USER_NOT_FOUND).build();
        }
        if (!user.isVerified()) {
            throw AppException.builder().appError(AppError.ACCOUNT_NOT_ACTIVATED).build();
        }

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw AppException.builder().appError(AppError.LOGIN_FAILED).build();
        }

        AuthenticationResponse response = new AuthenticationResponse();
        response.setAuthenticated(authenticated);
        String token = jwtHelper.generateToken(user);
        response.setToken(token);

        return response;
    }

    public AuthenticationResponse refreshToken(String oldToken) {
        try {
            SignedJWT signToken = jwtHelper.verifyToken(oldToken);

            String username = signToken.getJWTClaimsSet().getSubject();
            User user = userRepository.findByUsername(username);

            AuthenticationResponse response = new AuthenticationResponse();
            String token = jwtHelper.generateToken(user);
            response.setAuthenticated(true);
            response.setToken(token);

            return response;
        } catch (ParseException e) {
            throw AppException.builder().appError(AppError.FAILED_TO_PARSE_DATA).build();
        } catch (JOSEException e) {
            throw AppException.builder().appError(AppError.JOSE_PROCESSING_ERROR).build();
        }

    }

    public IntrospectResponse introspect(String token) {
        try {
            return jwtHelper.introspect(token);
        } catch (ParseException e) {
            throw AppException.builder().appError(AppError.FAILED_TO_PARSE_DATA).build();
        } catch (JOSEException e) {
            throw AppException.builder().appError(AppError.JOSE_PROCESSING_ERROR).build();
        }

    }

    public UserResponse register(RegisterRequest request) {
        User user = userMapper.requestToUser(request);

        String activationCode = this.generateActivationCode();

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw AppException.builder().appError(AppError.EMAIL_ALREADY_EXISTS).build();
        }
        if (userRepository.existsByUsername(request.getEmail())) {
            throw AppException.builder().appError(AppError.USERNAME_ALREADY_EXISTS).build();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(activationCode);

        User saved = userRepository.save(user);

        try {
            profileClient.createUserDetail(UserDetailRequest.builder().userId(saved.getId()).build());
        } catch (Exception e) {
            userRepository.delete(saved);
            throw AppException.builder().appError(AppError.CREATE_PROFILE_FAILED).build();
        }

        EmailNotificationEvent emailNotificationEvent = EmailNotificationEvent.builder()
                .channel("EMAIL")
                .recipient(request.getEmail())
                .subject("Kích hoạt tài khoản của bạn")
                .body(createBodyEmail.bodyActivateAccount(saved.getEmail(), saved.getActivationCode()))
                .build();

        kafkaTemplate.send("activate-account", emailNotificationEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Cannot send event", ex);
                        // sau này thích dùng @Schedule và Outbox pattern để gửi lại event
                    }
                });
        return userMapper.userToResponse(saved);
    }

    public void activateAccount(ActiveAccountRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw AppException.builder().appError(AppError.USER_NOT_FOUND).build();
        }
        if (user.isVerified()) {
            throw AppException.builder().appError(AppError.ACCOUNT_ALREADY_ACTIVATED).build();
        }
        if (user.getActivationCode().equals(request.getActivationCode())) {
            user.setVerified(true);
            user.setActivationCode(null);
            userRepository.save(user);
        } else {
            throw AppException.builder().appError(AppError.INVALID_VERIFICATION_CODE).build();
        }
    }

    public void forgotPassword(String email) {
        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email);
            String forgotPassWordCode = this.generateActivationCode();
            user.setForgotPasswordCode(forgotPassWordCode);
            userRepository.save(user);

            EmailNotificationEvent emailNotificationEvent = EmailNotificationEvent.builder()
                    .channel("EMAIL")
                    .recipient(user.getEmail())
                    .subject("Đổi mật khẩu tài khoản của bạn")
                    .body(createBodyEmail.bodyActivateAccount(user.getEmail(), user.getForgotPasswordCode()))
                    .build();

            kafkaTemplate.send("change-password", emailNotificationEvent)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Cannot send event", ex);
                            // sau này thích dùng @Schedule và Outbox pattern để gửi lại event
                        }
                    });
        } else {
            throw AppException.builder().appError(AppError.EMAIL_NOT_FOUND).build();
        }
    }

    public UserResponse changePassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw AppException.builder().appError(AppError.USER_NOT_FOUND).build();
        }

        if (user.getForgotPasswordCode().equals(request.getForgotPasswordCode())) {
            user.setForgotPasswordCode(null);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUpdatedAt(LocalDateTime.now());
            User saved = userRepository.save(user);
            return userMapper.userToResponse(saved);
        } else {
            throw AppException.builder().appError(AppError.INVALID_VERIFICATION_CODE).build();
        }
    }

    public AuthenticationResponse loginWithGoogle(String code) {
        ExchangeTokenRequest request = new ExchangeTokenRequest(code, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI,
                GRANT_TYPES);
        AuthenticationResponse response = new AuthenticationResponse();
        try {
            ExchangeTokenResponse exchangeTokenResponse = outboundIdentityClient.exchangeToken(request);

            String bearerToken = "Bearer " + exchangeTokenResponse.getAccessToken();
            OutboundUserResponse userInfo = outboundUserClient.getUserDetails(bearerToken);

            boolean exist = userRepository.existsByEmail(userInfo.getEmail());

            String token = "";
            if (!exist) {
                List<String> Stringroles = new ArrayList<String>();
                Stringroles.add("USER");
                List<Role> roles = roleRepository.findAllById(Stringroles);

                User newUser = User.builder().username(userInfo.getEmail()).email(userInfo.getEmail()).verified(true)
                        .hide(false).roles(roles).build();
                User saved = userRepository.save(newUser);
                try {
                    profileClient.createUserDetail(UserDetailRequest.builder().userId(saved.getId()).build());
                } catch (Exception e) {
                    userRepository.delete(saved);
                    throw AppException.builder().appError(AppError.CREATE_PROFILE_FAILED).build();
                }
                token = jwtHelper.generateToken(saved);
            } else {
                User user = userRepository.findByEmail(userInfo.getEmail());
                token = jwtHelper.generateToken(user);
            }

            response.setToken(token);
            response.setAuthenticated(true);
        } catch (Exception e) {
            throw AppException.builder().appError(AppError.GOOGLE_LOGIN_FAILED).build();
        }
        return response;
    }

    private String generateActivationCode() {
        return UUID.randomUUID().toString();
    }

}
