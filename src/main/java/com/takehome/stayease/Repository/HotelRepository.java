package com.takehome.stayease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.takehome.stayease.Entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long>{
    
}
