package com.example.sovellus;

import java.util.Timer;
import java.util.TimerTask;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class Counter {
    private int seconds;
    private Timer timer;
    private CustomGauge tv;
    private boolean firstRun = true;
    private boolean isRunning;
    private int pointerSize;

    public Counter(int seconds,CustomGauge CG) {
        this.seconds = seconds;
        this.timer = new Timer();
        this.tv = CG;
        this.tv.setValue(this.seconds);
        this.isRunning = false;
        this.pointerSize = this.seconds;

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
        isRunning = true;
        firstRun = true;
        timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(firstRun){
                    firstRun = false;
                } else {
                    seconds++;
                    if (seconds * 12 > 24) {
                        tv.setPointSize(pointerSize * 12);
                    } else {
                        tv.setPointSize(24);
                    }
                    tv.setValue(seconds);
                }
            }
        }, 0, 1000);
    }

    public void stop() {
        this.timer.cancel();
        isRunning = false;
        tv.setValue(this.seconds);
    }

    public boolean isRunning(){
        return this.isRunning;
    }
}