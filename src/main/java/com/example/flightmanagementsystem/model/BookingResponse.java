package com.example.flightmanagementsystem.model;

import java.util.List;

public record BookingResponse(
        String bookingId,
        String flightInstanceId,
        int numberOfSeats,
        List<String> passengerNames
) {
}
