package com.takehome.stayease.DTO.ResponseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingResponseDto {
    
    private Long bookingId;
    private Long hotelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    
}
