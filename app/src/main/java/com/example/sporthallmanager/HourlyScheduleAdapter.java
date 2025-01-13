
package com.example.sporthallmanager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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