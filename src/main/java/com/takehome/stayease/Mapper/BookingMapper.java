package com.takehome.stayease.Mapper;

import com.takehome.stayease.DTO.ResponseDto.BookingResponseDto;
import com.takehome.stayease.Entity.Booking;

public class BookingMapper {
    public static BookingResponseDto convertToDto(Booking booking){
        BookingResponseDto response=new BookingResponseDto();
        response.setBookingId(booking.getId());
        response.setHotelId(booking.getHotel().getId());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());

        return response;
    }
}
