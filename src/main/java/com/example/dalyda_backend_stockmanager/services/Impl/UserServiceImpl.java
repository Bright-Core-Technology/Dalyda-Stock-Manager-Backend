package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.dtos.UserDto;
import com.example.dalyda_backend_stockmanager.entities.Role;
import com.example.dalyda_backend_stockmanager.entities.Users;
import com.example.dalyda_backend_stockmanager.mappers.UserMapper;
import com.example.dalyda_backend_stockmanager.repositories.UserRepository;
import com.example.dalyda_backend_stockmanager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSenderImpl mailSender;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;

    // method to generate random password as the default password
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    // method to send mails with the default password when you Sign Up
    private void sendWelcomeEmail(String to, String password, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to DALYDA");
        message.setText("Hello," + " " + firstName +
                "\n\nWelcome to DALYDA, Your temporary password is: " + password +
                "\n\nPlease log in and change it to your own password.\n\nThank you.");

        mailSender.send(message);
    }

    // SignUp method
    @Override
    public UserDto.ViewUserDto signup(UserDto.SignupDto signupDto, Role role) {
        Users user = UserMapper.map(signupDto);

        // If Email exist in the database
        if (userRepository.existsByEmail(user.getEmail())) throw new DuplicateKeyException("Email already in use");

        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.setRole(role);
        userRepository.save(user);

        sendWelcomeEmail(user.getEmail(), rawPassword, user.getFirstname());
        return UserMapper.map(user);
    }

    @Override
    public String logIn(UserDto.LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        var user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return jwtService.generateToken(user);
    }

    @Override
    public String passwordReset(String email) {

        var user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email does not exist"));
        String password = generateRandomPassword();

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Password");
        message.setText("Hello," +
                "\n\n Your new Password is:" + " " + password +
                "\n\nPlease log in and update your password.\n\nThank you.");

        mailSender.send(message);
        return email;
    }

}
