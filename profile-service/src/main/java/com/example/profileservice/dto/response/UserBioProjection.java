package com.example.profileservice.dto.response;

import com.example.ConnectionStatus;

public interface UserBioProjection {

    Long getId();

    String getFullName();

    String getAvatarUrl();

    String getBio();

    ConnectionStatus getStatus();
}