package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.BookingResponse;
import com.example.flightmanagementsystem.model.CreateBookingRequest;

public interface BookingService {

    BookingResponse createBooking(CreateBookingRequest request);
}
