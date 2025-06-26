package com.takehome.stayease.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.takehome.stayease.Repository.UserRepository;
//userservice which is used to laod the username after checking from repository for login
@Service
public class UserServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        return userRepository.findByEmail(username).orElseThrow(()-> new RuntimeException("user not found with email: "+username));
    }

}
