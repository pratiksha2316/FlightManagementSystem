package com.example.flightmanagementsystem.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.flightmanagementsystem.dto.BookingResponseDto;
import com.example.flightmanagementsystem.dto.CreateBookingRequestDto;
import com.example.flightmanagementsystem.dto.CreateFlightInstanceRequestDto;
import com.example.flightmanagementsystem.dto.CreateFlightRequestDto;
import com.example.flightmanagementsystem.dto.ErrorResponseDto;
import com.example.flightmanagementsystem.dto.FlightInstanceResponseDto;
import com.example.flightmanagementsystem.dto.FlightResponseDto;
import com.example.flightmanagementsystem.exception.ErrorCode;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.model.BookingResponse;
import com.example.flightmanagementsystem.model.CreateBookingRequest;
import com.example.flightmanagementsystem.model.CreateFlightInstanceRequest;
import com.example.flightmanagementsystem.model.CreateFlightRequest;
import com.example.flightmanagementsystem.model.FlightInstanceResponse;
import com.example.flightmanagementsystem.model.FlightResponse;
import java.util.List;
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
    void toServiceRequestShouldMapFlightInstanceDtoToServiceRequest() {
        CreateFlightInstanceRequest serviceRequest = flightMapper.toServiceRequest(
                new CreateFlightInstanceRequestDto("AI101", "2026-06-04 10:30", 3)
        );

        assertEquals("AI101", serviceRequest.flightNumber());
        assertEquals("2026-06-04 10:30", serviceRequest.initialDepartureTime());
        assertEquals(3, serviceRequest.flightHours());
    }

    @Test
    void toServiceRequestShouldMapBookingDtoToServiceRequest() {
        CreateBookingRequest serviceRequest = flightMapper.toServiceRequest(
                new CreateBookingRequestDto("AI101_2026-06-04 10:30", 2, List.of("Alice", "Bob"))
        );

        assertEquals("AI101_2026-06-04 10:30", serviceRequest.flightInstanceId());
        assertEquals(2, serviceRequest.numberOfSeats());
        assertEquals(List.of("Alice", "Bob"), serviceRequest.passengerNames());
    }

    @Test
    void toServiceRequestShouldReturnNullForNullDto() {
        assertNull(flightMapper.toServiceRequest((CreateFlightRequestDto) null));
        assertNull(flightMapper.toServiceRequest((CreateFlightInstanceRequestDto) null));
        assertNull(flightMapper.toServiceRequest((CreateBookingRequestDto) null));
    }

    @Test
    void toResponseDtoShouldMapServiceResponseToControllerDto() {
        FlightResponseDto responseDto = flightMapper.toResponseDto(new FlightResponse("AI101", 180));

        assertEquals("AI101", responseDto.flightNumber());
        assertEquals(180, responseDto.totalSeats());
    }

    @Test
    void toResponseDtoShouldMapFlightInstanceServiceResponseToControllerDto() {
        FlightInstanceResponseDto responseDto = flightMapper.toResponseDto(
                new FlightInstanceResponse("AI101_2026-06-04 10:30", "AI101", "2026-06-04 10:30", 3)
        );

        assertEquals("AI101_2026-06-04 10:30", responseDto.flightInstanceId());
        assertEquals("AI101", responseDto.flightNumber());
        assertEquals("2026-06-04 10:30", responseDto.initialDepartureTime());
        assertEquals(3, responseDto.flightHours());
    }

    @Test
    void toResponseDtoShouldMapBookingServiceResponseToControllerDto() {
        BookingResponseDto responseDto = flightMapper.toResponseDto(
                new BookingResponse("booking-1", "AI101_2026-06-04 10:30", 2, List.of("Alice", "Bob"))
        );

        assertEquals("booking-1", responseDto.bookingId());
        assertEquals("AI101_2026-06-04 10:30", responseDto.flightInstanceId());
        assertEquals(2, responseDto.numberOfSeats());
        assertEquals(List.of("Alice", "Bob"), responseDto.passengerNames());
    }

    @Test
    void toErrorResponseShouldMapExceptionMessage() {
        ErrorResponseDto errorResponse = flightMapper.toErrorResponse(new ValidationException(ErrorCode.FLIGHT_NUMBER_REQUIRED));

        assertEquals(400, errorResponse.code());
        assertEquals("flightNumber is required", errorResponse.message());
    }
}
