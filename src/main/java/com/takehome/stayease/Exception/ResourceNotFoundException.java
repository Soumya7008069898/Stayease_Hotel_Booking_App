package com.takehome.stayease.Exception;
//exception is thrown when resourse is not found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
