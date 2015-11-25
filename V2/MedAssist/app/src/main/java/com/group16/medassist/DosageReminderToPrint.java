package com.group16.medassist;

import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DosageReminderToPrint {

    private static final String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday"};

    int hour, minute;
    List<Integer> days;
    int dosage_id;
    List<Integer> reminderRowIds;

    DosageReminderToPrint() {days = new ArrayList<>(); reminderRowIds = new ArrayList<>();}

    public String toString() {
        String daysString;
        if(days.size() == 7) daysString = " Everyday";
        else {
            daysString = " on ";
            for(int n : days) daysString += dayNames[n-1] +", ";
            if(daysString.endsWith(", ")) daysString = daysString.substring(0, daysString.length()-2);
        }
        return (hour > 9 ? hour : "0" + hour) + ":" +
                (minute > 9 ? minute : "0" + minute) + daysString;
    }

}
