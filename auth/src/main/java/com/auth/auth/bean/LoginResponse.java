package com.auth.auth.bean;

import lombok.Data;

@Data
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private Long tokenExpiryTime;

}
