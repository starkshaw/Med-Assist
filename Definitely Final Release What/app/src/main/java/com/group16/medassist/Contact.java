package com.group16.medassist;

public class Contact {

    int id;
    String name;
    String email;
    String phone;
    String address;

    Contact(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    Contact(int id, String name, String email, String phone, String address) {
        this(name, email, phone, address);
        this.id = id;
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
