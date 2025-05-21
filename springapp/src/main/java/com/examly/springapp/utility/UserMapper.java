package com.examly.springapp.utility;

import com.examly.springapp.dto.LoginDTO;
import com.examly.springapp.dto.UserDTO;
import com.examly.springapp.model.User;

public class UserMapper {

    public static UserDTO mapToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setMobileNumber(user.getMobileNumber());
        userDTO.setUsername(user.getUsername());
        userDTO.setUserRole(user.getUserRole());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }  
    
    public static User mapToUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setUsername(userDTO.getUsername());
        user.setUserRole(userDTO.getUserRole());
        user.setPassword(userDTO.getPassword());
        return user;
    }  

    public static LoginDTO mappedToLoginDTO(User user){
        String token="token"; // coming from jwtUtils
        LoginDTO loginDTO= new LoginDTO();
        loginDTO.setToken(token);
        loginDTO.setUserId(user.getUserId());
        loginDTO.setUsername(user.getUsername());
        loginDTO.setEmail(user.getEmail());
        loginDTO.setMobileNumber(user.getMobileNumber());
        loginDTO.setUserRole(user.getUserRole());
        return loginDTO;
    }
}
