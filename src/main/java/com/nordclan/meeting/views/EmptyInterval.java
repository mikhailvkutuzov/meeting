package com.nordclan.meeting.views;

public class EmptyInterval extends Interval {
    public EmptyInterval(int number) {
        super(number);

    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean isFirst() {
        return false;
    }
}
