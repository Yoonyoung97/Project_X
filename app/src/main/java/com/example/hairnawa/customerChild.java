package com.example.hairnawa;

public class customerChild {
    private String last_date;
    private String last_procedure;

    public customerChild(String last_date, String last_procedure) {
        this.last_date = last_date;
        this.last_procedure = last_procedure;
    }

    public String getLast_date() {
        return last_date;
    }

    public String getLast_procedure() {
        return last_procedure;
    }

}
