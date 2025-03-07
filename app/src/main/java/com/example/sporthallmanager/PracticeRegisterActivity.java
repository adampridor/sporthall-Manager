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

        // Basic validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || activityType.isEmpty() || age.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show collected data
        String message = "Name: " + firstName + " " + lastName + "\nEmail: " + email +
                "\nActivity: " + activityType + "\nAge: " + age + "\nPhone: " + phoneNumber;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(PracticeRegisterActivity.this, HourlyScheduleActivity.class);
        startActivity(intent);
    }
}