package com.nordclan.meeting;


import com.nordclan.meeting.services.*;
import com.nordclan.meeting.services.jpa.JpaAuthenticationService;
import com.nordclan.meeting.services.jpa.JpaBookingEventRepository;
import com.nordclan.meeting.services.jpa.JpaBookingService;
import com.nordclan.meeting.services.jpa.JpaBookingUserRepository;
import com.nordclan.meeting.services.reactive.ReactiveSingleThreadDelegatingBookingService;
import com.nordclan.meeting.services.views.BookingEventViewService;
import com.nordclan.meeting.services.views.SynchronousBookingEventViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeetingConfiguration {

    @Value("${nordclan.calendar.interval}")
    private int minutes;

    @Value("${nordclan.booking.max.interval.amount}")
    private int maxLongitudeOfMeetingInIntervals;

    @Autowired
    private JpaBookingEventRepository eventRepository;

    @Autowired
    private JpaBookingUserRepository userRepository;

    @Bean
    public CalendarService calendarService() {
        try {
            return new RussianWeekCalendarService(minutes);
        } catch (InvalidTimeUnit e) {
            throw new IllegalArgumentException();
        }
    }

    @Bean
    public BookingService bookingService() {
        return new Split2DaysBookingEventsOnEventsOperation(new JpaBookingService(eventRepository, calendarService(), maxLongitudeOfMeetingInIntervals), calendarService());
    }

    @Bean
    public AuthenticationService authenticationService(){
        return new JpaAuthenticationService(userRepository);
    }

    @Bean
    public BookingEventViewService bookingEventViewService() {
        return new SynchronousBookingEventViewService(new ReactiveSingleThreadDelegatingBookingService(bookingService(), calendarService()),
                                               authenticationService(),
                                               calendarService().timeIntervals());
    }

}
