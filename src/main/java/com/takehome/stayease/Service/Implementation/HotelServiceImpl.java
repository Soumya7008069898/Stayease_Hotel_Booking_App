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

    //method for creating hotel
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
    
    //method for fetching all hotels
    @Override
    public List<Hotel> getAllHotels() {
        // TODO Auto-generated method stub
        List<Hotel> allHotels=hotelRepository.findAll();
        return allHotels;
    }

    //method to update hotel
    @Override
    public Map<String, Object> updateHotel(Long id, UpdateHotelDto request) {
        // TODO Auto-generated method stub
        //fetch hotel by hotel id
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id: "+id+" doesn't exist"));

        Map<String,Object> responseMap=new LinkedHashMap<>();
        responseMap.put("id",hotel.getId());
        //update name if needed
        if(request.getName()!=null){
            hotel.setName(request.getName());
            responseMap.put("name",request.getName());
        }
        //update location if needed
        if(request.getLocation()!=null){
            hotel.setLocation(request.getLocation());
            responseMap.put("location",request.getLocation());
        }
        //update description if needed
        if(request.getDescription()!=null){
            hotel.setDescription(request.getDescription());
            responseMap.put("description",request.getDescription());
        }

        //checking condition that total rooms > available rooms before and after update
        Integer newTotalRooms = request.getTotalRooms() != null ? request.getTotalRooms() : hotel.getTotalRooms();
        Integer newAvailableRooms = request.getAvailableRooms() != null ? request.getAvailableRooms() : hotel.getAvailableRooms();

        if (newAvailableRooms != null && newTotalRooms != null && (newAvailableRooms > newTotalRooms)) {
            throw new AvailableRoomsExceededException("Available rooms cannot exceed total rooms");
        }

        //update total rooms
        if (request.getTotalRooms() != null) {
            hotel.setTotalRooms(request.getTotalRooms());
            responseMap.put("totalRooms", request.getTotalRooms());
        }

        //update available rooms
        if (request.getAvailableRooms() != null) {
            hotel.setAvailableRooms(request.getAvailableRooms());
            responseMap.put("availableRooms", request.getAvailableRooms());
        }

        hotelRepository.save(hotel);
        return responseMap;

    }

    //method to delete hotel
    @Transactional
    @Override
    public void deleteHotel(Long id) {
        // TODO Auto-generated method stub
        //hotel is fetched by id
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id: "+id+" doesn't exist"));
        //bookings of that hotel are deleted
        bookingRepository.deleteByHotel(hotel);
        //hotel is deleted
        hotelRepository.delete(hotel);
    }
    
}
