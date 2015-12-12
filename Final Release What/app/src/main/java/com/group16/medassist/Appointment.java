package com.group16.medassist;

/**
 * Created by Derri_000 on 17/11/2015.
 */
public class Appointment
{
    int id;
    Contact contact;
    String startTime;
    String endTime;

    Appointment() {}

    Appointment(Contact contact, String startTime, String endTime)
    {
        this.contact = contact;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    Appointment(int id, Contact contact, String startTime, String endTime)
    {
        this.id = id;
        this.contact = contact;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String toString() {
        return "Doctor Name: " + contact + "\nStart Time: " + startTime + "\tEnd Time " + endTime + "\n(Tap to delete)";
    }
}
