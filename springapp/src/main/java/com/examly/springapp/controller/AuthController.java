package com.examly.springapp.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.config.JwtUtils;
import com.examly.springapp.dto.LoginDTO;
import com.examly.springapp.dto.UserDTO;
import com.examly.springapp.model.User;
import com.examly.springapp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


/**
 * This class is a REST controller for handling authentication-related requests.
 * It provides endpoints for user registration, login, and fetching all users.
 */
@RestController
@RequestMapping("/api/")
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils; 
    private final AuthenticationManager authenticationManager;

     /** Constructor-based dependency injection for UserService, JwtUtils, and AuthenticationManager.
     * 
     * @param userService the service for user-related operations
     * @param jwtUtils the utility class for JWT operations
     * @param authenticationManager the manager for authentication operations
     */
    public AuthController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

     /**
     * Endpoint for user registration.
     * 
     * @param userDTO the data transfer object containing user details
     * @return a ResponseEntity containing the created UserDTO and HTTP status 201
     */


    
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
    @PostMapping("register")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
        userDTO = userService.createUser(userDTO);
        return ResponseEntity.status(201).body(userDTO); 
    }
  
    /**
     * Endpoint for user login.
     * 
     * @param loginDTO the data transfer object containing login details
     * @return a ResponseEntity containing the LoginDTO with token and HTTP status 200, or an error message with HTTP status 401
     */

    @Operation(summary = "Authenticate and log in a user", description = "Authenticates user credentials and generates a JWT token.")
    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.genrateToken(authentication);

        LoginDTO existingUserDTO = userService.loginUser(loginDTO);
        if (existingUserDTO == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Set the token in the response DTO
        existingUserDTO.setToken(token);

        return ResponseEntity.status(200).body(existingUserDTO);
    }
    
    @Operation(summary = "Get Users from Database", description = "Get All Users from the Database.")
    @GetMapping("users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> list = userService.getAllUsers();
        return ResponseEntity.status(200).body(list); 
    }
}




 