package com.nordclan.meeting.views;

public abstract class Interval {
    private int number;

    public Interval(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public  abstract boolean isEmpty();
    public  abstract boolean isLast();
    public  abstract boolean isFirst();
}
