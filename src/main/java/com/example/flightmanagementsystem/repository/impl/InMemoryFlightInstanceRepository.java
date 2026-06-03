package com.example.flightmanagementsystem.repository.impl;

import com.example.flightmanagementsystem.entity.FlightInstance;
import com.example.flightmanagementsystem.repository.FlightInstanceRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFlightInstanceRepository implements FlightInstanceRepository {

    private final ConcurrentMap<String, FlightInstance> flightInstancesById = new ConcurrentHashMap<>();

    @Override
    public boolean saveIfAbsent(FlightInstance flightInstance) {
        return flightInstancesById.putIfAbsent(flightInstance.flightInstanceId(), flightInstance) == null;
    }

    @Override
    public Optional<FlightInstance> findByFlightInstanceId(String flightInstanceId) {
        return Optional.ofNullable(flightInstancesById.get(flightInstanceId));
    }

    @Override
    public boolean existsByFlightInstanceId(String flightInstanceId) {
        return flightInstancesById.containsKey(flightInstanceId);
    }
}
