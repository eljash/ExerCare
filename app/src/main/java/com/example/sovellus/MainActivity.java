package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity {
    private Mega mega;
    private static final String LOGTAG = "MainActivity.java";
    private boolean eRunning = false;
    private boolean sRunning = false;
    private Counter eCounter = new Counter();
    private Counter sCounter = new Counter();
    private User user = new User("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mega = new Mega(this);
        CustomGauge sportGauge = findViewById(R.id.sportTV);
        sportGauge.setEndValue(user.getTimeGoal());
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(LOGTAG,"onStart()");

    }

    public void debugSave(View v){
        mega.todayData();
    }

    public void saveData(View v){
        this.mega.saveData();
    }

    public void loadData(View v){

    }

    public void sportTimeClicked(View v) {
        CustomGauge sportGauge = findViewById(R.id.sportTV);
        if (!eRunning) {
            eCounter.run(sportGauge);
            eRunning = true;
        } else {
            eCounter.stop(sportGauge);
            eRunning = false;
        }
    }

    /*

    public void screenTimeClicked(View v) {
        TextView screenTV = findViewById(R.id.screenTV);
        if (sRunning == false) {
            sCounter.run(screenTV);
            sRunning = true;
        } else {
            sCounter.stop(screenTV);
            sRunning = false;
        }
    } */
}
