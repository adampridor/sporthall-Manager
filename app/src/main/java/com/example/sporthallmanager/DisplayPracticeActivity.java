package com.example.sporthallmanager;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sporthallmanager.Practice;
import com.example.sporthallmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DisplayPracticeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DisplayPracticeAdapter displayPracticeAdapter;
    private List<Practice> practiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_practice);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        practiceList = new ArrayList<>();
        displayPracticeAdapter = new DisplayPracticeAdapter(practiceList);
        recyclerView.setAdapter(displayPracticeAdapter);

        fetchPracticesFromFirebase();
    }

    private void fetchPracticesFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("practices");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                practiceList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Practice practice = dataSnapshot.getValue(Practice.class);
                    practiceList.add(practice);
                }
                displayPracticeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}