package com.example.flightmanagementsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.flightmanagementsystem.entity.Flight;
import com.example.flightmanagementsystem.repository.impl.InMemoryFlightRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryFlightRepositoryTest {

    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        flightRepository = new InMemoryFlightRepository();
    }

    @Test
    void saveIfAbsentShouldSaveNewFlight() {
        Flight flight = new Flight("AI101", 180);

        boolean saved = flightRepository.saveIfAbsent(flight);

        assertTrue(saved);
        assertTrue(flightRepository.existsByFlightNumber("AI101"));
    }

    @Test
    void saveIfAbsentShouldNotOverwriteExistingFlight() {
        Flight originalFlight = new Flight("AI101", 180);
        Flight duplicateFlight = new Flight("AI101", 220);

        boolean firstSave = flightRepository.saveIfAbsent(originalFlight);
        boolean secondSave = flightRepository.saveIfAbsent(duplicateFlight);

        Optional<Flight> savedFlight = flightRepository.findByFlightNumber("AI101");
        assertTrue(firstSave);
        assertFalse(secondSave);
        assertTrue(savedFlight.isPresent());
        assertEquals(originalFlight, savedFlight.get());
    }

    @Test
    void findByFlightNumberShouldReturnFlightWhenPresent() {
        Flight flight = new Flight("AI101", 180);
        flightRepository.saveIfAbsent(flight);

        Optional<Flight> savedFlight = flightRepository.findByFlightNumber("AI101");

        assertTrue(savedFlight.isPresent());
        assertEquals(flight, savedFlight.get());
    }

    @Test
    void findByFlightNumberShouldReturnEmptyWhenMissing() {
        Optional<Flight> savedFlight = flightRepository.findByFlightNumber("UNKNOWN");

        assertTrue(savedFlight.isEmpty());
    }

    @Test
    void existsByFlightNumberShouldReturnFalseWhenMissing() {
        assertFalse(flightRepository.existsByFlightNumber("UNKNOWN"));
    }
}
