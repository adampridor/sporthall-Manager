package com.example.sporthallmanager;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayPracticeActivity extends AppCompatActivity {

    // RecyclerView to display the list of practices
    private RecyclerView recyclerView;

    // Adapter for managing practice data display
    private DisplayPracticeAdapter displayPracticeAdapter;

    // List to store retrieved practice data
    private List<Practice> practiceList;

    // Firebase database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_practice); // Set the activity layout

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set vertical layout

        // Initialize list and adapter
        practiceList = new ArrayList<>();
        displayPracticeAdapter = new DisplayPracticeAdapter(practiceList);
        recyclerView.setAdapter(displayPracticeAdapter); // Attach adapter to RecyclerView

        // Firebase reference to "practices" node
        databaseReference = FirebaseDatabase.getInstance().getReference("practices");

        //  Fetch practice data from Firebase
        fetchPracticesFromFirebase();

        // Back button to return to the previous screen
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Closes current activity and returns to the previous one
    }

    //  Method to retrieve practices from Firebase Realtime Database
    private void fetchPracticesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                practiceList.clear(); // Clear the list before updating it
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //  Convert each database entry into a Practice object
                    Practice practice = dataSnapshot.getValue(Practice.class);
                    if (practice != null) { // Ensure no null data is added
                        practiceList.add(practice);
                    }
                }
                displayPracticeAdapter.notifyDataSetChanged(); // Refresh RecyclerView with new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  Handle potential database errors (e.g., permissions issues)
            }
        });
    }
}