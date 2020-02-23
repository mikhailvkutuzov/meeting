package com.nordclan.meeting.services.jpa;

import com.nordclan.meeting.entities.BookingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaBookingUserRepository extends JpaRepository<BookingUser, Long> {

    BookingUser findByName(String name);

    BookingUser findByNameAndPassword(String name, String password);

    List<BookingUser> findAllByNameIn(List<String> names);

}
