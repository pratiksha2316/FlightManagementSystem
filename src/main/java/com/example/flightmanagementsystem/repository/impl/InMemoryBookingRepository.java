package com.example.flightmanagementsystem.repository.impl;

import com.example.flightmanagementsystem.entity.Booking;
import com.example.flightmanagementsystem.repository.BookingRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookingRepository implements BookingRepository {

    private final ConcurrentMap<String, Booking> bookingsById = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> bookedSeatsByFlightInstanceId = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Object> locksByFlightInstanceId = new ConcurrentHashMap<>();

    @Override
    public boolean trySaveBooking(Booking booking, int totalSeats) {
        Object lock = locksByFlightInstanceId.computeIfAbsent(
                booking.flightInstanceId(),
                ignored -> new Object()
        );

        synchronized (lock) {
            int currentBookedSeats = getBookedSeats(booking.flightInstanceId());
            int updatedBookedSeats = currentBookedSeats + booking.numberOfSeats();
            if (updatedBookedSeats > totalSeats) {
                return false;
            }

            bookedSeatsByFlightInstanceId.put(booking.flightInstanceId(), updatedBookedSeats);
            bookingsById.put(booking.bookingId(), booking);
            return true;
        }
    }

    @Override
    public Optional<Booking> findByBookingId(String bookingId) {
        return Optional.ofNullable(bookingsById.get(bookingId));
    }

    @Override
    public int getBookedSeats(String flightInstanceId) {
        return bookedSeatsByFlightInstanceId.getOrDefault(flightInstanceId, 0);
    }
}
