package com.example.backend.Resource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.backend.DTO.User.UserAuthRequestDTO;
import com.example.backend.DTO.User.UserAuthResponseDTO;
import com.example.backend.DTO.User.UserDTO;
import com.example.backend.Entity.User;
import com.example.backend.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserResource {

    private static final String ENTITY_NAME = "user";
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
    public ResponseEntity createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok().body(userService.save(userDTO));
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        if (userDTO.getId() == null) {
            throw new RuntimeException("Social network post not found: " + userDTO);
        }
        UserDTO result = userService.save(userDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDTO.getId().toString()))
                .body(result);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.debug("REST request to get all Users");
        List<UserDTO> usersList = userService.findAll();
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.debug("REST request to get User with id: {}", id);
        Optional<UserDTO> userDTO = userService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDTO);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete User with id: {}", id);
        try {
            userService.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException exception) {
            log.debug(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
