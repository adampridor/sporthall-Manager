package com.example.sporthallmanager;

// This class represents a practice session that is stored in Firebase
public class Practice {

    // Attributes related to a practice session
    private String firstName;     // First name of the participant
    private String lastName;      // Last name of the participant
    private String email;         // Email of the participant
    private String activityType;  // Type of activity (e.g., basketball, handball)
    private String age;           // Age of the participant
    private String phoneNumber;   // Contact phone number
    private String date;          // Date of the practice session
    private String time;          // Time slot of the practice session

    // Default constructor required for Firebase data retrieval
    public Practice() {}

    // Constructor to initialize all fields when creating a practice session
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

    // Getters (used to retrieve values from a Practice object)
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getActivityType() { return activityType; }
    public String getAge() { return age; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}