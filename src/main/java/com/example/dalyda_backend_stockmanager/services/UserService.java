package com.example.dalyda_backend_stockmanager.services;

import com.example.dalyda_backend_stockmanager.dtos.UserDto;
import com.example.dalyda_backend_stockmanager.entities.Role;

public interface UserService {
    UserDto.ViewUserDto signup(UserDto.SignupDto signupDto, Role role);

    String logIn(UserDto.LoginDto loginDto);

    String passwordReset(String email);
}
