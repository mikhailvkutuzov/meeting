package com.nordclan.meeting.services;

import com.nordclan.meeting.entities.BookingEvent;

import java.util.List;

public class OverlappingTimeInterval extends BookingException {
    public List<BookingEvent> overlapping;

    public OverlappingTimeInterval(List<BookingEvent> overlapping) {
        this.overlapping = overlapping;
    }
}
