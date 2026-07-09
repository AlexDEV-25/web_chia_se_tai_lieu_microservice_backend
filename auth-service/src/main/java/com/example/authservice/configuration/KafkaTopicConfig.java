package com.example.authservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic activateAccountTopic() {
        return TopicBuilder.name("activate-account")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic changePasswordTopic() {
        return TopicBuilder.name("change-password")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic lockAccountTopic() {
        return TopicBuilder.name("lock-account")
                .partitions(2)
                .replicas(1)
                .build();
    }
}
