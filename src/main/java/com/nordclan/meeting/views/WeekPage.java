package com.nordclan.meeting.views;

import java.util.List;

public class WeekPage {

    private List<WeekDay> dayCaption;
    private List<IntervalCaption> intervalCaptions;

    private MeetingIntervalTable meetings;
    private String errorMessage;

    public WeekPage(List<WeekDay> dayCaption, List<IntervalCaption> intervalCaptions, MeetingIntervalTable meetings, String errorMessage) {
        this.dayCaption = dayCaption;
        this.intervalCaptions = intervalCaptions;
        this.meetings = meetings;
        this.errorMessage = errorMessage;
    }

    public WeekPage(List<WeekDay> dayCaption, List<IntervalCaption> intervalCaptions, MeetingIntervalTable meetings) {
        this.dayCaption = dayCaption;
        this.intervalCaptions = intervalCaptions;
        this.meetings = meetings;
    }

    public List<WeekDay> getDayCaption() {
        return dayCaption;
    }

    public List<IntervalCaption> getIntervalCaptions() {
        return intervalCaptions;
    }

    public MeetingIntervalTable getMeetings() {
        return meetings;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return errorMessage != null;
    }
}
