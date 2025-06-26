package com.takehome.stayease.Exception;
//exception is thrown when available rooms exceed total rooms
public class AvailableRoomsExceededException extends RuntimeException {
    public AvailableRoomsExceededException(String message){
        super(message);
    }
}
