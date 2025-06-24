package com.takehome.stayease.Service;

import java.util.List;
import java.util.Map;

import com.takehome.stayease.DTO.RequestDto.CreateHotelDto;
import com.takehome.stayease.DTO.RequestDto.UpdateHotelDto;
import com.takehome.stayease.Entity.Hotel;
public interface HotelService {
    Hotel createHotel(CreateHotelDto request);
    List<Hotel> getAllHotels();
    Map<String,Object> updateHotel(Long id,UpdateHotelDto request);
    void deleteHotel(Long id);
}
