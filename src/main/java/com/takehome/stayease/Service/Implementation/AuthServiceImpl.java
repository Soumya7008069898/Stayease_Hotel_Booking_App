package com.takehome.stayease.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.takehome.stayease.DTO.RequestDto.LoginRequestDto;
import com.takehome.stayease.DTO.RequestDto.UserRequestDto;
import com.takehome.stayease.DTO.ResponseDto.AuthResponseDto;
import com.takehome.stayease.Entity.User;
import com.takehome.stayease.Entity.Enum.Role;
import com.takehome.stayease.Repository.UserRepository;
import com.takehome.stayease.Service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    //method to register user
    @Override
    public AuthResponseDto registerUser(UserRequestDto request) {
        // TODO Auto-generated method stub

        // if(userRepository.existsByEmail(request.getEmail())){
        //     throw new IllegalArgumentException("User with email already exists: " + request.getEmail());
        // }
        
        //assigning default role if role is not assigned
        if(request.getRole()==null){
            request.setRole(Role.CUSTOMER);
        }

        //building/creating the user from dto
        User user=User.builder()
                  .firstName(request.getFirstName())
                  .lastName(request.getLastName())
                  .email(request.getEmail())
                  .role(request.getRole())
                  .password(passwordEncoder.encode(request.getPassword()))
                  .build();
       //saving the user
        userRepository.save(user);
        //generating the jwt token and returning it in auth response
        String jwtToken=jwtService.generateToken(user);
        return AuthResponseDto.builder()
                              .token(jwtToken)
                              .build();
    }

    @Override
    public AuthResponseDto loginUser(LoginRequestDto request) {
        // TODO Auto-generated method stub
        //authentication using username and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));
        //getting the user by email
        User user= userRepository.findByEmail(request.getEmail())
                             .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        //generate the jwt token and return
        String jwtToken=jwtService.generateToken(user);
        return AuthResponseDto.builder()
                              .token(jwtToken)
                              .build();
    }
    
}
