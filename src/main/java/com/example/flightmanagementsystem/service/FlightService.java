package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.dto.CreateFlightRequest;
import com.example.flightmanagementsystem.dto.FlightResponse;

public interface FlightService {

    FlightResponse createFlight(CreateFlightRequest request);
}
