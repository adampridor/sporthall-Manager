package com.example.sporthallmanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HourlyScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hourly_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the date from the intent
        String selectedDate = getIntent().getStringExtra("selected_date");
        TextView dateHeader = findViewById(R.id.dateHeader);
        dateHeader.setText(selectedDate);

        // Set up RecyclerView for hourly slots
        RecyclerView scheduleList = findViewById(R.id.scheduleList);
        scheduleList.setLayoutManager(new LinearLayoutManager(this));
        scheduleList.setAdapter(new HourlyScheduleAdapter(generateTimeSlots()));
    }

    private List<TimeSlot> generateTimeSlots() {
        List<TimeSlot> slots = new ArrayList<>();
        // Generate slots from 8 AM to 10 PM
        for(int hour = 8; hour <= 22; hour++) {
            slots.add(new TimeSlot(hour + ":00", "Available"));
        }
        return slots;
    }
    }
