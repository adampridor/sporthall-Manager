package com.example.sporthallmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_calendar);

        // Adjusts padding to accommodate system UI elements (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Reference to the CalendarView from the layout
        CalendarView calendarView = findViewById(R.id.calendarView);

        // Listener for date selection in CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("CalendarView", "Selected date: " + dayOfMonth + "/" + (month + 1) + "/" + year);

                // Note: month is zero-based, so we add 1 to display correctly
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                // Display the selected date as a Toast message
                Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();

                // Navigate to HourlyScheduleActivity and pass the selected date
                Intent intent = new Intent(MainCalendarActivity.this, HourlyScheduleActivity.class);
                intent.putExtra("selected_date", date);
                startActivity(intent);
            }
        });

        // Button to navigate to DisplayPracticeActivity
        Button btnGoToDisplayPractice = findViewById(R.id.button);

        // Set OnClickListener to open DisplayPracticeActivity when clicked
        btnGoToDisplayPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate to DisplayPracticeActivity
                Intent intent = new Intent(MainCalendarActivity.this, DisplayPracticeActivity.class);
                startActivity(intent);
            }
        });
    }
}