package com.takehome.stayease.DTO.RequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Dto for creating a hotel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHotelDto {
    
    @NotBlank(message="name cannot be blank")
    private String name;

    @NotBlank(message="location cannot be blank")
    private String location;

    @NotBlank(message = "description cannot be blank")
    private String description;

    // @NotNull(message="total rooms cannot be blank")
    // @Min(value=1,message="total rooms should be atleast 1")
    private Integer totalRooms;

    @NotNull(message="available rooms cannot be blank")
    @Min(value=0,message="available rooms should be atleast 0")
    private Integer availableRooms;
    
}
