package com.example.commonsecurity.helper;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// lấy header Authorization từ request hiện tại và thêm nó vào header của request Feign để truyền qua các service khác
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes == null) return;

        String authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

        if (StringUtils.hasText(authHeader))
            template.header("Authorization", authHeader);
    }
}