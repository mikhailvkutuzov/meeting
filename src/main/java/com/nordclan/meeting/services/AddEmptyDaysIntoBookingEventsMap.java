package com.nordclan.meeting.services;

import com.nordclan.meeting.entities.BookingEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AddEmptyDaysIntoBookingEventsMap extends DelegatingBookingService {
    public AddEmptyDaysIntoBookingEventsMap(BookingService service) {
        super(service);
    }

    @Override
    public Map<LocalDate, List<BookingEvent>> events(LocalDate from, LocalDate to) throws InvalidTimeRange {
        var events = super.events(from, to);
        Stream.iterate(from, i -> i.plusDays(1)).takeWhile(d -> !d.isAfter(to))
                .forEach(d -> {
                    var list = events.get(d);
                    if(list == null) {
                        events.put(d, Collections.EMPTY_LIST);
                    }
                });
        return events;
    }
}
