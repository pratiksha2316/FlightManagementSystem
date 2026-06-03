package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.entity.Booking;
import java.util.Optional;

public interface BookingRepository {

    boolean trySaveBooking(Booking booking, int totalSeats);

    Optional<Booking> findByBookingId(String bookingId);

    int getBookedSeats(String flightInstanceId);
}
