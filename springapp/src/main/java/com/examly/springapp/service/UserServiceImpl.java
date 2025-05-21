package com.examly.springapp.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examly.springapp.config.UserPrinciple;
import com.examly.springapp.dto.LoginDTO;
import com.examly.springapp.dto.UserDTO;
import com.examly.springapp.exception.UserNotFoundException;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;
import com.examly.springapp.utility.UserMapper;

/**
 * This class implements the UserService and UserDetailsService interfaces and provides
 * business logic for handling user-related operations.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    /**
     * Constructor-based dependency injection for UserRepo and PasswordEncoder.
     */
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    /**
     * Creates a new user.
     * Validates the user details and checks for existing user before saving.
     * Logs the creation process and returns the saved user as a UserDTO.
     */
    @Override 
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating user: {}", userDTO);
        User user = UserMapper.mapToUser(userDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        User existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser != null) {
            logger.error("User already exists with email: {}", user.getEmail());
            throw new UserNotFoundException("User already exists!!!");
        }
        User savedUser = userRepo.save(user);
        logger.info("User created successfully: {}", savedUser);
        return UserMapper.mapToUserDTO(savedUser);
    }

    /**
     * Logs in a user.
     * Validates the user credentials and returns a LoginDTO if successful.
     * Throws UserNotFoundException if the user is not found or credentials are invalid.
     */
    @Override
    public LoginDTO loginUser(LoginDTO loginDTO) {
        logger.info("Logging in user with email: {}", loginDTO.getEmail());
        User existingUser = userRepo.findByEmail(loginDTO.getEmail());
        if (existingUser == null) {
            logger.error("User not found with email: {}", loginDTO.getEmail());
            throw new UserNotFoundException("User not found!!!");
        }
        if (encoder.matches(loginDTO.getPassword(), existingUser.getPassword())) {
            logger.info("User logged in successfully: {}", existingUser.getUsername());
            return new LoginDTO(
                null, // Token will be set later in the controller
                existingUser.getUsername(),
                null, // Do not include the password in the response
                existingUser.getUserRole(),
                existingUser.getUserId(),
                existingUser.getEmail(),
                existingUser.getMobileNumber()
            );
        }
        logger.error("Invalid credentials for user: {}", loginDTO.getEmail());
        throw new UserNotFoundException("Invalid Credentials");
    }

    /**
     * Retrieves all users.
     * Logs the retrieval process and returns the list of users.
     */
    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepo.findAll();
        logger.info("Users found: {}", users);
        return users;
    }

    /**
     * Loads user details by username.
     * Logs the loading process and returns the user details if found.
     * Throws UserNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("Loading user by username: {}", username);
        User user = userRepo.findByEmail(username);
        if (user == null) {
            logger.error("User not found with username: {}", username);
            throw new UserNotFoundException("User not found");
        }
        logger.info("User found: {}", user);
        return UserPrinciple.build(user);
    }
}
