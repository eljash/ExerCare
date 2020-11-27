package com.example.sovellus;

import android.widget.Switch;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class Counter {
    private int seconds;
    private int minutes;
    private int hours;
    private Timer timer;
    private int max;
    private CustomGauge tv;
    private boolean firstRun = true;

    public Counter(int seconds,CustomGauge CG) {
        this.seconds = seconds;
        this.timer = new Timer();
        this.tv = CG;
        this.tv.setValue(this.seconds);
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

    public void run() {
        firstRun = true;
        timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(firstRun){
                    firstRun = false;
                } else {
                    seconds++;
                    tv.setValue(seconds);
                }
            }
        }, 0, 1000);
    }

    public void stop() {
        this.timer.cancel();
        tv.setValue(this.seconds);
    }
}