
package com.example.sporthallmanager;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyScheduleAdapter extends RecyclerView.Adapter<HourlyScheduleAdapter.ViewHolder> {
    private List<TimeSlot> timeSlots;
    private Context context;
    private DatabaseReference databaseReference;
    private String selectedDate;

    public HourlyScheduleAdapter(Context context, List<TimeSlot> timeSlots, DatabaseReference databaseReference, String selectedDate) {
        this.context = context;
        this.timeSlots = timeSlots;
        this.databaseReference = databaseReference.child(selectedDate);
        this.selectedDate = selectedDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_slot_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeSlot slot = timeSlots.get(position);

        // ✅ Fetch real-time updates from Firebase
        databaseReference.child(slot.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                if (status != null) {
                    slot.setStatus(status);
                    holder.statusText.setText(status);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        holder.timeText.setText(slot.getTime());
        holder.statusText.setText(slot.getStatus());

        holder.statusText.setOnClickListener(v -> {
            if ("Available".equals(slot.getStatus())) {
                // Open the form, but don't change the status yet
                Intent intent = new Intent(v.getContext(), PracticeRegisterActivity.class);
                intent.putExtra("selected_time", slot.getTime());
                intent.putExtra("selected_date", selectedDate); // Pass the selected date
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
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
