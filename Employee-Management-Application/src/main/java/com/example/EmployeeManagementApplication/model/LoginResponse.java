package com.example.EmployeeManagementApplication.model;

import lombok.Builder;

@Builder
public class LoginResponse {
    private String accessToken;
    public LoginResponse() {
        // No-argument constructor
    }

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getAccessToken() {
        return accessToken;
    }
}
