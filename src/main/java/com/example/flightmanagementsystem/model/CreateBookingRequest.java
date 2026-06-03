package com.example.flightmanagementsystem.model;

import java.util.List;

public record CreateBookingRequest(
        String flightInstanceId,
        Integer numberOfSeats,
        List<String> passengerNames
) {
}
