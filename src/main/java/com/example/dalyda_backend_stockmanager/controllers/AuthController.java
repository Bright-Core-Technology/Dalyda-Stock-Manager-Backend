package com.example.dalyda_backend_stockmanager.controllers;

import com.example.dalyda_backend_stockmanager.dtos.UserDto;
import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import com.example.dalyda_backend_stockmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller", description = "Handles user login authentication")
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/auth")

public class AuthController {

    private final UserService userService;

    // login endpoint
    @Operation(summary = "User Login", description = "User Login Endpoint")
    @PostMapping("/login")
    public ResponseEntity<GenericResponse<String>> login(@Valid @RequestBody UserDto.LoginDto loginDto) {
        var token = userService.logIn(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("token to be used to login", token));
    }

}
