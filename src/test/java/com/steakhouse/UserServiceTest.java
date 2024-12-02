package com.steakhouse;

import com.steakhouse.exception.ResourceNotFoundException;
import com.steakhouse.model.User;
import com.steakhouse.repository.UserRepository;
import com.steakhouse.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUserByUsername_UserExists() {
        User existingUser = new User();
        existingUser.setUsername("oldUsername");

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");
        updatedUser.setEmail("newemail@example.com");
        updatedUser.setPassword("newpassword");

        when(userRepository.findByUsername("oldUsername")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUserByUsername("oldUsername", updatedUser);

        assertEquals("newUsername", result.getUsername());
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPasswordHash());
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserByUsername("nonexistent", new User()));
    }

    @Test
    void testDeleteUserByUsername_UserExists() {
        User user = new User();
        user.setUsername("deleteUser");

        when(userRepository.findByUsername("deleteUser")).thenReturn(Optional.of(user));

        userService.deleteUserByUsername("deleteUser");

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserByUsername("nonexistent"));
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        assertEquals("encodedPassword", result.getPasswordHash());
        assertEquals("ROLE_CUSTOMER", result.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void testFindByUsername_UserExists() {
        User user = new User();
        user.setUsername("findUser");

        when(userRepository.findByUsername("findUser")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("findUser");

        assertEquals("findUser", result.getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        User result = userService.findByUsername("nonexistent");

        assertNull(result);
    }

    @Test
    void testUpdateUser_UserExists() {
        User existingUser = new User();
        existingUser.setId(1L);

        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals("updatedUsername", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPasswordHash());
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, new User()));
    }

    @Test
    void testDeleteUser_UserExists() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
