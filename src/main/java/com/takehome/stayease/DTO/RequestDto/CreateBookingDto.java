package com.takehome.stayease.DTO.RequestDto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Dto for create a booking for the hotel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingDto {
    
    @Future(message = "Check-in date must be in the future")
    @NotNull(message="checkinDate can not be null")
    private LocalDate checkInDate;

    @Future(message = "Check-out date must be in the future")
    @NotNull(message="checkoutDate can not be null")
    private LocalDate checkOutDate;

}
