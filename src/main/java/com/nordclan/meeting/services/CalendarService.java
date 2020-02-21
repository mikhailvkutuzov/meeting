package com.nordclan.meeting.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface CalendarService {

    /**
     * Get a all the days representing a range configured in a system and containing a date.
     * @param date a date contained in a range
     * @return
     */
    List<LocalDate> currentRange(LocalDate date);

    /**
     * Starts of all time intervals available in a system.
     * @return
     */
    List<LocalTime> timeIntervals();

    /**
     * Get a all the days representing the next range for a date.
     * @param date a date contained in a current range
     * @return
     */
    List<LocalDate> nextRange(LocalDate date);

    /**
     * Get a all the days representing the previous range for a date.
     * @param date a date contained in a current range
     * @return
     */
    List<LocalDate> previousRange(LocalDate date);

}
