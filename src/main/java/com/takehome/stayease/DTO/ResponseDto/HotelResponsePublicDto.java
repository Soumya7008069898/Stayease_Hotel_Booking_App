package com.takehome.stayease.DTO.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//response of hotel used to fetch all hotels as total rooms should be hidden from public
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class HotelResponsePublicDto {

    private Long id;
    private String name;
    private String location;
    private String description;
    private Integer availableRooms;
    
}
