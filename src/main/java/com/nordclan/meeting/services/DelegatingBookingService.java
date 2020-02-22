package com.nordclan.meeting.services;

import com.nordclan.meeting.entities.BookingEvent;
import com.nordclan.meeting.entities.BookingUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class DelegatingBookingService implements BookingService {
    private BookingService service;

    public DelegatingBookingService(BookingService service) {
        this.service = service;
    }

    @Override
    public BookingEvent book(BookingUser user, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo) throws InvalidTimeUnit, OverlappingTimeInterval, InvalidTimeRange {
        return service.book(user, dateFrom, timeFrom, dateTo, timeTo);
    }

    @Override
    public void addParticipants(BookingEvent event, BookingUser actor, List<BookingUser> users) throws NotEnoughRights {
        service.addParticipants(event, actor, users);
    }

    @Override
    public void removeParticipants(BookingEvent event, BookingUser actor, List<BookingUser> users) throws NotEnoughRights {
        service.removeParticipants(event, actor, users);
    }

    @Override
    public int revoke(BookingEvent event, BookingUser user, LocalDateTime now) throws NotEnoughRights {
        return service.revoke(event, user, now);
    }

    @Override
    public Map<LocalDate, List<BookingEvent>> events(LocalDate from, LocalDate to) throws InvalidTimeRange {
        return service.events(from, to);
    }

}
