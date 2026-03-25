package com.example.betweenus.activities;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pomodoro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTime = findViewById(R.id.tvTime);
        circleTimer = findViewById(R.id.circleTimer);

        Button btnStart = findViewById(R.id.btnStart);
        Button btnCustomize = findViewById(R.id.btnCustomize);
        Button btnSound = findViewById(R.id.btnSound);

        timerManager = new PomodoroTimer(tvTime, circleTimer);

//        btnStart.setOnClickListener(v -> {
//            timerManager.startTimer(focusMinutes * 60);
//        });

        btnStart.setOnClickListener(v -> {
            currentRound = 1;
            isFocusPhase = true;
            startNextPhase();
        });

        btnCustomize.setOnClickListener(v -> showCustomizeDialog());

//        btnSound.setOnClickListener(v -> showSoundDialog());
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

//    private void showSoundDialog() {
//        String[] sounds = {"Rain", "Cafe", "None"};
//
//        new AlertDialog.Builder(this)
//                .setTitle("Choose Sound")
//                .setItems(sounds, (dialog, which) -> {
//
//                    if (mediaPlayer != null) {
//                        mediaPlayer.release();
//                    }
//
//                    if (which == 0) {
//                        mediaPlayer = MediaPlayer.create(this, R.raw.rain);
//                    } else if (which == 1) {
//                        mediaPlayer = MediaPlayer.create(this, R.raw.cafe);
//                    } else {
//                        return;
//                    }
//
//                    mediaPlayer.setLooping(true);
//                    mediaPlayer.start();
//                }).show();
//    }

    private void startNextPhase() {

        int time = isFocusPhase ? focusMinutes : breakMinutes;

        timerManager.startTimer(time * 60);

        new CountDownTimer(time * 60000L, 1000) {
            public void onTick(long l) {
            }

            public void onFinish() {

                if (isFocusPhase) {
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
                startNextPhase();
            }
        }.start();
    }

    private void showBreakDialog() {
        new AlertDialog.Builder(this).setTitle("Break Time 🌿").setMessage("You did great! Time to rest.").setPositiveButton("Start Break", null).show();
    }

    private void showFocusDialog() {
        new AlertDialog.Builder(this).setTitle("Focus Time 📚").setMessage("Ready for the next round?").setPositiveButton("Start", null).show();
    }

    private void showSessionCompleteDialog() {
        new AlertDialog.Builder(this).setTitle("All Done 🎉").setMessage("You completed all rounds!").setPositiveButton("Yay!", null).show();
    }

}