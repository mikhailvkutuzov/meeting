package com.nordclan.meeting.entities;

import java.util.List;

public class BookingUser {
    private Long id;
    private String name;
    private String password;
    private List<BookingEvent> events;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BookingEvent> getEvents() {
        return events;
    }

    public void setEvents(List<BookingEvent> events) {
        this.events = events;
    }
}
