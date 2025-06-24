package com.takehome.stayease.Exception;

public class AvailableRoomsExceededException extends RuntimeException {
    public AvailableRoomsExceededException(String message){
        super(message);
    }
}
