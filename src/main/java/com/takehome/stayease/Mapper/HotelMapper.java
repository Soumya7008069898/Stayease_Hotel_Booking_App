package com.takehome.stayease.Mapper;

import com.takehome.stayease.DTO.ResponseDto.HotelResponseDto;
import com.takehome.stayease.Entity.Hotel;

public class HotelMapper {
    public static HotelResponseDto convertToDto(Hotel hotel){
        HotelResponseDto response=new HotelResponseDto();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setLocation(hotel.getLocation());
        response.setDescription(hotel.getDescription());
        response.setTotalRooms(hotel.getTotalRooms());
        response.setAvailableRooms(hotel.getAvailableRooms());

        return response;
    }
}
