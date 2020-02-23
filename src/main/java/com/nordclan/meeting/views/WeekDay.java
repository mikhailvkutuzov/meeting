package com.nordclan.meeting.views;

public class WeekDay {
    private String date;
    private String name;

    public WeekDay(String date, String name) {
        this.date = date;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}
