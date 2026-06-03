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
import com.example.flightmanagementsystem.model.CreateFlightInstanceRequest;
import com.example.flightmanagementsystem.model.FlightInstanceResponse;
import com.example.flightmanagementsystem.service.FlightInstanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class FlightInstanceControllerTest {

    @Mock
    private FlightInstanceService flightInstanceService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        FlightMapper flightMapper = new FlightMapper();
        FlightInstanceController flightInstanceController = new FlightInstanceController(flightInstanceService, flightMapper);
        FlightControllerExceptionHandler exceptionHandler = new FlightControllerExceptionHandler(flightMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(flightInstanceController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void createFlightInstanceShouldReturnCreatedFlightInstance() throws Exception {
        when(flightInstanceService.createFlightInstance(any(CreateFlightInstanceRequest.class)))
                .thenReturn(new FlightInstanceResponse("AI101_2026-06-04 10:30", "AI101", "2026-06-04 10:30", 3));

        mockMvc.perform(post("/flight-instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightNumber": "AI101",
                                  "initialDepartureTime": "2026-06-04 10:30",
                                  "flightHours": 3
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightInstanceId", is("AI101_2026-06-04 10:30")))
                .andExpect(jsonPath("$.flightNumber", is("AI101")))
                .andExpect(jsonPath("$.initialDepartureTime", is("2026-06-04 10:30")))
                .andExpect(jsonPath("$.flightHours", is(3)));
    }

    @Test
    void createFlightInstanceShouldReturnBadRequestForValidationException() throws Exception {
        when(flightInstanceService.createFlightInstance(any(CreateFlightInstanceRequest.class)))
                .thenThrow(new ValidationException(ErrorCode.INITIAL_DEPARTURE_TIME_REQUIRED));

        mockMvc.perform(post("/flight-instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightNumber": "AI101",
                                  "initialDepartureTime": "",
                                  "flightHours": 3
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("initialDepartureTime is required")));
    }

    @Test
    void createFlightInstanceShouldReturnNotFoundForMissingFlightNumber() throws Exception {
        when(flightInstanceService.createFlightInstance(any(CreateFlightInstanceRequest.class)))
                .thenThrow(new FlightManagementException(ErrorCode.FLIGHT_NUMBER_NOT_FOUND));

        mockMvc.perform(post("/flight-instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightNumber": "AI101",
                                  "initialDepartureTime": "2026-06-04 10:30",
                                  "flightHours": 3
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("flightNumber does not exist")));
    }

    @Test
    void createFlightInstanceShouldReturnConflictForDuplicateFlightInstance() throws Exception {
        when(flightInstanceService.createFlightInstance(any(CreateFlightInstanceRequest.class)))
                .thenThrow(new FlightManagementException(ErrorCode.FLIGHT_INSTANCE_ALREADY_EXISTS));

        mockMvc.perform(post("/flight-instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightNumber": "AI101",
                                  "initialDepartureTime": "2026-06-04 10:30",
                                  "flightHours": 3
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.message", is("flightInstance already exists")));
    }
}
