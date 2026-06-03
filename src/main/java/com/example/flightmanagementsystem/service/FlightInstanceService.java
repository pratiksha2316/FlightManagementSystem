package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.CreateFlightInstanceRequest;
import com.example.flightmanagementsystem.model.FlightInstanceResponse;

public interface FlightInstanceService {

    FlightInstanceResponse createFlightInstance(CreateFlightInstanceRequest request);
}
