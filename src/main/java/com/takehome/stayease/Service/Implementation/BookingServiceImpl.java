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

    //used for creating booking and principal contains the username/email after login
    @Override
    public Booking createBooking(Long id,CreateBookingDto request, Principal principal) {
        // TODO Auto-generated method stub
        //check the user and hotel are present
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("hotel not found with id: "+id));
        User user=(User)userDetailsService.loadUserByUsername(principal.getName());

        //check if hotel is full
        if(hotel.getAvailableRooms()<=0){
            throw new ResourceNotFoundException("No rooms available");   //resourcenotfoundexp
        } 

        //validation for checkout date> checkin date
        if(!request.getCheckOutDate().isAfter(request.getCheckInDate())){
            throw new IllegalArgumentException("check-out date must be after check-in date");
        }

        //booking is created
        Booking booking=Booking.builder()
                        .user(user)
                        .hotel(hotel)
                        .checkInDate(request.getCheckInDate())
                        .checkOutDate(request.getCheckOutDate())
                        .build();
        //available rooms of that hotel is updated and saved
        hotel.setAvailableRooms(hotel.getAvailableRooms()-1);
        hotelRepository.save(hotel);

        //booking is saved
        return bookingRepository.save(booking);                     
    }

    //get booking details by booking id
    @Override
    public Booking getBookingDetailsById(Long id) {
        // TODO Auto-generated method stub
        Booking booking=bookingRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("booking not found with id: "+id));
        return booking;
    }

    // cancellation of booking
    @Override
    public void cancelBooking(Long id) {
        // TODO Auto-generated method stub
        //booking fetched by id
        Booking booking=getBookingDetailsById(id);
        //hotel fetched from booking
        Hotel hotel=booking.getHotel();
        //update the availablerooms and save
        hotel.setAvailableRooms(hotel.getAvailableRooms()+1);
        hotelRepository.save(hotel);
        //delete booking
        bookingRepository.delete(booking);
    }

}
