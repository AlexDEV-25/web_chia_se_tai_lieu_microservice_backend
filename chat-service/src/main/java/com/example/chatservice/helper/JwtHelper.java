package com.example.chatservice.helper;


import com.example.AppError;
import com.example.commondto.response.IntrospectResponse;
import com.example.commonexception.exception.AppException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class JwtHelper {

    @Value("${jwt.secretKey}")
    private String SIGNER_KEY;

    public IntrospectResponse introspect(String token) throws JOSEException, ParseException {
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        IntrospectResponse response = new IntrospectResponse();
        response.setValid(isValid);
        return response;
    }

    private void verifyToken(String token) throws JOSEException, ParseException, AppException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date epx = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!epx.after(new Date())) {
            throw AppException.builder().appError(AppError.TOKEN_EXPIRED).build();
        }
        if (!verified) {
            throw AppException.builder().appError(AppError.INVALID_TOKEN).build();
        }
    }

}