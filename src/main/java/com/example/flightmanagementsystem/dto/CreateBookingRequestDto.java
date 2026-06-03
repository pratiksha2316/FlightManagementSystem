package com.example.flightmanagementsystem.dto;

import java.util.List;

public record CreateBookingRequestDto(
        String flightInstanceId,
        Integer numberOfSeats,
        List<String> passengerNames
) {
}
