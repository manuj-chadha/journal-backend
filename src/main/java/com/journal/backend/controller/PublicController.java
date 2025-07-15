package com.journal.backend.controller;

import com.journal.backend.dto.ApiResponse;
import com.journal.backend.dto.AuthResponse;
import com.journal.backend.dto.UserDTO;
import com.journal.backend.dto.UserResponseDTO;
import com.journal.backend.entity.User;
import com.journal.backend.services.CustomUserDetailsServiceImpl;
import com.journal.backend.services.UserService;
import com.journal.backend.utils.JwtService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            userService.createNewUser(userDTO);
            User createdUser = userService.findByUsername(userDTO.getUsername()); // You must implement this
            String jwt = jwtService.generateToken(createdUser.getUsername());

            UserResponseDTO userResponse = UserResponseDTO.builder()
                    .id(createdUser.getId().toHexString())
                    .username(createdUser.getUsername())
                    .email(createdUser.getEmail())
                    .journalEntries(createdUser.getJournalEntries())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<AuthResponse>builder()
                            .success(true)
                            .message("User registered and logged in")
                            .data(AuthResponse.builder().token(jwt).user(userResponse).build())
                            .build()
            );
        } catch (Exception e) {
            log.error("Signup failed for user: {}", userDTO.getUsername(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.<AuthResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtService.generateToken(userDetails.getUsername());

            User loggedInUser = userService.findByUsername(user.getUsername());

            UserResponseDTO userResponse = UserResponseDTO.builder()
                    .id(loggedInUser.getId().toHexString())
                    .username(loggedInUser.getUsername())
                    .email(loggedInUser.getEmail())
                    .journalEntries(loggedInUser.getJournalEntries())
                    .build();

            return ResponseEntity.ok(
                    ApiResponse.<AuthResponse>builder()
                            .success(true)
                            .message("Login successful")
                            .data(AuthResponse.builder().token(jwt).user(userResponse).build())
                            .build()
            );
        } catch (Exception e) {
            log.error("Login failed for user: {}", user.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.<AuthResponse>builder()
                            .success(false)
                            .message("Invalid username or password")
                            .data(null)
                            .build()
            );
        }
    }

}