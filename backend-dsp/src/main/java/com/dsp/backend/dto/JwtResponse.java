package com.dsp.backend.dto;

import lombok.Data; // Requiere la dependencia Lombok

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private java.util.List<String> roles;

    public JwtResponse(String accessToken, String username, java.util.List<String> roles) {
        this.token = accessToken;
        this.username = username;
        this.roles = roles;
    }
}
