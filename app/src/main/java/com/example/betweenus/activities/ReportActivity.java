package com.example.betweenus.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private int currentUserID = 1;

    private TextView tvTotal, tvToday, tvWeek, tvMonth, tvAvg, tvSuccess;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tvTotal = findViewById(R.id.tvTotal);
        tvToday = findViewById(R.id.tvToday);
        tvWeek = findViewById(R.id.tvWeek);
        tvMonth = findViewById(R.id.tvMonth);
        tvAvg = findViewById(R.id.tvAvg);
        tvSuccess = findViewById(R.id.tvSuccess);

        barChart = findViewById(R.id.barChart);

        loadData();
    }

    private void loadData() {

        DatabaseHelper db = new DatabaseHelper(this);

        int total = db.getTotalMinutes(db.currentUserId);
        int today = db.getTodayMinutes(db.currentUserId);
        int week = db.getWeekMinutes(db.currentUserId);
        int month = db.getMonthMinutes(db.currentUserId);

        float avg = db.getAverageStudyTime(db.currentUserId);
        float success = db.getSuccessRate(db.currentUserId) * 100;

        tvTotal.setText("🌸 Total: " + formatTime(total));
        tvToday.setText("📅 Today: " + formatTime(today));
        tvWeek.setText("🗓 Week: " + formatTime(week));
        tvMonth.setText("🌙 Month: " + formatTime(month));
        tvAvg.setText("📊 Avg Session: " + String.format("%.1f min", avg));
        tvSuccess.setText("✨ Success Rate: " + String.format("%.0f%%", success));

        setupChart(today, week, month);
    }

    private String formatTime(int minutes) {
        int hrs = minutes / 60;
        int mins = minutes % 60;
        return hrs + "h " + mins + "m";
    }

    private void setupChart(int today, int week, int month) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, today));
        entries.add(new BarEntry(2, week));
        entries.add(new BarEntry(3, month));

        BarDataSet dataSet = new BarDataSet(entries, "Study Time");

        // pastel vibe 🌸
        dataSet.setColors(new int[]{
                0xFFF8BBD0, // pink
                0xFFC5E1A5, // green
                0xFFB3E5FC  // blue
        });

        BarData data = new BarData(dataSet);
        data.setValueTextSize(12f);

        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);

        barChart.invalidate();
    }
}