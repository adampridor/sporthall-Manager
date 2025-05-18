package com.example.sporthallmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class HourlyScheduleAdapter extends RecyclerView.Adapter<HourlyScheduleAdapter.ViewHolder> {
    private List<TimeSlot> timeSlots; // List of time slots
    private Context context; // Context to handle UI interactions
    private DatabaseReference databaseReference; // Reference to Firebase database
    private String selectedDate; // Selected date to fetch specific time slots

    public HourlyScheduleAdapter(Context context, List<TimeSlot> timeSlots, DatabaseReference databaseReference, String selectedDate) {
        this.context = context;
        this.timeSlots = timeSlots;
        this.databaseReference = databaseReference.child(selectedDate); // Reference specific to the selected date
        this.selectedDate = selectedDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each row in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeSlot slot = timeSlots.get(position); // Get the current time slot

        // Fetch the status of the time slot from Firebase and update the UI
        databaseReference.child(slot.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class); // Get status from Firebase
                if (status != null) {
                    slot.setStatus(status); // Update local object
                    holder.statusText.setText(status); // Update UI
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors when fetching data
            }
        });

        // Set time and status text in the UI
        holder.timeText.setText(slot.getTime());
        holder.statusText.setText(slot.getStatus());

        // When clicking a time slot, open the PracticeRegisterActivity
        holder.statusText.setOnClickListener(v -> {
            if ("Available".equals(slot.getStatus())) {
                Intent intent = new Intent(v.getContext(), PracticeRegisterActivity.class);
                intent.putExtra("selected_time", slot.getTime()); // Pass the selected time
                intent.putExtra("selected_date", selectedDate); // Pass the selected date
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size(); // Return the number of time slots
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        TextView statusText;

        public ViewHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            statusText = itemView.findViewById(R.id.statusText);
        }
    }
}