package com.dsp.backend.dto;

import lombok.Data; // Requiere la dependencia Lombok

@Data // Genera getters, setters, etc.
public class LoginRequest {
    private String username;
    private String password;
}