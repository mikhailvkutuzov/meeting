package com.nordclan.meeting.services.jpa;

import com.nordclan.meeting.entities.BookingUser;
import com.nordclan.meeting.services.AuthenticationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class JpaAuthenticationService implements AuthenticationService {

    private JpaBookingUserRepository userRepository;

    public JpaAuthenticationService(JpaBookingUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BookingUser authorize(String name, String password) {
        return userRepository.findByNameAndPassword(name, password);
    }

    @Override
    public List<BookingUser> users() {
        return userRepository.findAll();
    }


    @Override
    public BookingUser findByName(String userName) {
        return userRepository.findByName(userName);
    }


    @Override
    public List<BookingUser> findByName(List<String> userNames) {
        return userRepository.findAllByNameIn(userNames);
    }
}
