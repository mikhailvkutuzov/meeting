package com.nordclan.meeting.views;

import com.nordclan.meeting.entities.BookingEvent;

import java.util.List;

public class LastInterval extends MeetingInterval {

    public LastInterval(int number, String description, List<String> participants, BookingEvent event) {
        super(number, description, participants, event);
    }

    @Override
    public boolean isLast() {
        return true;
    }

    @Override
    public boolean isFirst() {
        return false;
    }
}
