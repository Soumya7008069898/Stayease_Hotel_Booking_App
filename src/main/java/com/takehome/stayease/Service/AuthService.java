package com.takehome.stayease.Service;

import com.takehome.stayease.DTO.RequestDto.LoginRequestDto;
import com.takehome.stayease.DTO.RequestDto.UserRequestDto;
import com.takehome.stayease.DTO.ResponseDto.AuthResponseDto;

public interface AuthService {
    AuthResponseDto registerUser(UserRequestDto request);
    AuthResponseDto loginUser(LoginRequestDto request);
}
