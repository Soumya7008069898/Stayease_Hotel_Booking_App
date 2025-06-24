package com.takehome.stayease.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.takehome.stayease.DTO.RequestDto.CreateHotelDto;
import com.takehome.stayease.DTO.RequestDto.UpdateHotelDto;
import com.takehome.stayease.DTO.ResponseDto.HotelResponseDto;
import com.takehome.stayease.DTO.ResponseDto.HotelResponsePublicDto;
import com.takehome.stayease.Entity.Hotel;
import com.takehome.stayease.Mapper.HotelMapper;
import com.takehome.stayease.Mapper.HotelPublicMapper;
import com.takehome.stayease.Service.HotelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    
    @Autowired
    private HotelService hotelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HotelResponseDto> createHotel(@Valid @RequestBody CreateHotelDto request){
        Hotel hotel=hotelService.createHotel(request);
        HotelResponseDto response=HotelMapper.convertToDto(hotel);
       // return new ResponseEntity<>(response,HttpStatus.CREATED);
       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //public
    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<HotelResponsePublicDto>> getAllHotel(){
        List<Hotel> hotels=hotelService.getAllHotels();
        List<HotelResponsePublicDto> responses=hotels.stream()
                                                .map(r->HotelPublicMapper.convertToDto(r))
                                                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    @PutMapping("/{hotel_Id}")
    public ResponseEntity<Map<String,Object>> updateHotelData(@PathVariable("hotel_Id") Long hotel_Id, @Valid @RequestBody UpdateHotelDto request){
        Map<String,Object> response=hotelService.updateHotel(hotel_Id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{hotel_Id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("hotel_Id") Long hotel_Id){
        hotelService.deleteHotel(hotel_Id);
        return ResponseEntity.noContent().build();
    }
    
}
