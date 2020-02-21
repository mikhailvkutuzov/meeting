package com.nordclan.meeting;

import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.BookingService;
import com.nordclan.meeting.services.CalendarService;
import com.nordclan.meeting.services.jpa.JpaBookingUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
class MeetingApplicationTests {

    @Autowired
    private JpaBookingUserRepository userRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CalendarService calendarService;

    @Test
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

        LocalDate thisDate = LocalDate.now();

        List<LocalTime> intervals = calendarService.timeIntervals();
    }

}
