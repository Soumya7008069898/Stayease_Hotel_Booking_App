package com.takehome.stayease.Service;

import com.takehome.stayease.DTO.RequestDto.LoginRequestDto;
import com.takehome.stayease.DTO.RequestDto.UserRequestDto;
import com.takehome.stayease.DTO.ResponseDto.AuthResponseDto;
import com.takehome.stayease.Entity.Enum.Role;
import com.takehome.stayease.Entity.User;
import com.takehome.stayease.Repository.UserRepository;
import com.takehome.stayease.Service.Implementation.AuthServiceImpl;
import com.takehome.stayease.Service.Implementation.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    // test method for successful registration
    @Test
    void testRegisterUser_SuccessfulRegistration() {
        //mock for dto
        UserRequestDto request = new UserRequestDto();
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setEmail("alice@example.com");
        request.setPassword("secret");
        request.setRole(null); // should default to CUSTOMER

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded-password");

        //mock of registered user
        User savedUser = User.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@example.com")
                .role(Role.CUSTOMER)
                .password("encoded-password")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");

        //method invocation
        AuthResponseDto response = authService.registerUser(request);

        //assertion
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    // @Test
    // void testRegisterUser_EmailAlreadyExists_ShouldThrowException() {
    //     UserRequestDto request = new UserRequestDto();
    //     request.setEmail("existing@example.com");

    //     when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

    //     Exception ex = assertThrows(IllegalArgumentException.class, () -> {
    //         authService.registerUser(request);
    //     });

    //     assertEquals("User with email already exists: existing@example.com", ex.getMessage());
    //     verify(userRepository, never()).save(any(User.class));
    // }

    //test method for successful login
    @Test
    void testLoginUser_SuccessfulLogin() {
        //setting up the dto
        LoginRequestDto login = new LoginRequestDto();
        login.setEmail("login@example.com");
        login.setPassword("secret");

        //create user mock
        User user = User.builder()
                .email("login@example.com")
                .password("encoded-password")
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.findByEmail("login@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("login-jwt-token");

        //method invocation
        AuthResponseDto response = authService.loginUser(login);

        //assertions
        assertNotNull(response);
        assertEquals("login-jwt-token", response.getToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("login@example.com", "secret"));
        verify(jwtService).generateToken(user);
    }

    //test for login of user which doesnt exist
    @Test
    void testLoginUser_UserNotFound_ShouldThrowException() {
        //setting up of dto
        LoginRequestDto login = new LoginRequestDto();
        login.setEmail("notfound@example.com");
        login.setPassword("123");

        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        //assertions
        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loginUser(login);
        });

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("notfound@example.com", "123"));
    }
}
