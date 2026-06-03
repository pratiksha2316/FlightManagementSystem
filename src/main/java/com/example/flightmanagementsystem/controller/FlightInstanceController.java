package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.dto.CreateFlightInstanceRequestDto;
import com.example.flightmanagementsystem.dto.FlightInstanceResponseDto;
import com.example.flightmanagementsystem.mapper.FlightMapper;
import com.example.flightmanagementsystem.model.FlightInstanceResponse;
import com.example.flightmanagementsystem.service.FlightInstanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flight-instances")
public class FlightInstanceController {

    private final FlightInstanceService flightInstanceService;
    private final FlightMapper flightMapper;

    public FlightInstanceController(FlightInstanceService flightInstanceService, FlightMapper flightMapper) {
        this.flightInstanceService = flightInstanceService;
        this.flightMapper = flightMapper;
    }

    @PostMapping
    public ResponseEntity<FlightInstanceResponseDto> createFlightInstance(
            @RequestBody CreateFlightInstanceRequestDto requestDto
    ) {
        FlightInstanceResponse response = flightInstanceService.createFlightInstance(
                flightMapper.toServiceRequest(requestDto)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(flightMapper.toResponseDto(response));
    }
}
