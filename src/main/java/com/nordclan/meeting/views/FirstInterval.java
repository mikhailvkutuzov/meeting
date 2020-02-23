package com.nordclan.meeting.views;

import com.nordclan.meeting.entities.BookingEvent;

import java.util.List;

public class FirstInterval extends MeetingInterval {

    public FirstInterval(int number, String description, List<String> participants, BookingEvent event) {
        super(number, description, participants, event);
    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean isFirst() {
        return true;
    }
}
