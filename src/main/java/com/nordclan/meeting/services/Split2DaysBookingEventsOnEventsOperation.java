package com.nordclan.meeting.services;

import com.nordclan.meeting.entities.BookingEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Split2DaysBookingEventsOnEventsOperation extends DelegatingBookingService {
    private CalendarService calendar;

    public Split2DaysBookingEventsOnEventsOperation(BookingService service, CalendarService calendar) {
        super(service);
        this.calendar = calendar;
    }

    @Override
    public Map<LocalDate, List<BookingEvent>> events(LocalDate from, LocalDate to) throws InvalidTimeRange {
        return split(super.events(from, to));
    }

    private Map<LocalDate, List<BookingEvent>> split(Map<LocalDate, List<BookingEvent>> week) {
        BookingEvent begin = null;

        for (LocalDate day : week.keySet()) {
            var events = week.get(day);
            if (begin != null) {
                events.add(begin);
                begin = null;
            }
            var last = events.get(events.size() - 1);
            var to = last.getToDate().toLocalDate();
            var from = last.getFromDate().toLocalDate();
            if (to.isAfter(from)) {
                begin = new BookingEvent();
                begin.setFromDate(LocalDateTime.of(to, LocalTime.of(0, 0)));
                begin.setToDate(last.getToDate());
                begin.setId(last.getId());
                begin.setUser(last.getUser());
                begin.setParticipants(last.getParticipants());

                var intervals = calendar.timeIntervals();
                last.setToDate(LocalDateTime.of(from, intervals.get(intervals.size() - 1)));
            }
        }
        return week;
    }

}
