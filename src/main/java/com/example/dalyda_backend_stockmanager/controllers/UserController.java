package com.example.dalyda_backend_stockmanager.controllers;

import com.example.dalyda_backend_stockmanager.dtos.UserDto;
import com.example.dalyda_backend_stockmanager.entities.Role;
import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import com.example.dalyda_backend_stockmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "Handles Users")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserService userService;

    // user Registration
    @Operation(summary = "User Registration", description = "Register Users")
    @PostMapping("/user/registration")
    public ResponseEntity<GenericResponse<UserDto.ViewUserDto>> userRegistration(@Valid @RequestBody UserDto.SignupDto signupDto, @RequestParam Role role) {
        var userSignup = userService.signup(signupDto, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse<>("User Registered Successful", userSignup));
    }
}
