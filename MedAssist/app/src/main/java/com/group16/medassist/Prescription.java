package com.group16.medassist;

public class Prescription {

    int id;
    String drug;
    int quantity;
    String dateOfIssue;

    Prescription() {}

    Prescription(String name, int quantity, String dateOfIssue) {
        this.drug = name;
        this.quantity = quantity;
        this.dateOfIssue = dateOfIssue;
    }

    Prescription(int id, String drug, int quantity, String dateOfIssue) {
        this.id = id;
        this.drug = drug;
        this.quantity = quantity;
        this.dateOfIssue = dateOfIssue;
    }

    public String toString() {
        return "Medication Name: " + drug + "\nQuantity: " + quantity + "\nDate of issue: " + dateOfIssue;
    }

}