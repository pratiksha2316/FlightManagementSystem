# Flight Management System

This project is a Spring Boot based flight booking system. The initial goal is to build the backend APIs incrementally, starting with a simple health/status endpoint and then adding flight creation, flight instance creation, and booking functionality.

## Quick Test Flow

Start the application:

```bash
./gradlew bootRun
```

Check the app status:

```bash
curl -i http://localhost:8080/status
```

Create one flight:

```bash
curl -i -X POST http://localhost:8080/flights \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "AI101",
    "totalSeats": 180
  }'
```

Create one flight instance:

```bash
curl -i -X POST http://localhost:8080/flight-instances \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "AI101",
    "initialDepartureTime": "2026-06-04 10:30",
    "flightHours": 3
  }'
```

Expected `flightInstanceId`:

```text
AI101_2026-06-04 10:30
```

Create one booking:

```bash
curl -i -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightInstanceId": "AI101_2026-06-04 10:30",
    "numberOfSeats": 2,
    "passengerNames": ["Alice", "Bob"]
  }'
```

## Service Scope

The system will support three core functional requirements:

1. Admin creates a flight by providing `flightNumber` and `totalSeats`.
2. Admin creates a flight instance by providing `flightNumber`, `initialDepartureTime`, and `flightHours`.
3. User books seats by providing `flightInstanceId`, `numberOfSeats`, and passenger names.

Authentication and authorization are intentionally out of scope. Admin-only APIs are assumed to be used only by admins.

## Functional Requirements

### 1. Create Flight

An admin user can create a flight with:

- `flightNumber`
- `totalSeats`

Validation:

- Required fields must not be null or empty.
- `totalSeats` must be a positive number.
- Duplicate `flightNumber` is not allowed.

### 2. Create Flight Instance

An admin user can create a flight instance with:

- `flightNumber`
- `initialDepartureTime`
- `flightHours`

The system generates:

- `flightInstanceId` as a string concatenation of `flightNumber`, `_`, and `initialDepartureTime`.

Validation:

- Required fields must not be null or empty.
- `flightHours` must be a positive number.
- `flightNumber` must already exist.
- Duplicate `flightNumber` and `initialDepartureTime` combination is not allowed.
- No date format validation is required. The provided departure time is assumed to be valid and human readable.

### 3. Book Flight

A user can book a flight by directly providing:

- `flightInstanceId`
- `numberOfSeats`
- `passengerNames`

Validation:

- Required fields must not be null or empty.
- `numberOfSeats` must be a positive number.
- `flightInstanceId` must exist.
- `numberOfSeats` must match the size of `passengerNames`.
- Booking must not exceed the total seats available for the flight.

## Non-Goals

- No authentication or authorization.
- No rate limiting.
- No flight search.
- No source or destination logic.
- No APIs to retrieve bookings.
- No frontend.
- No database.
- No distributed systems concerns.

## Technical Guidelines

- Use Java and Spring Boot.
- Use REST APIs with appropriate HTTP methods and status codes.
- Use in-memory storage only.
- Implement one functional requirement at a time.
- Follow layered architecture.
- Keep services dependent on repository interfaces, not concrete implementations.
- Follow SOLID principles and clean code practices.
- Keep code easy to extend for future requirements.
- Keep code easy to unit test.
- Add unit tests for each functional area.
- Ensure thread safety, especially for booking operations.

## Planned Architecture

The project will follow a layered structure:

- `controller`: REST API endpoints and request/response handling.
- `service`: Business logic and validation orchestration.
- `repository`: Storage abstraction interfaces.
- `repository.impl`: In-memory repository implementations.
- `entity` or `model`: Domain objects such as `Flight`, `FlightInstance`, and `Booking`.
- `dto`: API request and response objects.
- `exception`: Custom exceptions and global exception handling.

High-level service classes should access repositories through interfaces. This keeps the code extensible if in-memory storage is later replaced with a database.

## Planned APIs

### Status

```http
GET /status
```

Expected response:

```text
200 OK
Ok
```

### Create Flight

```http
POST /flights
```

Expected successful status:

```text
201 Created
```

The response should return the full created flight resource.

### Create Flight Instance

```http
POST /flight-instances
```

Expected successful status:

```text
201 Created
```

The response should return the full created flight instance resource, including the generated `flightInstanceId`.

### Book Flight

```http
POST /bookings
```

Expected successful status:

```text
201 Created
```

The response should return the full created booking resource.

## Improvements

- Add integration tests that run the full API flow: create flight, create flight instance, then booking.
- Add repository unit tests for `FlightInstanceRepository`; currently only flight and booking repositories have focused tests.
- Split `FlightMapper` into separate mappers: `FlightMapper`, `FlightInstanceMapper`, and `BookingMapper`.
- Rename `FlightControllerExceptionHandler` to a generic name like `GlobalExceptionHandler`.
- Add a fallback handler for unexpected exceptions returning `500 INTERNAL_ERROR`.
- Use `HttpStatus` in `ErrorCode` instead of raw integer codes.
- Add validation for malformed JSON and missing request body.
- Add controller tests that verify the service receives correctly mapped request objects.
- Add concurrency tests at service level, not only repository level.
- Consider making `BookingRepository.trySaveBooking` return a richer result instead of boolean.
- Add package-level naming consistency: current `dto` is controller DTO, `model` is service model, but README should explicitly document that.
- Add negative API tests for all validation cases through MockMvc.
- Add logs and metrics.

## Error Handling Plan

The system should separate validation errors from non-validation errors.

Planned exception categories:

- Validation exceptions for invalid or missing request data.
- Not found exceptions for missing flights or flight instances.
- Duplicate resource exceptions for duplicate flights or flight instances.
- Booking capacity exceptions for overbooking attempts.
- Internal application exceptions for unexpected failures.

Checked exceptions should be caught where appropriate and wrapped in custom unchecked application exceptions.

## Thread Safety Plan

The booking flow must be safe under concurrent requests. Since storage is in-memory and the application is single-instance, the implementation can use Java concurrency primitives such as:

- `ConcurrentHashMap` for repository storage.
- Per-flight-instance locking or synchronized update logic for seat booking.
- Atomic validation and update of booked seat count.

The important invariant is:

```text
alreadyBookedSeats + requestedSeats <= totalSeats
```

This check and the booked-seat update must happen atomically.

## Implementation Sequence

1. Keep the existing `/status` API as the baseline setup check.
2. Implement flight creation with entity, repository, service, controller, exceptions, and unit tests.
3. Implement flight instance creation with entity, repository, service, controller, exceptions, and unit tests.
4. Implement booking with entity, repository, service, controller, concurrency handling, exceptions, and unit tests.
5. Iterate on API design or validation behavior after each requirement is tested.

## Confirmed Design Decisions

- `totalSeats`, `flightHours`, and `numberOfSeats` must be positive numbers.
- `flightInstanceId` will use `_` as the separator between `flightNumber` and `initialDepartureTime`.
- Successful create APIs should return full created resources.
