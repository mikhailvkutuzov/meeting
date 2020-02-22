package com.nordclan.meeting.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface CalendarService {

    /**
     * Get a all the days representing a range configured in a system and containing a date.
     * @param date a date contained in a range
     * @return sorted list of days (asc)
     */
    List<LocalDate> currentRange(LocalDate date);

    /**
     * Starts of all time intervals available in a system.
     * @return sorted list of time moments (asc)
     */
    List<LocalTime> timeIntervals();

    /**
     * Get a all the days representing the next range for a date.
     * @param date a date contained in a current range
     * @return sorted list of days (asc)
     */
    List<LocalDate> nextRange(LocalDate date);

    /**
     * Get a all the days representing the previous range for a date.
     * @param date a date contained in a current range
     * @return sorted list of days (asc)
     */
    List<LocalDate> previousRange(LocalDate date);


    /**
     * Return a minimal amount of minutes containing maximum amount of available time intervals in a minutesInterval.
     * @param minutesInterval
     * @return
     */
    int getValidTimeInterval(int minutesInterval);

}
