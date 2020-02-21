package com.nordclan.meeting.services.jpa;

import com.nordclan.meeting.entities.BookingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaBookingEventRepository extends JpaRepository<BookingEvent, Long> {

    BookingEvent findByFromDateAndToDate(LocalDateTime begin, LocalDateTime end);

    List<BookingEvent> findAllByFromDateBeforeAndToDateAfter(LocalDateTime end, LocalDateTime begin);

    List<BookingEvent> findAllByFromDateBetweenOrToDateBetween(LocalDateTime begin1, LocalDateTime end1, LocalDateTime begin2, LocalDateTime end2);

}
