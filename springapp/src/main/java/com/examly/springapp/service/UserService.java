package com.examly.springapp.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.examly.springapp.dto.LoginDTO;
import com.examly.springapp.dto.UserDTO;
import com.examly.springapp.model.User;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);
    List<User> getAllUsers();
    UserDetails loadUserByUsername(String email);
    LoginDTO loginUser(LoginDTO loginDTO); 
    
}
