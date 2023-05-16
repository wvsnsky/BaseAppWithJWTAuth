package com.example.backend.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthRequestDTO {
    private String email;
    private String password;
}
