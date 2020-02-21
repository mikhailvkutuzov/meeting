package com.nordclan.meeting.services.jpa;

import com.nordclan.meeting.entities.BookingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaBookingEventRepository extends JpaRepository<BookingEvent, Long> {

    List<BookingEvent> findAllByFromDateAfterOrToDateBefore(LocalDateTime from, LocalDateTime to);

}
