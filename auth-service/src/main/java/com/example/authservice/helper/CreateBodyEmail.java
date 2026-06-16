package com.example.authservice.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CreateMail {

    @Value("${app.domain.frontend}")
    private String frontendDomain;

    public String bodyActivateAccount(String email, String activationCode) {

        String url = frontendDomain + "/activate/" + email + "/" + activationCode;

        return "<html><body>"
                + "<h2 style='color:#4A90E2'>Kích hoạt tài khoản</h2>"
                + "<p>Vui lòng sử dụng mã sau:</p>"
                + "<h1 style='color:green'>" + activationCode + "</h1>"
                + "<p>Bấm vào link để kích hoạt:</p>"
                + "<a href='" + url + "'>Nhấn vào đây để kích hoạt</a>"
                + "</body></html>";

    }

    public String bodyChangePassword(String email, String forgotPassWordCode) {
        String url = frontendDomain + "/change-password/" + email + "/" + forgotPassWordCode;

        return "<html><body>"
                + "<h2 style='color:#4A90E2'>Đặt lại mật khẩu</h2>"
                + "<p>Vui lòng sử dụng mã sau:</p>"
                + "<h1 style='color:green'>" + forgotPassWordCode + "</h1>"
                + "<p>Bấm vào link để kích hoạt:</p>"
                + "<a href='" + url + "'>Nhấn vào đây để đặt lại mật khẩu</a>"
                + "</body></html>";

    }

    public String bodyLockAccount(String email) {

        return "<html><body>"
                + "<h2 style='color:#D0021B'>Thông báo khóa tài khoản</h2>"
                + "<p>Tài khoản của bạn đã bị khóa vì lý do bảo mật.</p>"
                + "<p>Nếu bạn có thắc mắc, vui lòng liên hệ ngay với bộ phận hỗ trợ thông qua số 0987654321.</p>"
                + "</body></html>";


    }
}
