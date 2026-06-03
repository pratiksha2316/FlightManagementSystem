package com.example.flightmanagementsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.flightmanagementsystem.entity.Booking;
import com.example.flightmanagementsystem.repository.impl.InMemoryBookingRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryBookingRepositoryTest {

    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository = new InMemoryBookingRepository();
    }

    @Test
    void trySaveBookingShouldSaveBookingWhenSeatsAreAvailable() {
        Booking booking = new Booking("booking-1", "AI101_2026-06-04 10:30", 2, List.of("Alice", "Bob"));

        boolean saved = bookingRepository.trySaveBooking(booking, 3);

        Optional<Booking> savedBooking = bookingRepository.findByBookingId("booking-1");
        assertTrue(saved);
        assertTrue(savedBooking.isPresent());
        assertEquals(booking, savedBooking.get());
        assertEquals(2, bookingRepository.getBookedSeats("AI101_2026-06-04 10:30"));
    }

    @Test
    void trySaveBookingShouldRejectBookingWhenSeatsAreNotAvailable() {
        Booking existingBooking = new Booking("booking-1", "AI101_2026-06-04 10:30", 2, List.of("Alice", "Bob"));
        Booking overbooking = new Booking("booking-2", "AI101_2026-06-04 10:30", 2, List.of("Charlie", "Dave"));

        boolean firstSaved = bookingRepository.trySaveBooking(existingBooking, 3);
        boolean secondSaved = bookingRepository.trySaveBooking(overbooking, 3);

        assertTrue(firstSaved);
        assertFalse(secondSaved);
        assertTrue(bookingRepository.findByBookingId("booking-2").isEmpty());
        assertEquals(2, bookingRepository.getBookedSeats("AI101_2026-06-04 10:30"));
    }

    @Test
    void getBookedSeatsShouldReturnZeroWhenNoBookingExists() {
        assertEquals(0, bookingRepository.getBookedSeats("UNKNOWN"));
    }

    @Test
    void trySaveBookingShouldHandleConcurrentRequestsWithoutOverbooking() throws Exception {
        int totalSeats = 10;
        int requestCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(requestCount);
        CountDownLatch readyLatch = new CountDownLatch(requestCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger successfulBookings = new AtomicInteger(0);

        for (int index = 0; index < requestCount; index++) {
            int bookingNumber = index;
            executorService.submit(() -> {
                readyLatch.countDown();
                startLatch.await();
                Booking booking = new Booking(
                        "booking-" + bookingNumber,
                        "AI101_2026-06-04 10:30",
                        1,
                        List.of("Passenger " + bookingNumber)
                );
                if (bookingRepository.trySaveBooking(booking, totalSeats)) {
                    successfulBookings.incrementAndGet();
                }
                return null;
            });
        }

        assertTrue(readyLatch.await(5, TimeUnit.SECONDS));
        startLatch.countDown();
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        assertEquals(totalSeats, successfulBookings.get());
        assertEquals(totalSeats, bookingRepository.getBookedSeats("AI101_2026-06-04 10:30"));
    }
}
