package com.takehome.stayease.Service.Implementation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takehome.stayease.DTO.RequestDto.CreateHotelDto;
import com.takehome.stayease.DTO.RequestDto.UpdateHotelDto;
import com.takehome.stayease.Entity.Hotel;
import com.takehome.stayease.Exception.AvailableRoomsExceededException;
import com.takehome.stayease.Exception.ResourceNotFoundException;
import com.takehome.stayease.Repository.BookingRepository;
import com.takehome.stayease.Repository.HotelRepository;
import com.takehome.stayease.Service.HotelService;

@Service
public class HotelServiceImpl implements HotelService {
    
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Hotel createHotel(CreateHotelDto request) {
        // TODO Auto-generated method stub
        Hotel hotel=Hotel.builder()
                    .name(request.getName())
                    .location(request.getLocation())
                    .description(request.getDescription())
                    .totalRooms(request.getTotalRooms())
                    .availableRooms(request.getAvailableRooms())
                    .build();

        return hotelRepository.save(hotel);   
    }

    @Override
    public List<Hotel> getAllHotels() {
        // TODO Auto-generated method stub
        List<Hotel> allHotels=hotelRepository.findAll();
        return allHotels;
    }

    @Override
    public Map<String, Object> updateHotel(Long id, UpdateHotelDto request) {
        // TODO Auto-generated method stub

        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id: "+id+" doesn't exist"));

        Map<String,Object> responseMap=new LinkedHashMap<>();
        responseMap.put("id",hotel.getId());

        if(request.getName()!=null){
            hotel.setName(request.getName());
            responseMap.put("name",request.getName());
        }

        if(request.getLocation()!=null){
            hotel.setLocation(request.getLocation());
            responseMap.put("location",request.getLocation());
        }

        if(request.getDescription()!=null){
            hotel.setDescription(request.getDescription());
            responseMap.put("description",request.getDescription());
        }

        Integer newTotalRooms = request.getTotalRooms() != null ? request.getTotalRooms() : hotel.getTotalRooms();
        Integer newAvailableRooms = request.getAvailableRooms() != null ? request.getAvailableRooms() : hotel.getAvailableRooms();

        if (newAvailableRooms != null && newTotalRooms != null && (newAvailableRooms > newTotalRooms)) {
            throw new AvailableRoomsExceededException("Available rooms cannot exceed total rooms");
        }

        if (request.getTotalRooms() != null) {
            hotel.setTotalRooms(request.getTotalRooms());
            responseMap.put("totalRooms", request.getTotalRooms());
        }

        if (request.getAvailableRooms() != null) {
            hotel.setAvailableRooms(request.getAvailableRooms());
            responseMap.put("availableRooms", request.getAvailableRooms());
        }

        hotelRepository.save(hotel);
        return responseMap;

    }

    @Transactional
    @Override
    public void deleteHotel(Long id) {
        // TODO Auto-generated method stub
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id: "+id+" doesn't exist"));
        bookingRepository.deleteByHotel(hotel);
        hotelRepository.delete(hotel);
    }
    
}
