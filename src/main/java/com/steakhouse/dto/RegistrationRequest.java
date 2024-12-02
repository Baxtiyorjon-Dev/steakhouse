package com.steakhouse.dto;

import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.Email;


public class RegistrationRequest {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    @Size(min = 6)
    private String passwordConfirm;

    // Getters and Setters

    public @Size(min = 3, max = 50) String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 3, max = 50) String username) {
        this.username = username;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public @Size(min = 6) String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 6) String password) {
        this.password = password;
    }

    public @Size(min = 6) String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(@Size(min = 6) String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
