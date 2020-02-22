package com.nordclan.meeting.services.jpa;

import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.BookingUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class BookingUserDetailsService implements UserDetailsService {

    private JpaBookingUserRepository repository;

    public BookingUserDetailsService(JpaBookingUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BookingUser user = repository.findByName(username);
        if (user != null) {
            return new BookingUserDetails(user);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
