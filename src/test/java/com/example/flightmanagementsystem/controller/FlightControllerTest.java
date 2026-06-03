package com.example.flightmanagementsystem.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.mapper.FlightMapper;
import com.example.flightmanagementsystem.model.CreateFlightRequest;
import com.example.flightmanagementsystem.model.FlightResponse;
import com.example.flightmanagementsystem.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

    @Mock
    private FlightService flightService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        FlightMapper flightMapper = new FlightMapper();
        FlightController flightController = new FlightController(flightService, flightMapper);
        FlightControllerExceptionHandler exceptionHandler = new FlightControllerExceptionHandler(flightMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(flightController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void createFlightShouldReturnCreatedFlight() throws Exception {
        when(flightService.createFlight(any(CreateFlightRequest.class)))
                .thenReturn(new FlightResponse("AI101", 180));

        mockMvc.perform(post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightNumber": "AI101",
                                  "totalSeats": 180
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightNumber", is("AI101")))
                .andExpect(jsonPath("$.totalSeats", is(180)));
    }

    @Test
    void createFlightShouldReturnBadRequestForValidationException() throws Exception {
        when(flightService.createFlight(any(CreateFlightRequest.class)))
                .thenThrow(new ValidationException("flightNumber is required"));

        mockMvc.perform(post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightNumber": "",
                                  "totalSeats": 180
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("flightNumber is required")));
    }

    @Test
    void createFlightShouldReturnConflictForFlightManagementException() throws Exception {
        when(flightService.createFlight(any(CreateFlightRequest.class)))
                .thenThrow(new FlightManagementException("flightNumber already exists"));

        mockMvc.perform(post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightNumber": "AI101",
                                  "totalSeats": 180
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("flightNumber already exists")));
    }
}
