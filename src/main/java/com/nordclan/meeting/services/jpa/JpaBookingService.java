package com.nordclan.meeting.services.jpa;

import com.nordclan.meeting.entities.BookingEvent;
import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Transactional(propagation = Propagation.REQUIRED)
@Service
public class JpaBookingService implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(JpaBookingService.class);

    private JpaBookingUserRepository userRepository;
    private JpaBookingEventRepository eventRepository;
    private CalendarService calendarService;
    private ExecutorService executor;

    public JpaBookingService(JpaBookingUserRepository userRepository, JpaBookingEventRepository eventRepository, CalendarService calendarService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.calendarService = calendarService;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public BookingUser authorize(String name, String password) {
        return userRepository.findByNameAndPassword(name, password);
    }

    @Override
    public BookingEvent book(BookingUser user, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo) throws InvalidTimeUnit, OverlappingTimeInterval {

        var available = calendarService.timeIntervals();

        if (!available.contains(timeFrom)) {
            throw new InvalidTimeUnit();
        }

        if (!available.contains(timeTo)) {
            throw new InvalidTimeUnit();
        }

        var from = LocalDateTime.of(dateFrom, timeFrom);
        var to = LocalDateTime.of(dateTo, timeTo);

        if (from.isAfter(to)) {
            //todo
            throw new InvalidTimeUnit();
        }

        try {
            EventOrEvents submitResult = executor.submit(() -> {

                var event = new BookingEvent();
                event.setFromDate(from);
                event.setToDate(to);
                event.setUser(user);

                List<BookingEvent> overlapping = eventRepository.findAllByFromDateBeforeAndToDateAfter(to, from);

                EventOrEvents result = new EventOrEvents();

                if (overlapping.size() > 0) {
                    result.overlapping = overlapping;
                } else {
                    try {
                        eventRepository.save(event);
                        result.event = event;
                    } catch (RuntimeException e) {
                        var theSame = eventRepository.findByFromDateAndToDate(from, to);
                        if (theSame != null) {
                            result.overlapping = new ArrayList<>(1);
                            result.overlapping.add(theSame);
                        } else {
                            throw e;
                        }
                    }
                }
                return result;
            }).get();

            if (submitResult.event != null) {
                return submitResult.event;
            } else {
                throw new OverlappingTimeInterval(submitResult.overlapping);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e.getCause());
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private static class EventOrEvents {
        BookingEvent event;
        List<BookingEvent> overlapping;
    }

    @Override
    public int revoke(BookingEvent event, BookingUser user) throws NotEnoughRights {
        if (!event.getUser().getId().equals(user.getId())) {
            throw new NotEnoughRights();
        }
        try {
            return executor.submit(() -> {
                var now = LocalDateTime.now();
                var to = event.getToDate();
                var from = event.getFromDate();
                if (now.isBefore(from)) {
                    eventRepository.delete(event);
                    return (int) Duration.between(from, to).toMinutes();
                } else if (now.isBefore(to)) {
                    int revokedMinutes = calendarService.getValidTimeInterval((int) Duration.between(now, event.getToDate()).toMinutes());
                    event.setToDate(to.minusMinutes(revokedMinutes));
                    eventRepository.save(event);
                    return revokedMinutes;
                } else {
                    return 0;
                }
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Map<LocalDate, List<BookingEvent>> events(LocalDate from, LocalDate to) throws InvalidTimeRange {
        var begin = LocalDateTime.of(from, LocalTime.of(0, 0));
        var end = LocalDateTime.of(to, LocalTime.of(0, 0));
        if (begin.isAfter(end)) {
            throw new InvalidTimeRange();
        }
        var events = eventRepository.findAllByFromDateBetweenOrToDateBetween(begin, end, begin, end);

        return events.stream()
                .filter(be -> be.getFromDate().isEqual(end) || be.getToDate().isEqual(begin))
                .collect(Collectors.groupingBy(e -> e.getFromDate().toLocalDate()));
    }
}
