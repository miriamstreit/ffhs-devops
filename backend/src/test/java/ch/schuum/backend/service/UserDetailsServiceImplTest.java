package ch.schuum.backend.service;

import ch.schuum.backend.model.User;
import ch.schuum.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    User user;
    String password;

    @BeforeEach
    void setUp() {
        password = BCrypt.hashpw("securepassword", BCrypt.gensalt());
        user = new User(1, "admin@schuum.ch", password, null);
    }

    @Test
    void test_loadUserByUsername_whenUserExists_returnUser() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));

        // Act
        UserDetails dbUser = userDetailsService.loadUserByUsername(user.getEmail());

        // Assert
        assertEquals(user.getEmail(), dbUser.getUsername());
        assertEquals(0, dbUser.getAuthorities().size());
        assertEquals(password, dbUser.getPassword());
    }

    @Test
    void test_loadUserByUsername_whenUserDoesntExist_throwException() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        String email = user.getEmail();

        // Act
        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email),
                "Expected findByEmail() to throw, but it didn't"
        );

        // Assert
        assertTrue(thrown.getMessage().contentEquals("Benutzer mit E-Mail " + user.getEmail() + " konnte nicht gefunden werden."));
    }
}