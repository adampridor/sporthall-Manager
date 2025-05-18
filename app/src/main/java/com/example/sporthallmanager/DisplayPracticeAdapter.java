package com.example.sporthallmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DisplayPracticeAdapter extends RecyclerView.Adapter<DisplayPracticeAdapter.PracticeViewHolder> {

    // List that holds all practices retrieved from the database
    private List<Practice> practiceList;

    // Constructor that initializes the adapter with the practice list
    public DisplayPracticeAdapter(List<Practice> practiceList) {
        this.practiceList = practiceList;
    }

    @NonNull
    @Override
    public PracticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single row in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_practice, parent, false);
        return new PracticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PracticeViewHolder holder, int position) {
        // Get the practice object at the current position
        Practice practice = practiceList.get(position);

        // Bind data from the practice object to the corresponding TextViews
        holder.tvName.setText(practice.getFirstName());
        holder.tvLastName.setText(practice.getLastName());
        holder.tvEmail.setText(practice.getEmail());
        holder.tvActivityType.setText(practice.getActivityType());
        holder.tvAge.setText(practice.getAge());
        holder.tvPhoneNumber.setText(practice.getPhoneNumber());

        // Display Date and Time, handling null values to avoid crashes
        holder.dateText.setText("Date: " + (practice.getDate() != null ? practice.getDate() : "N/A"));
        holder.timeText.setText("Time: " + (practice.getTime() != null ? practice.getTime() : "N/A"));
    }

    @Override
    public int getItemCount() {
        // Return the total number of practice items in the list
        return practiceList.size();
    }

    // ViewHolder class that holds references to the UI elements in each row
    public static class PracticeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLastName, tvEmail, tvActivityType, tvAge, tvPhoneNumber, dateText, timeText;

        public PracticeViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize all TextViews with their corresponding IDs from row_practice.xml
            tvName = itemView.findViewById(R.id.tvName);
            tvLastName = itemView.findViewById(R.id.tvLastName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvActivityType = itemView.findViewById(R.id.tvActivityType);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            dateText = itemView.findViewById(R.id.tvDate);
            timeText = itemView.findViewById(R.id.tvTime);
        }
    }
}