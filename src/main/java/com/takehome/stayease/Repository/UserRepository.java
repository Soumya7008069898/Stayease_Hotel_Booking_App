package com.takehome.stayease.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.takehome.stayease.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    //query to check the user with email is present or not
    Optional<User> findByEmail(String email);
    //returns a boolean to that username is present or not
    boolean existsByEmail(String email);
}
