package com.example.sporthallmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HourlyScheduleActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private HourlyScheduleAdapter adapter;
    private List<TimeSlot> timeSlots = new ArrayList<>();

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

        databaseReference = FirebaseDatabase.getInstance().getReference("TimeSlots");


        // Set up RecyclerView for hourly slots
        RecyclerView scheduleList = findViewById(R.id.scheduleList);
        scheduleList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HourlyScheduleAdapter(this, timeSlots, databaseReference);
        scheduleList.setAdapter(adapter);

        loadTimeSlots();

    }

    private void loadTimeSlots() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                timeSlots.clear();
                for (int hour = 8; hour <= 22; hour++) {
                    String time = hour + ":00";
                    String status = dataSnapshot.child(time).getValue(String.class);
                    if (status == null) status = "Available"; // Default value
                    timeSlots.add(new TimeSlot(time, status));
                }
                adapter.notifyDataSetChanged(); // Refresh RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
