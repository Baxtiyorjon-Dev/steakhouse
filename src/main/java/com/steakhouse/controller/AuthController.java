package com.steakhouse.controller;

import com.steakhouse.dto.ApiResponse;
import com.steakhouse.dto.RegistrationRequest;
import com.steakhouse.exception.ValidationException;
import com.steakhouse.model.User;
import com.steakhouse.security.JwtTokenUtil;
import com.steakhouse.service.AuthService;
import com.steakhouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<User>> showOwnProfile(
            @RequestBody @Valid User userDetails, Authentication authentication) {

        String username = authentication.getName();
        User user = userService.findByUsername(username);
        ApiResponse<User> response = new ApiResponse<>("success", user, "Profile updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        if (!registrationRequest.getPassword().equals(registrationRequest.getPasswordConfirm())) {
            throw new ValidationException("Passwords do not match");
        }

        if (userService.findByUsername(registrationRequest.getUsername()) != null) {
            throw new ValidationException("Username is already taken");
        }

        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());

        userService.registerUser(user);

        ApiResponse<String> response = new ApiResponse<>("success", null, "User registered successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> createAuthenticationToken(@RequestBody @Valid RegistrationRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = authService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        ApiResponse<String> response = new ApiResponse<>("success", jwt, "Login successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Foydalanuvchi o‘z profilini yangilash
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<User>> updateOwnProfile(
            @RequestBody @Valid User userDetails, Authentication authentication) {

        String username = authentication.getName();
        User updatedUser = userService.updateUserByUsername(username, userDetails);
        ApiResponse<User> response = new ApiResponse<>("success", updatedUser, "Profile updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Foydalanuvchi o‘z profilini o‘chirish
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> deleteOwnProfile(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUserByUsername(username);
        ApiResponse<String> response = new ApiResponse<>("success", null, "Profile deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
