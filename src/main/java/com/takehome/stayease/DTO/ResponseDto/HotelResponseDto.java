package com.takehome.stayease.DTO.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//response of hotel when created and updated.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDto {

    private Long id;
    private String name;
    private String location;
    private String description;
    private Integer totalRooms;
    private Integer availableRooms;
    
}
