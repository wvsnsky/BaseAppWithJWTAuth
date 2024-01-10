package com.example.backend.Service;

import com.example.backend.DTO.User.UserDTO;
import com.example.backend.Entity.User;
import com.example.backend.Mapper.UserMapper;
import com.example.backend.Repository.UserRepository;
import com.example.backend.Service.Criteria.UserCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
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

    public Page<UserDTO> getFilteredUsers(UserCriteria filterCriteria, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<User> specification = createSpecification(filterCriteria);
        return userRepository.findAll(specification, pageable).map(userMapper::toDto);
    }

    public void updateUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findById(userDTO.getId());
        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();

            userToUpdate.setEmail(userDTO.getEmail());
            userToUpdate.setRole(userDTO.getRole());

            userRepository.save(userToUpdate);
        }
    }

    private Specification<User> createSpecification(UserCriteria filterCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterCriteria.getEmail() != null && !filterCriteria.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + filterCriteria.getEmail() + "%"));
            }

            if (filterCriteria.getRole() != null && !filterCriteria.getRole().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("role"), "%" + filterCriteria.getRole() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
