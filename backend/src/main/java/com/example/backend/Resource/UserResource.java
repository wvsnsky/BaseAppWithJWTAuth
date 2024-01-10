package com.example.backend.Resource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.backend.DTO.User.UserAuthRequestDTO;
import com.example.backend.DTO.User.UserAuthResponseDTO;
import com.example.backend.DTO.User.UserDTO;
import com.example.backend.Entity.User;
import com.example.backend.Service.Criteria.UserCriteria;
import com.example.backend.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserResource {

    @Value("${application.name}")
    private String applicationName;
    private static final Logger log = LoggerFactory.getLogger(UserResource.class);
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    public UserResource(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> auth(@RequestBody UserAuthRequestDTO userAuthRequestDTO) {
        // method generates jwt token
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthRequestDTO.getEmail(), userAuthRequestDTO.getPassword()));
            User user = (User) authenticate.getPrincipal();

            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                    .withSubject(user.getUsername())
                    .withIssuer("Adrian W")
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);

            UserAuthResponseDTO userAuthResponseDTO = new UserAuthResponseDTO(user.getUsername(), token);
            return ResponseEntity.ok(userAuthResponseDTO);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok().body(userService.save(userDTO));
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        Optional<UserDTO> userDTO = userService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getFilteredUsers(String filterCriteria, int pageNumber, int pageSize) throws JsonProcessingException {
        UserCriteria criteria = new ObjectMapper().readValue(filterCriteria, UserCriteria.class);
        Page<UserDTO> usersPage = userService.getFilteredUsers(criteria, pageNumber, pageSize);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), usersPage);
        return ResponseEntity.ok().headers(headers).body(usersPage.getContent());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException exception) {
            log.debug(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
