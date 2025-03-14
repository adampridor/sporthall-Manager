package com.example.sporthallmanager;

public class Practice {
    private String firstName;
    private String lastName;
    private String email;
    private String activityType;
    private String age;
    private String phoneNumber;
    private String date;
    private String time;

    public Practice() {} // Empty constructor required for Firebase

    public Practice(String firstName, String lastName, String email, String activityType, String age, String phoneNumber, String date, String time) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activityType = activityType;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.time = time;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getActivityType() { return activityType; }
    public String getAge() { return age; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}
