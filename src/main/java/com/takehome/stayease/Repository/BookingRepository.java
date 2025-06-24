package com.takehome.stayease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.takehome.stayease.Entity.Booking;
import com.takehome.stayease.Entity.Hotel;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>{
    void deleteByHotel(Hotel hotel);
}
