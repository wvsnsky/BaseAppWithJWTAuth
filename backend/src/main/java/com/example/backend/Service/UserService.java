package com.example.backend.Service;

import com.example.backend.DTO.User.UserDTO;
import com.example.backend.Entity.User;
import com.example.backend.Mapper.UserMapper;
import com.example.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDTO save(UserDTO userDTO) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDTO)));
    }

    @Transactional
    public Optional<UserDTO> findOne(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }


    public void delete(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found user with id: " + id + " to delete"));
        userRepository.delete(user);
    }
}
