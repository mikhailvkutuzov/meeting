package com.nordclan.meeting;

import com.nordclan.meeting.entities.BookingEvent;
import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.*;
import com.nordclan.meeting.services.jpa.JpaBookingUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class MeetingApplicationTests {

    @Autowired
    private JpaBookingUserRepository userRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CalendarService calendarService;

    @Test
    @Order(0)
    void saveUsers() {
        BookingUser user1 = new BookingUser();
        user1.setName("name1");
        user1.setPassword("password1");
        user1.setId(1L);

        userRepository.save(user1);

        BookingUser user2 = new BookingUser();
        user2.setName("name2");
        user2.setPassword("password2");
        user2.setId(2L);

        userRepository.save(user2);

        BookingUser u1 = bookingService.authorize("name1", "password1");
        BookingUser u2 = bookingService.authorize("name2", "password2");

        Assertions.assertEquals(user1.getName(), u1.getName());

        Assertions.assertEquals(user2.getName(), u2.getName());


    }

    @Test
    @Order(1)
    void makeBooking() throws InvalidTimeUnit, OverlappingTimeInterval, InvalidTimeRange {
        BookingUser user1 = bookingService.authorize("name1", "password1");

        LocalDate thisDate = LocalDate.now();

        List<LocalTime> intervals = calendarService.timeIntervals();

        LocalTime thisTime = LocalTime.now();

        LocalDate validThisDate = null;
        LocalTime validThisTime = null;
        LocalTime nextTime = null;
        LocalDate nextDate = null;

        for(int i = 0; i < intervals.size(); i++) {
            if(thisTime.isBefore(intervals.get(i))) {
                validThisTime = intervals.get(i);
                validThisDate = thisDate;
                if( i == intervals.size() - 1) {
                    nextTime = intervals.get(0);
                    nextDate = validThisDate.plusDays(1);
                } else {
                    nextTime = intervals.get(i + 1);
                    nextDate = validThisDate;
                }
            }
        }
        if(validThisDate == null) {
            validThisDate = thisDate.plusDays(1);
            validThisTime = intervals.get(0);

            nextDate = validThisDate;
            nextTime = intervals.get(1);
        }

        BookingEvent event = bookingService.book(user1, validThisDate, validThisTime, nextDate, nextTime);

        LocalDate validThisDate1 = validThisDate;
        LocalTime validThisTime1 = validThisTime;
        LocalTime nextTime1 = nextTime;
        LocalDate nextDate1 = nextDate;

        Assertions.assertThrows(OverlappingTimeInterval.class, () -> bookingService.book(user1, validThisDate1, validThisTime1, nextDate1, nextTime1));

    }

}
