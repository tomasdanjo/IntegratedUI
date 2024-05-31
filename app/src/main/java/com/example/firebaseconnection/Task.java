package com.example.firebaseconnection;

public class Task {
    public String name;
    public String date;
    public Long duration;
    public String mode;
    public int reward;

    public Task(String name, String date, Long duration, String mode, int reward) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.mode = mode;
        this.reward = reward;
    }
}