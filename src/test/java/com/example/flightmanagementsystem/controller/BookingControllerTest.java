package com.example.flightmanagementsystem.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flightmanagementsystem.exception.ErrorCode;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.mapper.FlightMapper;
import com.example.flightmanagementsystem.model.BookingResponse;
import com.example.flightmanagementsystem.model.CreateBookingRequest;
import com.example.flightmanagementsystem.service.BookingService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        FlightMapper flightMapper = new FlightMapper();
        BookingController bookingController = new BookingController(bookingService, flightMapper);
        FlightControllerExceptionHandler exceptionHandler = new FlightControllerExceptionHandler(flightMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void createBookingShouldReturnCreatedBooking() throws Exception {
        when(bookingService.createBooking(any(CreateBookingRequest.class)))
                .thenReturn(new BookingResponse(
                        "booking-1",
                        "AI101_2026-06-04 10:30",
                        2,
                        List.of("Alice", "Bob")
                ));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightInstanceId": "AI101_2026-06-04 10:30",
                                  "numberOfSeats": 2,
                                  "passengerNames": ["Alice", "Bob"]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingId", is("booking-1")))
                .andExpect(jsonPath("$.flightInstanceId", is("AI101_2026-06-04 10:30")))
                .andExpect(jsonPath("$.numberOfSeats", is(2)))
                .andExpect(jsonPath("$.passengerNames[0]", is("Alice")))
                .andExpect(jsonPath("$.passengerNames[1]", is("Bob")));
    }

    @Test
    void createBookingShouldReturnBadRequestForValidationException() throws Exception {
        when(bookingService.createBooking(any(CreateBookingRequest.class)))
                .thenThrow(new ValidationException(ErrorCode.PASSENGER_COUNT_MISMATCH));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightInstanceId": "AI101_2026-06-04 10:30",
                                  "numberOfSeats": 2,
                                  "passengerNames": ["Alice"]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("numberOfSeats must match passengerNames size")));
    }

    @Test
    void createBookingShouldReturnNotFoundForMissingFlightInstance() throws Exception {
        when(bookingService.createBooking(any(CreateBookingRequest.class)))
                .thenThrow(new FlightManagementException(ErrorCode.FLIGHT_INSTANCE_NOT_FOUND));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightInstanceId": "AI101_2026-06-04 10:30",
                                  "numberOfSeats": 1,
                                  "passengerNames": ["Alice"]
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("flightInstanceId does not exist")));
    }

    @Test
    void createBookingShouldReturnConflictWhenSeatsAreNotAvailable() throws Exception {
        when(bookingService.createBooking(any(CreateBookingRequest.class)))
                .thenThrow(new FlightManagementException(ErrorCode.SEATS_NOT_AVAILABLE));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightInstanceId": "AI101_2026-06-04 10:30",
                                  "numberOfSeats": 2,
                                  "passengerNames": ["Alice", "Bob"]
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.message", is("requested seats are not available")));
    }
}
