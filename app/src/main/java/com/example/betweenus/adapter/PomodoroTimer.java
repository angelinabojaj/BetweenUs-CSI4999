package com.example.betweenus.adapter;

import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PomodoroTimer {

    private CountDownTimer timer;
    private TextView tvTime;
    private ProgressBar progressBar;

    private int totalTime;
    private boolean isRunning = false;

    public PomodoroTimer(TextView tvTime, ProgressBar progressBar) {
        this.tvTime = tvTime;
        this.progressBar = progressBar;
    }

    public void startTimer(int seconds) {

        // 🔥 FIX: cancel old timer FIRST
        if (timer != null) {
            timer.cancel();
        }

        totalTime = seconds;
        isRunning = true;

        timer = new CountDownTimer(seconds * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {

                int secondsLeft = (int) (millisUntilFinished / 1000);

                int min = secondsLeft / 60;
                int sec = secondsLeft % 60;

                tvTime.setText(String.format("%02d:%02d", min, sec));

                int progress = (int) ((secondsLeft / (float) totalTime) * 100);
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                isRunning = false;
            }
        }.start();
    }

    public void stop() {
        if (timer != null) timer.cancel();
        isRunning = false;
    }
}
