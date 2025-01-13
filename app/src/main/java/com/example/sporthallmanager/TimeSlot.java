package com.example.sporthallmanager;

public class TimeSlot {
    private String time;
    private String status;

    public TimeSlot(String time, String status) {
        this.time = time;
        this.status = status;
    }

    // Getters and setters
    // Getter for time
    public String getTime() {
        return time;
    }

    // Setter for time
    public void setTime(String time) {
        this.time = time;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }
}
