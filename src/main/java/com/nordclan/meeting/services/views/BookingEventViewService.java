package com.nordclan.meeting.services.views;

import com.nordclan.meeting.views.MeetingInterval;
import com.nordclan.meeting.views.WeekPage;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface BookingEventViewService {
    WeekPage book(String userName, String fromDate, Integer fromIntervalNumber, String toDate, Integer toIntervalNumber, Locale locale);

    WeekPage addParticipants(MeetingInterval meeting, String actorName, List<String> userNames, Locale locale);

    WeekPage removeParticipants(MeetingInterval meeting, String actorName, List<String> userNames, Locale locale);

    WeekPage revoke(MeetingInterval meeting, String actorName, Locale locale);

    WeekPage eventsForRange(String fromDate, String toDate, Locale locale);

    WeekPage eventsForRange(LocalDate weekDay, Locale locale);
}
