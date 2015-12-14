package com.group16.medassist;

import java.util.List;

public class Dosage {
    int dosageId;
    int prescriptionId;
    int dosageAmount;
    String instructions;
    List<Dosage.Reminder> reminders;

    Dosage(int dosageId, int prescriptionId, int dosageAmount, String instructions) {
        this(prescriptionId, dosageAmount, instructions);
        this.dosageId = dosageId;
    }

    Dosage(int prescriptionId, int dosageAmount, String instructions) {
        this.prescriptionId = prescriptionId;
        this.dosageAmount = dosageAmount;
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Dosage Amount: " + dosageAmount + "\nInstructions: " + instructions;
    }

    public static class Reminder {
        int reminder_id, dosage_id, day, hour, minute, second;
        Reminder(int reminder_id, int dosage_id, int day, int hour, int minute, int second) {
            this(dosage_id, day, hour, minute, second);
            this.reminder_id = reminder_id;
        }

        Reminder() {}

        Reminder(int dosage_id, int day, int hour, int minute, int second) {
            this.dosage_id = dosage_id;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }

        @Override
        public String toString() {return hour+":"+minute+":"+second;}
    }

}