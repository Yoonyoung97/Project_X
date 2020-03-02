package com.example.hairnawa;


public class customerData {
    private String UserName;
    private String Date;
    private String Service;
    private Integer Price;

    public customerData(String userName, String date, String service,Integer price) {
        UserName = userName;
        Date = date;
        Service = service;
        Price = price;
    }

    public String getUserName() {
        return UserName;
    }

    public String getDate() {
        return Date;
    }

    public String getService() {
        return Service;
    }

    public Integer getPrice() { return Price; }
}
