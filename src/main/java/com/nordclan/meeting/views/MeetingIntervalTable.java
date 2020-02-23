package com.nordclan.meeting.views;

import java.util.List;

public class MeetingIntervalTable {
    private List<IntervalRow> rows;

    public MeetingIntervalTable(List<IntervalRow> rows) {
        this.rows = rows;
    }

    public List<IntervalRow> getRows() {
        return rows;
    }
}
