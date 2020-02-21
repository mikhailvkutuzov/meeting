package com.nordclan.meeting.entities;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class BookingEvent {
    @Id
    @SequenceGenerator(name = "bookingSequence", sequenceName = "booking_sequence", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookingSequence")
    private Long id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private BookingUser user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_participant", joinColumns = {@JoinColumn(name = "booking_id")}, inverseJoinColumns = {@JoinColumn(name="user_id")})
    private List<BookingUser> participants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public BookingUser getUser() {
        return user;
    }

    public void setUser(BookingUser user) {
        this.user = user;
    }

    public List<BookingUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<BookingUser> participants) {
        this.participants = participants;
    }

}
