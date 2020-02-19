package com.example.hairnawa;

public class ChildData {
    private String phoneNumber, price;

    public ChildData(String phoneNumber, String price) {
        this.phoneNumber = phoneNumber;
        this.price = price;
    }

    String getPhoneNumber() {
        return this.phoneNumber;
    }
    String getPrice() { return this.price; }
}