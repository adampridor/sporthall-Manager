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
    private Button btnSubmit, btnBack;
    private String selectedDate, selectedTime;

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


        // ✅ Initialize UI components
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etActivityType = findViewById(R.id.etActivityType);
        etAge = findViewById(R.id.etAge);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // ✅ Submit button
        btnSubmit.setOnClickListener(v -> submitForm());

        // ✅ Back button (goes back to the previous activity)
        btnBack.setOnClickListener(v -> finish());
    }

    private void submitForm() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String activityType = etActivityType.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        selectedDate = getIntent().getStringExtra("selected_date");
        selectedTime = getIntent().getStringExtra("selected_time");

        // ✅ Validate input fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || activityType.isEmpty() || age.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Create a Practice object with Date & Time
        Practice practice = new Practice(firstName, lastName, email, activityType, age, phoneNumber, selectedDate, selectedTime);

        // ✅ Firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("practices");
        DatabaseReference timeSlotsRef = database.getReference("TimeSlots").child(selectedDate).child(selectedTime);

        String practiceId = myRef.push().getKey();

        //  Save Practice & Update Time Slot Status
        myRef.child(practiceId).setValue(practice)
                .addOnSuccessListener(aVoid -> {
                    //  Mark time slot as "Unavailable" in Firebase
                    timeSlotsRef.setValue("Unavailable")
                            .addOnSuccessListener(aVoid2 -> {
                                Toast.makeText(PracticeRegisterActivity.this, "Practice booked!", Toast.LENGTH_SHORT).show();

                                // ✅ Go back to HourlyScheduleActivity
                                Intent intent = new Intent(PracticeRegisterActivity.this, HourlyScheduleActivity.class);
                                intent.putExtra("selected_date", selectedDate);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(PracticeRegisterActivity.this, "Failed to update time slot!", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(PracticeRegisterActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show());
    }
}