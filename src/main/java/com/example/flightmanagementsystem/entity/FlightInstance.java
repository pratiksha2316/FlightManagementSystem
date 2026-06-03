package com.example.flightmanagementsystem.entity;

public record FlightInstance(
        String flightInstanceId,
        String flightNumber,
        String initialDepartureTime,
        int flightHours
) {

    private static final String ID_SEPARATOR = "_";

    public static FlightInstance create(String flightNumber, String initialDepartureTime, int flightHours) {
        return new FlightInstance(
                generateFlightInstanceId(flightNumber, initialDepartureTime),
                flightNumber,
                initialDepartureTime,
                flightHours
        );
    }

    public static String generateFlightInstanceId(String flightNumber, String initialDepartureTime) {
        return flightNumber + ID_SEPARATOR + initialDepartureTime;
    }
}
