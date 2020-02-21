package com.nordclan.meeting.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This type of {@link CalendarService} operate with a range of dates representing one "russian" week.
 * Time intervals available for this type of {@link CalendarService} should consists of amount of minutes dividing
 * 24*60 minutes whit out of any rest.
 */
public class RussianWeekCalendarService implements CalendarService {

    private List<LocalTime> timeIntervals;

    public RussianWeekCalendarService(int minutes) throws InvalidTimeUnit {
        if (24 * 60 % minutes != 0) {
            throw new InvalidTimeUnit();
        }

        timeIntervals = Stream.iterate(LocalTime.of(0, 0),
                (LocalTime time) -> time.getHour() != 23 || time.getMinute() != (60 - minutes),
                (LocalTime time) -> time.plusMinutes(minutes))
                .collect(Collectors.toList());
        timeIntervals.add(LocalTime.of(23, (60 - minutes)));
    }

    @Override
    public List<LocalDate> currentRange(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();

        LocalDate firstDay = date.minusDays(day.getValue() - 1);
        LocalDate lastDay = firstDay.plusDays(7);

        return Stream.iterate(firstDay, (LocalDate d) -> !d.equals(lastDay), d -> d.plusDays(1) ).collect(Collectors.toList());
    }

    @Override
    public List<LocalTime> timeIntervals() {
        return timeIntervals;
    }

    @Override
    public List<LocalDate> nextRange(LocalDate date) {
        return currentRange(date.plusDays(7));
    }

    @Override
    public List<LocalDate> previousRange(LocalDate date) {
        return currentRange(date.minusDays(7));
    }
}
