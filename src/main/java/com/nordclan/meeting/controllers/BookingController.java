package com.nordclan.meeting.controllers;

import com.nordclan.meeting.services.views.BookingEventViewService;
import com.nordclan.meeting.views.WeekPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Locale;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingEventViewService bookingService;

    @GetMapping
    public String currentWeek(Model model) {
       var weekPage = bookingService.eventsForRange(LocalDate.now(), Locale.ENGLISH);
       model.addAttribute("weekPage", weekPage);
       return "meeting";
    }

    @GetMapping(path = "/next")
    public void nextWeek(Model model) {

    }

    @GetMapping(path = "/previous")
    public void previousWeek(Model model) {

    }


    @PostMapping(path = "/book")
    public void book() {

    }

    @PostMapping(path = "/revoke")
    public void revoke() {

    }


}
