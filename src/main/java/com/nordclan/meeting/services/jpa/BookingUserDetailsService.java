package com.nordclan.meeting.services.jpa;

import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.AuthenticationService;
import com.nordclan.meeting.services.BookingUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class BookingUserDetailsService implements UserDetailsService {

    private AuthenticationService authenticationService;

    public BookingUserDetailsService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BookingUser user = authenticationService.findByName(username);
        if (user != null) {
            return new BookingUserDetails(user);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
