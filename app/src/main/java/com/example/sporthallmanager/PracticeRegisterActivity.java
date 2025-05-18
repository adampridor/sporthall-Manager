package com.example.sporthallmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PracticeRegisterActivity extends AppCompatActivity {

    // UI elements for user input
    private EditText etFirstName, etLastName, etEmail, etActivityType, etAge, etPhoneNumber;
    private Button btnSubmit, btnBack;

    // Variables to store selected date and time
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practice_register);

        // Adjusts padding for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve selected date and time from the intent
        selectedDate = getIntent().getStringExtra("selected_date");
        selectedTime = getIntent().getStringExtra("selected_time");

        // Initialize UI components
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etActivityType = findViewById(R.id.etActivityType);
        etAge = findViewById(R.id.etAge);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Set click listener for Submit button
        btnSubmit.setOnClickListener(v -> submitForm());

        // Back button to return to the previous screen
        btnBack.setOnClickListener(v -> finish());
    }

    private void submitForm() {
        // Get input values from the user
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String activityType = etActivityType.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        // Validate input fields to ensure they are not empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                activityType.isEmpty() || age.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Practice object with all collected data
        Practice practice = new Practice(firstName, lastName, email, activityType, age, phoneNumber, selectedDate, selectedTime);

        // Firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("practices"); // Reference to store practice data
        DatabaseReference timeSlotsRef = database.getReference("TimeSlots").child(selectedDate).child(selectedTime); // Reference to update time slot status

        String practiceId = myRef.push().getKey(); // Generate a unique ID for the practice session

        // Save Practice data to Firebase and mark time slot as "Unavailable"
        myRef.child(practiceId).setValue(practice)
                .addOnSuccessListener(aVoid -> {
                    // If practice saved successfully, update time slot status
                    timeSlotsRef.setValue("Unavailable")
                            .addOnSuccessListener(aVoid2 -> {
                                Toast.makeText(PracticeRegisterActivity.this, "Practice booked!", Toast.LENGTH_SHORT).show();

                                // Navigate back to the HourlyScheduleActivity
                                Intent intent = new Intent(PracticeRegisterActivity.this, HourlyScheduleActivity.class);
                                intent.putExtra("selected_date", selectedDate); // Pass selected date back
                                startActivity(intent);
                                finish(); // Close the current activity
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(PracticeRegisterActivity.this, "Failed to update time slot!", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(PracticeRegisterActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show());
    }
}