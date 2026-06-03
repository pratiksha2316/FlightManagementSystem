package com.example.flightmanagementsystem.mapper;

import com.example.flightmanagementsystem.dto.BookingResponseDto;
import com.example.flightmanagementsystem.dto.CreateBookingRequestDto;
import com.example.flightmanagementsystem.dto.CreateFlightInstanceRequestDto;
import com.example.flightmanagementsystem.dto.CreateFlightRequestDto;
import com.example.flightmanagementsystem.dto.ErrorResponseDto;
import com.example.flightmanagementsystem.dto.FlightInstanceResponseDto;
import com.example.flightmanagementsystem.dto.FlightResponseDto;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.model.BookingResponse;
import com.example.flightmanagementsystem.model.CreateBookingRequest;
import com.example.flightmanagementsystem.model.CreateFlightInstanceRequest;
import com.example.flightmanagementsystem.model.CreateFlightRequest;
import com.example.flightmanagementsystem.model.FlightInstanceResponse;
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

    public CreateFlightInstanceRequest toServiceRequest(CreateFlightInstanceRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        return new CreateFlightInstanceRequest(
                requestDto.flightNumber(),
                requestDto.initialDepartureTime(),
                requestDto.flightHours()
        );
    }

    public CreateBookingRequest toServiceRequest(CreateBookingRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        return new CreateBookingRequest(
                requestDto.flightInstanceId(),
                requestDto.numberOfSeats(),
                requestDto.passengerNames()
        );
    }

    public FlightResponseDto toResponseDto(FlightResponse response) {
        return new FlightResponseDto(response.flightNumber(), response.totalSeats());
    }

    public FlightInstanceResponseDto toResponseDto(FlightInstanceResponse response) {
        return new FlightInstanceResponseDto(
                response.flightInstanceId(),
                response.flightNumber(),
                response.initialDepartureTime(),
                response.flightHours()
        );
    }

    public BookingResponseDto toResponseDto(BookingResponse response) {
        return new BookingResponseDto(
                response.bookingId(),
                response.flightInstanceId(),
                response.numberOfSeats(),
                response.passengerNames()
        );
    }

    public ErrorResponseDto toErrorResponse(FlightManagementException exception) {
        return new ErrorResponseDto(exception.getErrorCode().code(), exception.getErrorCode().message());
    }
}
