package com.nordclan.meeting.services.reactive;

import com.nordclan.meeting.entities.BookingEvent;
import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReactiveSingleThreadDelegatingBookingService implements ReactiveBookingService {
    private BookingService bookingService;
    private CalendarService calendarService;
    private ExecutorService executor;

    public ReactiveSingleThreadDelegatingBookingService(BookingService bookingService, CalendarService calendarService) {
        this.bookingService = bookingService;
        this.calendarService = calendarService;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public CompletableFuture<Map<LocalDate, List<BookingEvent>>> book(BookingUser user, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                bookingService.book(user, dateFrom, timeFrom, dateTo, timeTo);
                var week = calendarService.currentRange(dateFrom);
                return bookingService.events(week.get(0), week.get(1));
            } catch (InvalidTimeUnit | OverlappingTimeInterval | InvalidTimeRange e) {
                throw new ReactiveBookingException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Map<LocalDate, List<BookingEvent>>> addParticipants(BookingEvent event, BookingUser actor, List<BookingUser> users) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                bookingService.addParticipants(event, actor, users);
                var week = calendarService.currentRange(event.getFromDate().toLocalDate());
                return bookingService.events(week.get(0), week.get(1));
            } catch (NotEnoughRights | InvalidTimeRange e) {
                throw new ReactiveBookingException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Map<LocalDate, List<BookingEvent>>> removeParticipants(BookingEvent event, BookingUser actor, List<BookingUser> users) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                bookingService.removeParticipants(event, actor, users);
                var week = calendarService.currentRange(event.getFromDate().toLocalDate());
                return bookingService.events(week.get(0), week.get(1));
            } catch (NotEnoughRights | InvalidTimeRange e) {
                throw new ReactiveBookingException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Map<LocalDate, List<BookingEvent>>> revoke(BookingEvent event, BookingUser user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                bookingService.revoke(event, user, LocalDateTime.now());
                var week = calendarService.currentRange(event.getFromDate().toLocalDate());
                return bookingService.events(week.get(0), week.get(1));
            } catch (NotEnoughRights | InvalidTimeRange e) {
                throw new ReactiveBookingException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Map<LocalDate, List<BookingEvent>>> eventsForRange(LocalDate from, LocalDate to) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return bookingService.events(from, to);
            } catch (InvalidTimeRange e) {
                throw new ReactiveBookingException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Map<LocalDate, List<BookingEvent>>> eventsForRange(LocalDate dateInRange) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<LocalDate> range = calendarService.currentRange(dateInRange);
                return bookingService.events(range.get(0), range.get(range.size() - 1));
            } catch (InvalidTimeRange e) {
                throw new ReactiveBookingException(e);
            }
        }, executor);
    }
}
