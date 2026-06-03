package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.dto.ErrorResponseDto;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.mapper.FlightMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FlightControllerExceptionHandler {

    private final FlightMapper flightMapper;

    public FlightControllerExceptionHandler(FlightMapper flightMapper) {
        this.flightMapper = flightMapper;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(flightMapper.toErrorResponse(exception));
    }

    @ExceptionHandler(FlightManagementException.class)
    public ResponseEntity<ErrorResponseDto> handleFlightManagementException(FlightManagementException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(flightMapper.toErrorResponse(exception));
    }
}
