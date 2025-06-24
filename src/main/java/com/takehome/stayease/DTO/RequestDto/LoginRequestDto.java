package com.takehome.stayease.DTO.RequestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {
    
    @Email(message = "invalid format of email")
    @NotBlank(message="email cannot be blank")
    private String email;

    @NotBlank(message="password cannot be blank")
    private String password;
    
}
