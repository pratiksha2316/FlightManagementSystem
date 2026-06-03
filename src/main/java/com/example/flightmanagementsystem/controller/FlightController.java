package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.dto.CreateFlightRequestDto;
import com.example.flightmanagementsystem.dto.FlightResponseDto;
import com.example.flightmanagementsystem.mapper.FlightMapper;
import com.example.flightmanagementsystem.model.FlightResponse;
import com.example.flightmanagementsystem.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;
    private final FlightMapper flightMapper;

    public FlightController(FlightService flightService, FlightMapper flightMapper) {
        this.flightService = flightService;
        this.flightMapper = flightMapper;
    }

    @PostMapping
    public ResponseEntity<FlightResponseDto> createFlight(@RequestBody CreateFlightRequestDto requestDto) {
        FlightResponse response = flightService.createFlight(flightMapper.toServiceRequest(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(flightMapper.toResponseDto(response));
    }
}
