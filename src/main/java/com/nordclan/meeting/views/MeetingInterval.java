package com.nordclan.meeting.views;

import com.nordclan.meeting.entities.BookingEvent;

import java.util.List;

public abstract class MeetingInterval extends Interval  {
    private BookingEvent event;
    private String description;
    private List<String> participants;

    public MeetingInterval(int number, String description, List<String> participants, BookingEvent event) {
        super(number);
        this.description = description;
        this.participants = participants;
        this.event = event;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public BookingEvent getEvent() {
        return event;
    }
}
