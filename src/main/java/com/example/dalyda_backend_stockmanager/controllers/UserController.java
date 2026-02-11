package com.example.dalyda_backend_stockmanager.controllers;

import com.example.dalyda_backend_stockmanager.dtos.UserDto;
import com.example.dalyda_backend_stockmanager.entities.Role;
import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import com.example.dalyda_backend_stockmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    // default password reset and sent to your email endpoint
    @Operation(summary = "Password Reset", description = "Reset your password, a default password is sent to your email")
    @PostMapping("/password/reset")
    public ResponseEntity<GenericResponse<String>> defaultPassword(
            @Email(message = "Invalid Email Format")
            @NotBlank(message = "Email is required")
            @RequestParam String email) {
        var userEmail = userService.passwordReset(email);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Password Reset Successful, a default password was sent to your email", userEmail));
    }
}
