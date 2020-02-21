package com.nordclan.meeting.services;

import com.nordclan.meeting.entities.BookingEvent;
import com.nordclan.meeting.entities.BookingUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {

    /**
     * Try to book an event for a particular user
     * @param user a user who tries to create an event
     * @param dateFrom event starting date
     * @param timeFrom event starting time
     * @param dateTo event ending date
     * @param timeTo event ending time
     * @return
     * @throws InvalidTimeUnit in case: timeFrom ot timeTo represent invalid time unit
     * @throws OverlappingTimeInterval in case: there is another event in a system and its time range overlaps with a range required for the current event
     */
    BookingEvent book(BookingUser user, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo) throws InvalidTimeUnit, OverlappingTimeInterval;

    /**
     * Revoke an event registered by a user if it possible.
     * @param event the event to be revoked
     * @param user the user trying to revoke the event
     * @throws NotEnoughRights
     * @throws CouldNotRevokeHappenedEvent
     */
    void revoke(BookingEvent event, BookingUser user) throws NotEnoughRights, CouldNotRevokeHappenedEvent;

    /**
     * Return a list of events booked in an interval  [from ; to] (inclusive)  .
     * @param from
     * @param to
     * @return
     */
    List<DayEvents> events(LocalDate from, LocalDate to);
 }
