package com.nordclan.meeting.services;

import com.nordclan.meeting.entities.BookingUser;

import java.util.List;

public interface AuthenticationService {

    BookingUser authorize(String name, String password);

    List<BookingUser> users();

}
