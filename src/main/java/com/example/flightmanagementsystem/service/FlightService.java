package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.CreateFlightRequest;
import com.example.flightmanagementsystem.model.FlightResponse;

public interface FlightService {

    FlightResponse createFlight(CreateFlightRequest request);
}
