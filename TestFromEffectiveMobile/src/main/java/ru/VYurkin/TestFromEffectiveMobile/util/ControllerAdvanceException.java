package ru.VYurkin.TestFromEffectiveMobile.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvanceException {

    @ExceptionHandler(CustomException.class)
    private ResponseEntity<CustomErrorResponse> handleException(CustomException e){
        CustomErrorResponse response = new CustomErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

