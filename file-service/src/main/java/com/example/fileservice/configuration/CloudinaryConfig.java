package com.example.fileservice.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CloudinaryConfig {

    @Value("${app.cloud.name}")
    private String cloudName;
    @Value("${app.cloud.key}")
    private String cloudKey;
    @Value("${app.cloud.secret}")
    private String cloudSecret;

    @Bean
    RestTemplate restTemplate() {
        // dùng để gọi api từ spring
        // đang dùng trong fileManager
        return new RestTemplate();
    }

    @Bean
    Cloudinary cloudinary() {

        return new Cloudinary(//
                ObjectUtils.asMap(//
                        "cloud_name", cloudName,
                        "api_key", cloudKey,
                        "api_secret", cloudSecret,
                        "secure", true));
    }

}
