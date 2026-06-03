package com.example.flightmanagementsystem.repository.impl;

import com.example.flightmanagementsystem.entity.Booking;
import com.example.flightmanagementsystem.repository.BookingRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookingRepository implements BookingRepository {

    private final ConcurrentMap<String, Booking> bookingsById = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AtomicInteger> bookedSeatsByFlightInstanceId = new ConcurrentHashMap<>();

    @Override
    public boolean trySaveBooking(Booking booking, int totalSeats) {
        AtomicInteger bookedSeats = bookedSeatsByFlightInstanceId.computeIfAbsent(
                booking.flightInstanceId(),
                ignored -> new AtomicInteger(0)
        );

        while (true) {
            int currentBookedSeats = bookedSeats.get();
            int updatedBookedSeats = currentBookedSeats + booking.numberOfSeats();
            if (updatedBookedSeats > totalSeats) {
                return false;
            }
            if (bookedSeats.compareAndSet(currentBookedSeats, updatedBookedSeats)) {
                bookingsById.put(booking.bookingId(), booking);
                return true;
            }
        }
    }

    @Override
    public Optional<Booking> findByBookingId(String bookingId) {
        return Optional.ofNullable(bookingsById.get(bookingId));
    }

    @Override
    public int getBookedSeats(String flightInstanceId) {
        return bookedSeatsByFlightInstanceId
                .getOrDefault(flightInstanceId, new AtomicInteger(0))
                .get();
    }
}
