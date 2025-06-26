package com.takehome.stayease.Service;

import com.takehome.stayease.DTO.RequestDto.CreateBookingDto;
import com.takehome.stayease.Entity.Booking;
import com.takehome.stayease.Entity.Hotel;
import com.takehome.stayease.Entity.User;
//import com.takehome.stayease.Exception.ResourceNotFoundException;
import com.takehome.stayease.Repository.BookingRepository;
import com.takehome.stayease.Repository.HotelRepository;
import com.takehome.stayease.Service.Implementation.BookingServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //test for creating successful booking
    @Test
    public void testCreateBooking_Success() {
        Long hotelId = 1L;
        String email = "test@example.com";

        //setting up of dto
        CreateBookingDto dto = new CreateBookingDto();
        dto.setCheckInDate(LocalDate.of(2025, 6, 25));
        dto.setCheckOutDate(LocalDate.of(2025, 6, 27));

        //setting up of mock hotel,user and booking
        Hotel hotel = Hotel.builder().id(hotelId).availableRooms(5).build();
        User user = User.builder().email(email).build();
        Booking savedBooking = Booking.builder().id(10L).hotel(hotel).user(user).build();

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(principal.getName()).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(user);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        //method invoccation
        Booking result = bookingService.createBooking(hotelId, dto, principal);

        //assertions
        assertNotNull(result);
        assertEquals(savedBooking.getId(), result.getId());
        verify(hotelRepository).save(hotel);
        verify(bookingRepository).save(any(Booking.class));
    }

    //test for failed booking if no rooms available
    @Test
    public void testCreateBooking_NoRoomsAvailable_ThrowsException() {
        Long hotelId = 1L;
        //setting up dto
        CreateBookingDto dto = new CreateBookingDto();
        dto.setCheckInDate(LocalDate.of(2025, 6, 25));
        dto.setCheckOutDate(LocalDate.of(2025, 6, 27));

        //mock of hotel
        Hotel hotel = Hotel.builder().id(hotelId).availableRooms(0).build();

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        //assertions
        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(hotelId, dto, principal);
        });

        assertEquals("No rooms available", exception.getMessage());
    }

    //test for failed booking due to invalid dates 
    @Test
    public void testCreateBooking_InvalidDates_ThrowsException() {
        Long hotelId = 1L;
        //setting up of dto
        CreateBookingDto dto = new CreateBookingDto();
        dto.setCheckInDate(LocalDate.of(2025, 6, 25));
        dto.setCheckOutDate(LocalDate.of(2025, 6, 24));

        //hotel mock data
        Hotel hotel = Hotel.builder().id(hotelId).availableRooms(2).build();

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        //assertions
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(hotelId, dto, principal);
        });

        assertEquals("check-out date must be after check-in date", exception.getMessage());
    }

    //test for get booking details
    @Test
    public void testGetBookingDetailsById_Found() {
        Long bookingId = 10L;
        Booking booking = Booking.builder().id(bookingId).build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingDetailsById(bookingId);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
    }

    //test for booking cancel
    @Test
    public void testCancelBooking_Success() {
        Long bookingId = 11L;
        //mocks of hotel and booking
        Hotel hotel = Hotel.builder().availableRooms(2).build();
        Booking booking = Booking.builder().id(bookingId).hotel(hotel).build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        //method invocation
        bookingService.cancelBooking(bookingId);

        //assertions
        assertEquals(3, hotel.getAvailableRooms());
        verify(hotelRepository).save(hotel);
        verify(bookingRepository).delete(booking);
    }
}
