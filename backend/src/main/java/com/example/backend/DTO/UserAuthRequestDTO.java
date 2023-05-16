package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthRequestDTO {
    private String email;
    private String password;
}
