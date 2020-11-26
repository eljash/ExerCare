package com.example.sovellus;

import android.widget.Switch;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class Counter {
    private int seconds;
    private int minutes;
    private int hours;
    private Timer timer;

    public Counter(int seconds) {
        this.seconds = seconds;
        this.timer = new Timer();
    }

    public Counter() {
        this.seconds = 0;
        this.timer = new Timer();
    }

    public int getCurrent() {
        if (!(this.seconds == 0)) {
            return this.seconds;
        } else return 0;
    }

    public void setCurrent(int seconds) {
        this.seconds = seconds;
    }

    public void run(TextView tv) {
        timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                tv.setText(Integer.toString(seconds));
            }
        }, 0, 1000);
    }

    public void stop(TextView tv) {
        this.timer.cancel();
        String value = tv.getText().toString();
        int intValue = Integer.parseInt(value);
        this.seconds = intValue;
        tv.setText(Integer.toString(this.seconds));
    }
}