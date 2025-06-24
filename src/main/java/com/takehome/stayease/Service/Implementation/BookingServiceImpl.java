package com.takehome.stayease.Service.Implementation;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.takehome.stayease.DTO.RequestDto.CreateBookingDto;
import com.takehome.stayease.Entity.Booking;
import com.takehome.stayease.Entity.Hotel;
import com.takehome.stayease.Entity.User;
import com.takehome.stayease.Exception.ResourceNotFoundException;
import com.takehome.stayease.Repository.BookingRepository;
import com.takehome.stayease.Repository.HotelRepository;
import com.takehome.stayease.Service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Booking createBooking(Long id,CreateBookingDto request, Principal principal) {
        // TODO Auto-generated method stub
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("hotel not found with id: "+id));
        User user=(User)userDetailsService.loadUserByUsername(principal.getName());

        if(hotel.getAvailableRooms()<=0){
            throw new ResourceNotFoundException("No rooms available");   //resourcenotfoundexp
        } 

        if(!request.getCheckOutDate().isAfter(request.getCheckInDate())){
            throw new IllegalArgumentException("check-out date must be after check-in date");
        }

        Booking booking=Booking.builder()
                        .user(user)
                        .hotel(hotel)
                        .checkInDate(request.getCheckInDate())
                        .checkOutDate(request.getCheckOutDate())
                        .build();
        hotel.setAvailableRooms(hotel.getAvailableRooms()-1);
        hotelRepository.save(hotel);

        return bookingRepository.save(booking);                     
    }

    @Override
    public Booking getBookingDetailsById(Long id) {
        // TODO Auto-generated method stub
        Booking booking=bookingRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("booking not found with id: "+id));
        return booking;
    }

    @Override
    public void cancelBooking(Long id) {
        // TODO Auto-generated method stub
        Booking booking=getBookingDetailsById(id);
        Hotel hotel=booking.getHotel();
        hotel.setAvailableRooms(hotel.getAvailableRooms()+1);
        hotelRepository.save(hotel);
        bookingRepository.delete(booking);
    }

}
