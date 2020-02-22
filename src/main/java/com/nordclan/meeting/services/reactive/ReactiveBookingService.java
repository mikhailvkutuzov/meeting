package com.nordclan.meeting.services.reactive;

import com.nordclan.meeting.entities.BookingEvent;
import com.nordclan.meeting.entities.BookingUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ReactiveBookingService {

    CompletableFuture<Map<LocalDate, List<BookingEvent>>> book(BookingUser user, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo);

    CompletableFuture<Map<LocalDate, List<BookingEvent>>> addParticipants(BookingEvent event, BookingUser actor, List<BookingUser> users);

    CompletableFuture<Map<LocalDate, List<BookingEvent>>> removeParticipants(BookingEvent event, BookingUser actor, List<BookingUser> users);

    CompletableFuture<Map<LocalDate, List<BookingEvent>>> revoke(BookingEvent event, BookingUser user);

    CompletableFuture<Map<LocalDate, List<BookingEvent>>> eventsForRange(LocalDate from, LocalDate to);

}
