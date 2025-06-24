package com.takehome.stayease.DTO.RequestDto;

import com.takehome.stayease.Entity.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    
    @Email(message="invalid email format")
    @NotBlank(message="email cannot be null")
    private String email;

    @NotBlank(message="password cannot be blank")
    @Size(min = 7, message = "Password must be at least 7 characters")
    private String password;

    @NotBlank(message="firstName cannot be blank")
    private String firstName;

    @NotBlank(message="lastName cannot be blank")
    private String lastName;

    Role role;

}
