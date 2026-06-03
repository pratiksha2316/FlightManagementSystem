package com.example.flightmanagementsystem.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.flightmanagementsystem.dto.CreateFlightRequestDto;
import com.example.flightmanagementsystem.dto.ErrorResponseDto;
import com.example.flightmanagementsystem.dto.FlightResponseDto;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.model.CreateFlightRequest;
import com.example.flightmanagementsystem.model.FlightResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlightMapperTest {

    private FlightMapper flightMapper;

    @BeforeEach
    void setUp() {
        flightMapper = new FlightMapper();
    }

    @Test
    void toServiceRequestShouldMapControllerDtoToServiceRequest() {
        CreateFlightRequest serviceRequest = flightMapper.toServiceRequest(new CreateFlightRequestDto("AI101", 180));

        assertEquals("AI101", serviceRequest.flightNumber());
        assertEquals(180, serviceRequest.totalSeats());
    }

    @Test
    void toServiceRequestShouldReturnNullForNullDto() {
        assertNull(flightMapper.toServiceRequest(null));
    }

    @Test
    void toResponseDtoShouldMapServiceResponseToControllerDto() {
        FlightResponseDto responseDto = flightMapper.toResponseDto(new FlightResponse("AI101", 180));

        assertEquals("AI101", responseDto.flightNumber());
        assertEquals(180, responseDto.totalSeats());
    }

    @Test
    void toErrorResponseShouldMapExceptionMessage() {
        ErrorResponseDto errorResponse = flightMapper.toErrorResponse(new ValidationException("flightNumber is required"));

        assertEquals("flightNumber is required", errorResponse.message());
    }
}
