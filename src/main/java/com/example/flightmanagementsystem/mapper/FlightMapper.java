package com.example.flightmanagementsystem.mapper;

import com.example.flightmanagementsystem.dto.CreateFlightRequestDto;
import com.example.flightmanagementsystem.dto.ErrorResponseDto;
import com.example.flightmanagementsystem.dto.FlightResponseDto;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.model.CreateFlightRequest;
import com.example.flightmanagementsystem.model.FlightResponse;
import org.springframework.stereotype.Component;

@Component
public class FlightMapper {

    public CreateFlightRequest toServiceRequest(CreateFlightRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        return new CreateFlightRequest(requestDto.flightNumber(), requestDto.totalSeats());
    }

    public FlightResponseDto toResponseDto(FlightResponse response) {
        return new FlightResponseDto(response.flightNumber(), response.totalSeats());
    }

    public ErrorResponseDto toErrorResponse(FlightManagementException exception) {
        return new ErrorResponseDto(exception.getErrorCode().code(), exception.getErrorCode().message());
    }
}
