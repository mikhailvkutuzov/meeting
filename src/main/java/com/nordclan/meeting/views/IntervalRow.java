package com.nordclan.meeting.views;

import java.util.List;

public class IntervalRow {
    private List<Interval> intervals;

    public IntervalRow(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }
}
