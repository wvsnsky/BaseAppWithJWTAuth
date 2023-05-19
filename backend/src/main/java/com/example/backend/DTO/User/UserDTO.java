package com.example.backend.DTO.User;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    private Long id;
    private String email;
    private String role;

}
