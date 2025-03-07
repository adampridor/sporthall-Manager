
package com.example.sporthallmanager;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyScheduleAdapter extends RecyclerView.Adapter<HourlyScheduleAdapter.ViewHolder> {
    private List<TimeSlot> timeSlots;

    public HourlyScheduleAdapter(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
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
        holder.timeText.setText(slot.getTime());
        holder.statusText.setText(slot.getStatus());

        holder.statusText.setOnClickListener(v -> {
            if ("Available".equals(slot.getStatus())) {
                // change to unavailable
                slot.setStatus("Unavailable");
                holder.statusText.setText("Unavailable");

                // toast
                Toast.makeText(v.getContext(), "Time slot " + slot.getTime() + " is now Unavailable!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), PracticeRegisterActivity.class);
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