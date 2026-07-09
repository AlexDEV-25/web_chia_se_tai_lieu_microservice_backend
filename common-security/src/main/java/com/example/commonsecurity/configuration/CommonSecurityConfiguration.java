package com.example.commonsecurity.configuration;

import com.example.commonsecurity.helper.CustomAuthEntryPoint;
import com.example.commonsecurity.helper.CustomJwtDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonSecurityConfiguration {

    @Bean
    public CustomJwtDecoder customJwtDecoder() {
        return new CustomJwtDecoder();
    }

    @Bean
    public CustomAuthEntryPoint customAuthEntryPoint() {
        return new CustomAuthEntryPoint();
    }
}