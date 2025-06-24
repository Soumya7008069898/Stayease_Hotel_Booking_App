package com.takehome.stayease.DTO.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHotelDto {
    
    private String name;
    private String location;
    private String description;
    private Integer totalRooms;
    private Integer availableRooms;

}
