package com.nordclan.meeting.services;

import com.nordclan.meeting.entities.BookingEvent;

import java.time.LocalDate;
import java.util.List;

public class DayEvents {
    private LocalDate date;
    private List<BookingEvent> events;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<BookingEvent> getEvents() {
        return events;
    }

    public void setEvents(List<BookingEvent> events) {
        this.events = events;
    }
}
