package com.example.dalyda_backend_stockmanager.controllers;

import com.example.dalyda_backend_stockmanager.dtos.UserDto;
import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import com.example.dalyda_backend_stockmanager.services.TokenRevocationService;
import com.example.dalyda_backend_stockmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    private final TokenRevocationService tokenRevocationService;

    // login endpoint
    @Operation(summary = "User Login", description = "User Login Endpoint")
    @PostMapping("/login")
    public ResponseEntity<GenericResponse<String>> login(@Valid @RequestBody UserDto.LoginDto loginDto) {
        var token = userService.logIn(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("token to be used to login", token));
    }

    // logout endpoint
    @Operation(summary = "User Logout", description = "User Logout Endpoint - Revokes the JWT token")
    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<String>> logout(HttpServletRequest request,
                                                          HttpServletResponse response,
                                                          Authentication authentication) {
        if (authentication != null) {
            // Extract JWT token from Authorization header
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                // Revoke the token (add to blacklist)
                tokenRevocationService.revokeToken(token);
            }

            // Clear security context
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("User logged out successfully. Token has been revoked.", null));
    }
}
