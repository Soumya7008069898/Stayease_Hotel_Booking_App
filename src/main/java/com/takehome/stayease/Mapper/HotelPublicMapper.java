package com.takehome.stayease.Mapper;

import com.takehome.stayease.DTO.ResponseDto.HotelResponsePublicDto;
import com.takehome.stayease.Entity.Hotel;

public class HotelPublicMapper {
    public static HotelResponsePublicDto convertToDto(Hotel hotel){
        HotelResponsePublicDto response=new HotelResponsePublicDto();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setLocation(hotel.getLocation());
        response.setDescription(hotel.getDescription());
        response.setAvailableRooms(hotel.getAvailableRooms());

        return response;
    }
}
