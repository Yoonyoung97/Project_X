package com.example.hairnawa;

import java.util.Vector;

public class customerParent {
    private String name;
    private Integer total_price;
    private Integer visits;
    private String guestID;


    private Vector<customerChild> child;

    public customerParent(String name, Integer total_price, String last_date, String last_procedure, Integer visits, String guestID){
        this.guestID = guestID;
        this.name = name;
        this.total_price = total_price;
        this.visits = visits;
        child = new Vector<>();
        child.add(new customerChild(last_date,last_procedure));

    }
    public String getGuestID() {
        return guestID;
    }

    public String getName() {
        return name;
    }

    public Integer getTotal_price() {
        return total_price;
    }

    public Integer getVisits() {
        return visits;
    }


    public Vector<customerChild> getChild() {
        return child;
    }

}
