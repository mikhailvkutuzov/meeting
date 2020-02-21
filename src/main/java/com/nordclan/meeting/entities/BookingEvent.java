package com.nordclan.meeting.entities;

import java.time.LocalDateTime;

public class BookingEvent {
    private Long id;
    private LocalDateTime from;
    private LocalDateTime to;
    private BookingUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public BookingUser getUser() {
        return user;
    }

    public void setUser(BookingUser user) {
        this.user = user;
    }

}
