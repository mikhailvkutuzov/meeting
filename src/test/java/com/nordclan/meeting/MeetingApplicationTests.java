package com.nordclan.meeting;

import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.*;
import com.nordclan.meeting.services.jpa.JpaBookingUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeetingApplicationTests {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JpaBookingUserRepository userRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CalendarService calendarService;

    @Test
    @Order(1)
    public void saveUsers() {
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

        BookingUser u1 = authenticationService.authorize("name1", "password1");
        BookingUser u2 = authenticationService.authorize("name2", "password2");

        Assertions.assertEquals(user1.getName(), u1.getName());

        Assertions.assertEquals(user2.getName(), u2.getName());
    }

    @Test
    @Order(2)
    public void makeBooking() throws InvalidTimeUnit, OverlappingTimeInterval, InvalidTimeRange, NotEnoughRights {
        var user1 = authenticationService.authorize("name1", "password1");

        var now = LocalDateTime.now();

        var thisDate = now.toLocalDate();

        List<LocalTime> intervals = calendarService.timeIntervals();

        var thisTime = now.toLocalTime();

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
                break;
            }
        }
        if(validThisDate == null) {
            validThisDate = thisDate.plusDays(1);
            validThisTime = intervals.get(0);

            nextDate = validThisDate;
            nextTime = intervals.get(1);
        }

        var event = bookingService.book(user1, validThisDate, validThisTime, nextDate, nextTime);

        var validThisDate1 = validThisDate;
        var validThisTime1 = validThisTime;
        var nextTime1 = nextTime;
        var nextDate1 = nextDate;

        Assertions.assertThrows(OverlappingTimeInterval.class, () -> bookingService.book(user1, validThisDate1, validThisTime1, nextDate1, nextTime1));

        int minutes = bookingService.revoke(event, user1, now);

        int interval = (int)Duration.between(event.getFromDate(), event.getToDate()).toMinutes();

        Assertions.assertEquals(interval, minutes);

        event =  bookingService.book(user1, validThisDate1, validThisTime1, nextDate1, nextTime1);

        bookingService.revoke(event, user1, now);
    }

    @Test
    @Order(3)
    public void overlappingAndInvalidRange() throws InvalidTimeUnit, OverlappingTimeInterval, InvalidTimeRange, NotEnoughRights {
        var user1 = authenticationService.authorize("name1", "password1");

        var today = LocalDateTime.now().toLocalDate();

        List<LocalTime> intervals = calendarService.timeIntervals();

        var be0 = bookingService.book(user1, today, intervals.get(1), today, intervals.get(2));
        var be1 = bookingService.book(user1, today, intervals.get(3), today, intervals.get(4));
        var be2 = bookingService.book(user1, today, intervals.get(2), today, intervals.get(3));

        Assertions.assertThrows(InvalidTimeRange.class, () -> bookingService.book(user1, today, intervals.get(7), today, intervals.get(6)));

        var be3 = bookingService.book(user1, today, intervals.get(6), today, intervals.get(7));

        Assertions.assertThrows(OverlappingTimeInterval.class, () -> bookingService.book(user1, today, intervals.get(1), today, intervals.get(3)));
        Assertions.assertThrows(OverlappingTimeInterval.class, () -> bookingService.book(user1, today, intervals.get(3), today, intervals.get(5)));
        Assertions.assertThrows(OverlappingTimeInterval.class, () -> bookingService.book(user1, today, intervals.get(1), today, intervals.get(4)));
        Assertions.assertThrows(OverlappingTimeInterval.class, () -> bookingService.book(user1, today, intervals.get(1), today, intervals.get(5)));

        Assertions.assertThrows(OverlappingTimeInterval.class, () -> bookingService.book(user1, today, intervals.get(5), today, intervals.get(7)));
        Assertions.assertThrows(OverlappingTimeInterval.class, () -> bookingService.book(user1, today, intervals.get(5), today, intervals.get(8)));

        var dayBegin = LocalDateTime.of(today, LocalTime.of(0,0));

        bookingService.revoke(be0, user1, dayBegin);
        bookingService.revoke(be1, user1, dayBegin);
        bookingService.revoke(be2, user1, dayBegin);
        bookingService.revoke(be3, user1, dayBegin);

        Assertions.assertEquals(0, bookingService.events(today, today.plusDays(1)).size());
    }

    @Test
    @Order(4)
    public void showEvents() throws InvalidTimeUnit, OverlappingTimeInterval, InvalidTimeRange, NotEnoughRights {
        var user1 = authenticationService.authorize("name1", "password1");

        var today = LocalDateTime.now().toLocalDate();

        List<LocalTime> intervals = calendarService.timeIntervals();

        var tomorrow = today.plusDays(1);

        var theDayAfterTomorrow = tomorrow.plusDays(1);

        Assertions.assertEquals(0, bookingService.events(today, tomorrow).size());

        var be0 = bookingService.book(user1, today, intervals.get(1), today, intervals.get(2));
        var be1 = bookingService.book(user1, today, intervals.get(3), today, intervals.get(4));
        var be2 = bookingService.book(user1, today, intervals.get(2), today, intervals.get(3));
        var be3 = bookingService.book(user1, today, intervals.get(6), today, intervals.get(7));

        var fourEvents = bookingService.events(today, tomorrow);

        Assertions.assertEquals(1, fourEvents.size());
        Assertions.assertEquals(4,  fourEvents.get(today).size());

        Assertions.assertEquals(0, bookingService.events(tomorrow, theDayAfterTomorrow).size());

        var dayBefore = today.minusDays(1);
        Assertions.assertEquals(0, bookingService.events(dayBefore, today).size());

        var dayBegin = LocalDateTime.of(today, LocalTime.of(0,0));

        bookingService.revoke(be0, user1, dayBegin);
        bookingService.revoke(be1, user1, dayBegin);
        bookingService.revoke(be2, user1, dayBegin);
        bookingService.revoke(be3, user1, dayBegin);

        Assertions.assertEquals(0, bookingService.events(today, today.plusDays(1)).size());
    }

    @Test
    @Order(5)
    public void meetingCouldNotLastMoreThan4Intervals() throws InvalidTimeUnit, OverlappingTimeInterval, InvalidTimeRange, NotEnoughRights {
        var user1 = authenticationService.authorize("name1", "password1");
        var today = LocalDateTime.now().toLocalDate();
        var dayBegin = LocalDateTime.of(today, LocalTime.of(0,0));
        List<LocalTime> intervals = calendarService.timeIntervals();

        Assertions.assertThrows(InvalidTimeRange.class, () -> bookingService.book(user1, today, intervals.get(6), today, intervals.get(11)));

        var event = bookingService.book(user1, today, intervals.get(6), today, intervals.get(10));
        bookingService.revoke(event, user1, dayBegin);
    }

    @Test
    @Order(6)
    public void addParticipants() throws InvalidTimeUnit, OverlappingTimeInterval, InvalidTimeRange, NotEnoughRights {
        var user1 = authenticationService.authorize("name1", "password1");
        var user2 = authenticationService.authorize("name2", "password2");
        var today = LocalDateTime.now().toLocalDate();
        var dayBegin = LocalDateTime.of(today, LocalTime.of(0,0));
        List<LocalTime> intervals = calendarService.timeIntervals();

        var be0 = bookingService.book(user1, today, intervals.get(1), today, intervals.get(2));
        List<BookingUser> participants = new ArrayList<>();
        participants.add(user2);
        bookingService.addParticipants(be0, user1, participants);

        var events = bookingService.events(today, today.plusDays(1));
        Assertions.assertEquals(1, events.size());
        Assertions.assertEquals(1, events.get(today).size());

        var be00 = events.get(today).get(0);
        var u1 = be00.getUser();
        Assertions.assertEquals("name1", u1.getName());
        Assertions.assertEquals(1, be00.getParticipants().size());

        var u2 = be00.getParticipants().get(0);
        Assertions.assertEquals("name2", u2.getName());
        Assertions.assertThrows(NotEnoughRights.class, () ->  bookingService.removeParticipants(be00, u2, Collections.emptyList()));

        bookingService.removeParticipants(be00, u1, participants);
        events = bookingService.events(today, today.plusDays(1));
        be0 = events.get(today).get(0);
        Assertions.assertEquals(0, be0.getParticipants().size());

        bookingService.revoke(be0, u1, dayBegin);
    }

}
