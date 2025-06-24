package com.takehome.stayease.Service;

import java.security.Principal;

import com.takehome.stayease.DTO.RequestDto.CreateBookingDto;
import com.takehome.stayease.Entity.Booking;

public interface BookingService {
    Booking createBooking(Long id,CreateBookingDto request,Principal principal);
    Booking getBookingDetailsById(Long id);
    void cancelBooking(Long id);
}
