package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.dto.ErrorResponseDto;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.mapper.FlightMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FlightControllerExceptionHandler {

    private final FlightMapper flightMapper;

    public FlightControllerExceptionHandler(FlightMapper flightMapper) {
        this.flightMapper = flightMapper;
    }

    @ExceptionHandler(FlightManagementException.class)
    public ResponseEntity<ErrorResponseDto> handleFlightManagementException(FlightManagementException exception) {
        return ResponseEntity.status(exception.getErrorCode().code()).body(flightMapper.toErrorResponse(exception));
    }
}
