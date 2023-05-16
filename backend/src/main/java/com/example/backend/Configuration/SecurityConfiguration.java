package com.example.backend.Configuration;

import com.example.backend.JWT.JwtTokenFilter;
import com.example.backend.Entity.User;
import com.example.backend.Repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Optional;

@Configuration
public class SecurityConfiguration {

    private final UserRepository userRepository;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfiguration(UserRepository userRepository, JwtTokenFilter jwtTokenFilter) {
        this.userRepository = userRepository;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void saveUser() {
        Optional<User> existingUser = userRepository.findByEmail("adrian@test.pl");
        if (existingUser.isEmpty()) {
            User user = new User();
            user.setEmail("adrian@test.pl");
            user.setPassword(getBcryptPasswordEncoder().encode("admin123"));
            user.setRole("ROLE_ADMIN");
            userRepository.save(user);
        }
        Optional<User> existingUser2 = userRepository.findByEmail("adrian2@test.pl");
        if (existingUser2.isEmpty()) {
            User user = new User();
            user.setEmail("adrian2@test.pl");
            user.setPassword(getBcryptPasswordEncoder().encode("admin123"));
            user.setRole("ROLE_USER");
            userRepository.save(user);
        }
    }

    @Bean
    public PasswordEncoder getBcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // authentication is about checking if user with given credentials is in db; authorization gives him appropriate permissions
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // filter chain is about defining policy in the app - i dont want to store user data on backend and everyone has access to /auth/login endpoint, when the access to the rest of api needs auth
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // app not caching user data on backend after auth
        http.authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/hello").hasRole("ADMIN") // its really important to save roles on base like "ROLE_ADMIN", "ROLE_USER" etc.
                .anyRequest().authenticated();
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // before every request which need to be authenticated user, do jwtTokenFilter
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User with email: {}" + username + " not found"));
    }
}
