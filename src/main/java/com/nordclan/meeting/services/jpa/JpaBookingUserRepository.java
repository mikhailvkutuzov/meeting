package com.nordclan.meeting.services.jpa;

import com.nordclan.meeting.entities.BookingUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookingUserRepository extends JpaRepository<BookingUser, Long> {

    BookingUser findByNameAndPassword(String name, String password);

}
