package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.dto.BookingResponseDto;
import com.example.flightmanagementsystem.dto.CreateBookingRequestDto;
import com.example.flightmanagementsystem.mapper.FlightMapper;
import com.example.flightmanagementsystem.model.BookingResponse;
import com.example.flightmanagementsystem.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final FlightMapper flightMapper;

    public BookingController(BookingService bookingService, FlightMapper flightMapper) {
        this.bookingService = bookingService;
        this.flightMapper = flightMapper;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody CreateBookingRequestDto requestDto) {
        BookingResponse response = bookingService.createBooking(flightMapper.toServiceRequest(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(flightMapper.toResponseDto(response));
    }
}
