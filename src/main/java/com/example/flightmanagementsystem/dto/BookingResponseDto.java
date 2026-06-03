package com.example.flightmanagementsystem.dto;

import java.util.List;

public record BookingResponseDto(
        String bookingId,
        String flightInstanceId,
        int numberOfSeats,
        List<String> passengerNames
) {
}
