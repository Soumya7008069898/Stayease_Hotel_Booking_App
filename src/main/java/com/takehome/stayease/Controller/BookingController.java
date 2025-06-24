package com.takehome.stayease.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.takehome.stayease.DTO.RequestDto.CreateBookingDto;
import com.takehome.stayease.DTO.ResponseDto.BookingResponseDto;
import com.takehome.stayease.Entity.Booking;
import com.takehome.stayease.Mapper.BookingMapper;
import com.takehome.stayease.Service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{hotel_Id}")
    public ResponseEntity<BookingResponseDto> createBooking(@PathVariable("hotel_Id") Long hotel_Id,
     @Valid @RequestBody CreateBookingDto request, Principal principal){
        Booking booking=bookingService.createBooking(hotel_Id, request, principal);
        BookingResponseDto response=BookingMapper.convertToDto(booking);
        //return new ResponseEntity<>(response,HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{booking_Id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable("booking_Id") Long booking_Id){
        Booking booking=bookingService.getBookingDetailsById(booking_Id);
        BookingResponseDto response=BookingMapper.convertToDto(booking);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    @DeleteMapping("/{booking_Id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable("booking_Id") Long booking_Id){
        bookingService.cancelBooking(booking_Id);
        return ResponseEntity.noContent().build();
    }
}
