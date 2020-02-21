package com.nordclan.meeting.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class BookingUser {
    @Id
    private Long id;
    @Column(unique = true)
    private String name;
    private String password;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
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
