package com.example.hairnawa;

import java.util.ArrayList;
import java.util.Vector;

public class CommentData {
    private String name;
    private String context;
    private String score;
    private String time;

    public Vector<ChildData> child;

    public CommentData(String name, String context, String score) {
        this.name= name;
        this.context = context;
        this.score = score;
    }

    public CommentData(String name, String context, String score, String time){
        this.name  =name;
        this.context = context;
        this.score = score;
        this.time = time;

    }
    public String getName() {
        return this.name;
    }

    public String getContext() {
        return this.context;
    }

    public String getScore() {
        return this.score;
    }
    public String getTime() {return this.time;}
}