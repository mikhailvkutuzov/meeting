package com.nordclan.meeting;

import com.nordclan.meeting.services.CalendarService;
import com.nordclan.meeting.services.InvalidTimeUnit;
import com.nordclan.meeting.services.RussianWeekCalendarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class WeekCalendarTest {

    @Test
    public void intervals30() throws InvalidTimeUnit {
        CalendarService service = new RussianWeekCalendarService(30);

        List<LocalTime> intervals = service.timeIntervals();

        Assertions.assertEquals(24*2,  intervals.size());
    }

    @Test
    public void intervals13() {
        Assertions.assertThrows(InvalidTimeUnit.class, () -> new RussianWeekCalendarService(13));
    }

    @Test
    public void current() throws InvalidTimeUnit {
        CalendarService service = new RussianWeekCalendarService(30);

        LocalDate friday = LocalDate.of(2020, 02, 21);
        Assertions.assertEquals(DayOfWeek.FRIDAY, friday.getDayOfWeek());

        List<LocalDate> days = service.currentRange(friday);

        Assertions.assertEquals(7, days.size());
        Assertions.assertEquals(17, days.get(0).getDayOfMonth());
        Assertions.assertEquals(23, days.get(6).getDayOfMonth());
    }

    @Test
    public void next() throws InvalidTimeUnit {
        CalendarService service = new RussianWeekCalendarService(30);

        LocalDate friday = LocalDate.of(2020, 02, 21);
        Assertions.assertEquals(DayOfWeek.FRIDAY, friday.getDayOfWeek());

        List<LocalDate> days = service.nextRange(friday);

        Assertions.assertEquals(7, days.size());
        Assertions.assertEquals(24, days.get(0).getDayOfMonth());
        Assertions.assertEquals(1, days.get(6).getDayOfMonth());
    }

    @Test
    public void previous() throws InvalidTimeUnit {
        CalendarService service = new RussianWeekCalendarService(30);

        LocalDate sunday = LocalDate.of(2020, 02, 23);
        Assertions.assertEquals(DayOfWeek.SUNDAY, sunday.getDayOfWeek());

        List<LocalDate> days = service.previousRange(sunday);

        Assertions.assertEquals(7, days.size());
        Assertions.assertEquals(10, days.get(0).getDayOfMonth());
        Assertions.assertEquals(16, days.get(6).getDayOfMonth());
    }
}
