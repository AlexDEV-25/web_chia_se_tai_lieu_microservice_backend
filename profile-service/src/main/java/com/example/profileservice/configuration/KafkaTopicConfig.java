package com.example.profileservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic followUserTopic() {
        return TopicBuilder.name("follow-user")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userProfileUpdatedTopic() {
        return TopicBuilder.name("user-profile-updated")
                .partitions(2)
                .replicas(1)
                .build();
    }
}
