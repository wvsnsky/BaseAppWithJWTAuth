package com.example.backend.Mapper;

import com.example.backend.DTO.User.UserDTO;
import com.example.backend.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public abstract class UserMapper implements EntityMapper<UserDTO, User> {

    public abstract UserDTO toDto(User user);

    public abstract User toEntity(UserDTO userDTO);

    public User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
