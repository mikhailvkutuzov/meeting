package com.nordclan.meeting.views;

public class IntervalCaption {
    private Integer number;
    private String from;
    private String to;

    public IntervalCaption(String from, String to, Integer number) {
        this.from = from;
        this.to = to;
        this.number = number;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Integer getNumber() {
        return number;
    }
}
