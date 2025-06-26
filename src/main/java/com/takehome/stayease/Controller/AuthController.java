package com.takehome.stayease.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.takehome.stayease.DTO.RequestDto.LoginRequestDto;
import com.takehome.stayease.DTO.RequestDto.UserRequestDto;
import com.takehome.stayease.DTO.ResponseDto.AuthResponseDto;
// import com.takehome.stayease.Entity.User;
import com.takehome.stayease.Service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    //controller method for registering user. this is accessed by all roles
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody UserRequestDto request){
        AuthResponseDto response=authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);// changed to 200.
    }

    //controller method for login user.
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto request){
        AuthResponseDto response=authService.loginUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
