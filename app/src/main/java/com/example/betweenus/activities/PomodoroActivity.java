package com.example.betweenus.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.adapter.PomodoroTimer;

public class PomodoroActivity extends AppCompatActivity {

    private TextView tvTime;
    private ProgressBar circleTimer;

    private PomodoroTimer timerManager;

    private int focusMinutes = 25;
    private int breakMinutes = 5;
    private int rounds = 3;

    private int currentRound = 1;
    private boolean isFocusPhase = true;

    private int focusSecondsCompleted = 0;
    private int currentUserID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pomodoro);

        tvTime = findViewById(R.id.tvTime);
        circleTimer = findViewById(R.id.circleTimer);

        Button btnStart = findViewById(R.id.btnStart);
        Button btnCustomize = findViewById(R.id.btnCustomize);

        DatabaseHelper db = new DatabaseHelper(this);
        db.insertSampleData();

        // ✅ FIX: Initialize ONCE here
        timerManager = new PomodoroTimer(tvTime, circleTimer, new PomodoroTimer.TimerListener() {
            @Override
            public void onTick(int secondsElapsed) {
                if (isFocusPhase) {
                    focusSecondsCompleted = secondsElapsed;
                }
            }

            @Override
            public void onFinish() {
                handlePhaseFinish();
            }
        });

        btnStart.setOnClickListener(v -> {
            currentRound = 1;
            isFocusPhase = true;
            startNextPhase();
        });

        btnCustomize.setOnClickListener(v -> showCustomizeDialog());
    }

    private void startNextPhase() {

        // ✅ Reset tracking each phase
        focusSecondsCompleted = 0;

        int time = isFocusPhase ? focusMinutes : breakMinutes;

        timerManager.startTimer(time * 60);
    }

    private void handlePhaseFinish() {

        if (isFocusPhase) {

            int minutesCompleted = focusSecondsCompleted / 60;

            int pointsEarned = minutesCompleted + 10;

            saveSession(true, minutesCompleted);
            updateUserPoints(pointsEarned);

            showBreakDialog();

        } else {

            currentRound++;

            if (currentRound > rounds) {
                showSessionCompleteDialog();
                return;
            }

            showFocusDialog();
        }

        isFocusPhase = !isFocusPhase;

        // Start next phase automatically
        startNextPhase();
    }

    private void showCustomizeDialog() {

        View view = getLayoutInflater().inflate(R.layout.dialog_customize_time, null);

        Spinner spFocus = view.findViewById(R.id.spFocus);
        Spinner spBreak = view.findViewById(R.id.spBreak);
        Spinner spRounds = view.findViewById(R.id.spRounds);
        Button btnSave = view.findViewById(R.id.btn_save);

        Integer[] focusOptions = {15, 20, 25, 30, 45};
        Integer[] breakOptions = {5, 10, 15};
        Integer[] roundOptions = {1, 2, 3, 4, 5};

        spFocus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, focusOptions));
        spBreak.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, breakOptions));
        spRounds.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roundOptions));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        btnSave.setOnClickListener(v -> {
            focusMinutes = (int) spFocus.getSelectedItem();
            breakMinutes = (int) spBreak.getSelectedItem();
            rounds = (int) spRounds.getSelectedItem();
            dialog.dismiss();
        });
    }

    private void showBreakDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Break Time 🌿")
                .setMessage("You did great! Time to rest.")
                .setPositiveButton("Start Break", null)
                .show();
    }

    private void showFocusDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Focus Time 📚")
                .setMessage("Ready for the next round?")
                .setPositiveButton("Start", null)
                .show();
    }

    private void showSessionCompleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("All Done 🎉")
                .setMessage("You completed all rounds!")
                .setPositiveButton("Yay!", null)
                .show();
    }

    private void saveSession(boolean completed, int minutes) {

        String date = java.time.LocalDate.now().toString();

        DatabaseHelper db = new DatabaseHelper(this);
        db.insertStudySession(db.currentUserId, date, minutes, completed ? 1 : 0);
    }

    private void updateUserPoints(int pointsToAdd) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.addPointsToUser(db.currentUserId, pointsToAdd);
    }
}