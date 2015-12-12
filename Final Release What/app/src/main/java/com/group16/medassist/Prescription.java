package com.group16.medassist;

public class Prescription {

    int id;
    String drug;
    int quantity;
    String dateOfIssue;
    String notes;
    Contact contact;

    Prescription(String drug, int quantity, String dateOfIssue, String notes, Contact contact) {
        this.drug = drug;
        this.quantity = quantity;
        this.dateOfIssue = dateOfIssue;
        this.notes = notes;
        this.contact = contact;
    }

    Prescription(int id, String drug, int quantity, String dateOfIssue, String notes, Contact contact) {
        this(drug, quantity, dateOfIssue, notes, contact);
        this.id = id;
    }

    public String toString() {
        return "Medication: " + drug + "\nDate of issue: " + dateOfIssue;
    }

}