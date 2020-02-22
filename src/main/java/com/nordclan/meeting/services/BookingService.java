package com.nordclan.meeting.services;

import com.nordclan.meeting.entities.BookingEvent;
import com.nordclan.meeting.entities.BookingUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface BookingService {

    /**
     * Try to book an event for a particular user
     * @param user a user who tries to create an event
     * @param dateFrom event starting date
     * @param timeFrom event starting time
     * @param dateTo event ending date
     * @param timeTo event ending time
     * @return the created event
     * @throws InvalidTimeUnit in case: timeFrom ot timeTo represent invalid time unit
     * @throws OverlappingTimeInterval in case: there is another event in a system and its time range overlaps with a range required for the current event
     */
    BookingEvent book(BookingUser user, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo) throws InvalidTimeUnit, OverlappingTimeInterval, InvalidTimeRange;

    /**
     * Add some users into a meeting.
     * @param event represents the meeting
     * @param users a list of users to be added
     */
    void addParticipants(BookingEvent event, List<BookingUser> users);

    /**
     * Revoke an event registered by a user if it possible.
     * @param event the event to be revoked
     * @param user the user trying to revoke the event
     * @param now the time when this call has been done
     * @throws NotEnoughRights self-describing
     * @return an amount of minutes has been revoked successfully
     */
    int revoke(BookingEvent event, BookingUser user, LocalDateTime now) throws NotEnoughRights;

    /**
     * Return a map of events grouped by days and booked in an interval [from ; to].
     * @param from inclusive date
     * @param to inclusive date
     * @return map of events grouped by dates
     */
    Map<LocalDate, List<BookingEvent>> events(LocalDate from, LocalDate to) throws InvalidTimeRange;
 }
