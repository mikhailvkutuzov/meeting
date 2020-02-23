package com.nordclan.meeting.services.views;

import com.nordclan.meeting.entities.BookingEvent;
import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.AuthenticationService;
import com.nordclan.meeting.services.reactive.ReactiveBookingService;
import com.nordclan.meeting.views.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SynchroniousBookingEventViewService implements BookingEventViewService {

    private ReactiveBookingService bookingService;
    private AuthenticationService authenticationService;
    private List<IntervalCaption> intervalCaptions;
    private List<LocalTime> intervals;

    public SynchroniousBookingEventViewService(ReactiveBookingService bookingService, AuthenticationService authenticationService, List<IntervalCaption> intervalCaptions, List<LocalTime> intervals) {
        this.bookingService = bookingService;
        this.authenticationService = authenticationService;
        this.intervalCaptions = intervalCaptions;
        this.intervals = intervals;
    }

    private LocalDateTime fromLocalDateView(String dateView) {
        return LocalDateTime.of(LocalDate.parse(dateView), LocalTime.of(0, 0));
    }

    @Override
    public WeekPage book(String userName, String fromDate, Integer fromInterval, String toDate, Integer toInterval, Locale locale) {
        BookingUser user = authenticationService.findByName(userName);

        try {
            var map = bookingService.book(user, LocalDate.parse(fromDate), intervals.get(fromInterval), LocalDate.parse(toDate), intervals.get(toInterval))
                    .get();
            return convert(map, locale);
        } catch (InterruptedException | ExecutionException e) {
            try {
                var errorMap = bookingService.eventsForRange(LocalDate.parse(fromDate)).get();
                return convertWithException(errorMap, locale, e);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex.getCause());
            }
        }
    }

    @Override
    public WeekPage addParticipants(MeetingInterval meeting, String actorName, List<String> userNames, Locale locale) {
        try {
            var map = bookingService.addParticipants(meeting.getEvent(), authenticationService.findByName(actorName), authenticationService.findByName(userNames)).get();
            return convert(map, locale);
        } catch (InterruptedException | ExecutionException e) {
            try {
                var errorMap = bookingService.eventsForRange(meeting.getEvent().getFromDate().toLocalDate()).get();
                return convertWithException(errorMap, locale, e);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex.getCause());
            }
        }
    }

    @Override
    public WeekPage removeParticipants(MeetingInterval meeting, String actorName, List<String> userNames, Locale locale) {
        try {
            var map = bookingService.removeParticipants(meeting.getEvent(), authenticationService.findByName(actorName), authenticationService.findByName(userNames)).get();
            return convert(map, locale);
        } catch (InterruptedException | ExecutionException e) {
            try {
                var errorMap = bookingService.eventsForRange(meeting.getEvent().getFromDate().toLocalDate()).get();
                return convertWithException(errorMap, locale, e);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex.getCause());
            }
        }
    }

    @Override
    public WeekPage revoke(MeetingInterval meeting, String actorName, Locale locale) {
        try {
            return convert(bookingService.revoke(meeting.getEvent(), authenticationService.findByName(actorName)).get(), locale);
        } catch (InterruptedException | ExecutionException e) {
            try {
                var errorMap = bookingService.eventsForRange(meeting.getEvent().getFromDate().toLocalDate()).get();
                return convertWithException(errorMap, locale, e);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex.getCause());
            }
        }
    }

    @Override
    public WeekPage eventsForRange(String fromDate, String toDate, Locale locale) {
        try {
            return convert(bookingService.eventsForRange(LocalDate.parse(fromDate), LocalDate.parse(toDate)).get(), locale);
        }  catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    private WeekPage convert(Map<LocalDate, List<BookingEvent>> map, Locale locale) {
        List<WeekDay> weekCaption = format(map.keySet(), locale);
        var table = format(map);
        return new WeekPage(weekCaption, intervalCaptions, table);
    }

    private WeekPage convertWithException(Map<LocalDate, List<BookingEvent>> map, Locale locale, Throwable exception) {
        List<WeekDay> weekCaption = format(map.keySet(), locale);
        var table = format(map);
        return new WeekPage(weekCaption, intervalCaptions, table, exception.getClass().getName());
    }


    private List<WeekDay> format(Collection<LocalDate> weekDays, Locale locale) {
        return weekDays.stream()
                .sorted()
                .map(d -> new WeekDay(d.toString(), d.getDayOfWeek().getDisplayName(TextStyle.FULL, locale)))
                .collect(Collectors.toList());
    }

    private MeetingIntervalTable format(Map<LocalDate, List<BookingEvent>> data) {
        int rowNumber = 0;

        var intervalRows = new ArrayList<IntervalRow>(intervals.size());

        for (var iBegin : intervals) {
            var row = new ArrayList<Interval>(7);
            for (LocalDate date : data.keySet()) {
                Optional<BookingEvent> optional = data.get(date).stream().filter(event -> {
                    var toTime = event.getToDate().toLocalTime();
                    var fromTime = event.getFromDate().toLocalTime();
                    return iBegin.isBefore(toTime) && (iBegin.isAfter(fromTime) || iBegin.equals(fromTime));
                }).findFirst();

                if(optional.isPresent()) {
                    BookingEvent event = optional.get();
                    var from = event.getFromDate().toLocalTime();
                    var firstInterval = from.equals(intervals.get(rowNumber));
                    var to = event.getToDate().toLocalTime();
                    var lastInterval = rowNumber == intervals.size() - 1 || to.equals(intervals.get(rowNumber + 1));
                    var participants = event.getParticipants().stream().map(p -> p.getName()).collect(Collectors.toList());

                    if(firstInterval && lastInterval) {
                        row.add(new SingleInterval(rowNumber, "", participants, event));
                    } else if(!firstInterval && !lastInterval) {
                        row.add(new MiddleInterval(rowNumber, "", participants, event));
                    } else if (firstInterval) {
                        row.add(new FirstInterval(rowNumber, "", participants, event));
                    } else {
                        row.add(new LastInterval(rowNumber, "", participants, event));
                    }
                } else {
                    row.add(new EmptyInterval(rowNumber));
                }
            }

            intervalRows.add(new IntervalRow(row));
        }
        return new MeetingIntervalTable(intervalRows);
    }

}
