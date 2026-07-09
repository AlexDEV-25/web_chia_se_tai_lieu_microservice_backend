package com.example.studyservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic authorHideDocumentToFollowerTopic() {
        return TopicBuilder.name("author-hide-document-to-follower")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic authorDeleteDocumentToFollowerTopic() {
        return TopicBuilder.name("author-delete-document-to-follower")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic adminApproveDocumentToAuthorTopic() {
        return TopicBuilder.name("admin-approve-document-to-author")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic adminApproveDocumentToFollowerTopic() {
        return TopicBuilder.name("admin-approve-document-to-follower")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic adminHideDocumentToAuthorTopic() {
        return TopicBuilder.name("admin-hide-document-to-author")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic adminHideDocumentToFollowerTopic() {
        return TopicBuilder.name("admin-hide-document-to-follower")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic adminDeleteDocumentToAuthorTopic() {
        return TopicBuilder.name("admin-delete-document-to-author")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic adminDeleteDocumentToFollowerTopic() {
        return TopicBuilder.name("admin-delete-document-to-follower")
                .partitions(2)
                .replicas(1)
                .build();
    }
}
