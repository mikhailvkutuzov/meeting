package com.nordclan.meeting.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/booking")
public class BookingController {

    @GetMapping
    public void currentWeek(Model model) {

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
