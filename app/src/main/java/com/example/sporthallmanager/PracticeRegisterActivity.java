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

    private EditText etFirstName, etLastName, etEmail, etActivityType, etAge, etPhoneNumber;
    private Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practice_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize UI components
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etActivityType = findViewById(R.id.etActivityType);
        etAge = findViewById(R.id.etAge);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void submitForm() {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String activityType = etActivityType.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            String selectedDate = getIntent().getStringExtra("selected_date");
            String selectedTime = getIntent().getStringExtra("selected_time");

            // Basic validation
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || activityType.isEmpty() || age.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create Practice object
            Practice practice = new Practice(firstName, lastName, email, activityType, age, phoneNumber);

            // Firebase references
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("practices");
            DatabaseReference timeSlotsRef = database.getReference("TimeSlots").child(selectedDate).child(selectedTime);

            String practiceId = myRef.push().getKey();
            myRef.child(practiceId).setValue(practice)
                    .addOnSuccessListener(aVoid -> {
                        // ✅ Update the time slot to "Unavailable"
                        timeSlotsRef.setValue("Unavailable")
                                .addOnSuccessListener(aVoid2 -> {
                                    Toast.makeText(PracticeRegisterActivity.this, "Time slot booked!", Toast.LENGTH_SHORT).show();

                                    // ✅ Start HourlyScheduleActivity only after both operations succeed
                                    Intent intent = new Intent(PracticeRegisterActivity.this, HourlyScheduleActivity.class);
                                    intent.putExtra("selected_date", selectedDate);  // Pass the date back
                                    startActivity(intent);
                                    finish(); // Close the form activity
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(PracticeRegisterActivity.this, "Failed to update time slot!", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PracticeRegisterActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                    });
        }
    }

