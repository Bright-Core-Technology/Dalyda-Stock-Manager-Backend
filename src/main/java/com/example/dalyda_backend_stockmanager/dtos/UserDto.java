package com.example.dalyda_backend_stockmanager.dtos;

import com.example.dalyda_backend_stockmanager.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

public class UserDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewUserDto {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String phoneNumber;
        private Role role;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupDto {
        @NotBlank(message="firstName can not be blank")
        private String firstName;
        @NotBlank(message="lastName can not be blank")
        private String lastName;
        @Email(message="Invalid Email Format")
        @NotBlank(message="Email is Required")
        private String email;
        @NotBlank(message="phone Number can not blank")
        private String phoneNumber;
    }
}
