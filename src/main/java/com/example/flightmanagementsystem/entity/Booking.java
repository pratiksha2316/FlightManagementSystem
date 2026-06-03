package com.example.flightmanagementsystem.entity;

import java.util.List;
import java.util.UUID;

public record Booking(
        String bookingId,
        String flightInstanceId,
        int numberOfSeats,
        List<String> passengerNames
) {

    public Booking {
        passengerNames = List.copyOf(passengerNames);
    }

    public static Booking create(String flightInstanceId, int numberOfSeats, List<String> passengerNames) {
        return new Booking(UUID.randomUUID().toString(), flightInstanceId, numberOfSeats, passengerNames);
    }
}
