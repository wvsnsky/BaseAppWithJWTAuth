package com.example.backend.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAuthResponseDTO {
    private String email;
    private String accessToken;
}
