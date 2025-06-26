package com.takehome.stayease.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.takehome.stayease.DTO.RequestDto.CreateHotelDto;
import com.takehome.stayease.DTO.RequestDto.UpdateHotelDto;
import com.takehome.stayease.Entity.Hotel;
import com.takehome.stayease.Exception.AvailableRoomsExceededException;
import com.takehome.stayease.Exception.ResourceNotFoundException;
import com.takehome.stayease.Repository.BookingRepository;
import com.takehome.stayease.Repository.HotelRepository;
import com.takehome.stayease.Service.Implementation.HotelServiceImpl;

public class HotelServiceImplTest {
    @InjectMocks
    private HotelServiceImpl hotelService;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //method for creating hotel
    @Test
    void testCreateHotel_Success() {
        //setting up of dto
        CreateHotelDto dto = new CreateHotelDto();
        dto.setName("Test Hotel");
        dto.setLocation("City");
        dto.setDescription("A nice place");
        dto.setTotalRooms(50);
        dto.setAvailableRooms(20);

        //setting up of mock for hotel
        Hotel saved = Hotel.builder()
                .id(1L)
                .name(dto.getName())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .totalRooms(dto.getTotalRooms())
                .availableRooms(dto.getAvailableRooms())
                .build();

        when(hotelRepository.save(any(Hotel.class))).thenReturn(saved);

        //method invocation
        Hotel result=hotelService.createHotel(dto);
        //assertions
        assertNotNull(result);
        assertEquals("Test Hotel", result.getName());
        verify(hotelRepository).save(any(Hotel.class));
    }

    //test method to fetch all hotels
    @Test
    void testGetAllHotels_ReturnsList() {
        //setting up the hotels 
        List<Hotel> hotels = List.of(
                Hotel.builder().id(1L).name("H1").build(),
                Hotel.builder().id(2L).name("H2").build());

        when(hotelRepository.findAll()).thenReturn(hotels);

        //method innvocation 
        List<Hotel> result = hotelService.getAllHotels();
        //assertions
        assertEquals(2, result.size());
        verify(hotelRepository).findAll();
    }

    //test for hotel update if hotel is not present
    @Test
    void testUpdateHotel_ThrowsIfHotelNotFound() {
        when(hotelRepository.findById(100L)).thenReturn(Optional.empty());

        UpdateHotelDto dto = new UpdateHotelDto();
        assertThrows(ResourceNotFoundException.class, () -> hotelService.updateHotel(100L, dto));
    }

    //test for update hotel if available rooms exceed total rooms
    @Test
    void testUpdateHotel_ThrowsIfAvailableExceedsTotal() {
        //setting up of mock data for hotel
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Old Name")
                .totalRooms(10)
                .availableRooms(5)
                .build();

        //setting up of dto
        UpdateHotelDto dto = new UpdateHotelDto();
        dto.setAvailableRooms(15); // more than existing total

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        //assertions
        assertThrows(AvailableRoomsExceededException.class, () -> hotelService.updateHotel(1L, dto));
    }
    //test for update hotel
    @Test
    void testUpdateHotel_SuccessfulPartialUpdate() {
        //setting up mock data
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Old Name")
                .description("Old desc")
                .totalRooms(20)
                .availableRooms(10)
                .build();

        //setting up of dto
        UpdateHotelDto dto = new UpdateHotelDto();
        dto.setName("New Name");
        dto.setDescription("Updated Description");
        dto.setAvailableRooms(8); // valid

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any())).thenReturn(hotel);

        //method invocation
        Map<String, Object> result = hotelService.updateHotel(1L, dto);

        //assertions
        assertEquals("New Name", result.get("name"));
        assertEquals("Updated Description", result.get("description"));
        assertEquals(8, result.get("availableRooms"));
        verify(hotelRepository).save(hotel);
    }

    //test for successfully delete of hotel 
    @Test
    void testDeleteHotel_Success() {
        //mock data for hotel
        Hotel hotel = Hotel.builder().id(1L).name("ToDelete").build();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        //method invocation
        hotelService.deleteHotel(1L);

        //assertions
        verify(bookingRepository).deleteByHotel(hotel);
        verify(hotelRepository).delete(hotel);
    }

    //test for delete hotel if no such hotel exist
    @Test
    void testDeleteHotel_ThrowsIfNotFound() {
        when(hotelRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> hotelService.deleteHotel(404L));
    }
}
