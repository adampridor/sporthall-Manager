package com.example.sporthallmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("CalendarView", "Selected date: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                // Note that months are indexed from 0
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainCalendarActivity.this, HourlyScheduleActivity.class);
                intent.putExtra("selected_date", date);
                startActivity(intent);
            }

        });
    }
}