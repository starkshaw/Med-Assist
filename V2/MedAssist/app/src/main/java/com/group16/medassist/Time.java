package com.group16.medassist;

public class Time {

    int hour;
    int minute;
    int second;

    Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    Time(int hour, int minute) {
        this(hour, minute, 0);
    }

    @Override
    public String toString() {
        return hour+":"+minute+":"+second;
    }

}