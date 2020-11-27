package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity {
    private Mega mega;
    private static final String LOGTAG = "MainActivity.java";
    private boolean eRunning = false;
    private boolean sRunning = false;
    private Counter eCounter;
    private Counter sCounter;
    private User user = new User("user");

    private Switch sportSwitch;
    private Switch screenSwitch;

    private dataOlio todayObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mega = new Mega(this);
        CustomGauge sportGauge = findViewById(R.id.sportTV);
        CustomGauge screenGauge = findViewById(R.id.screenTV);
        sportGauge.setEndValue(user.sportTimeGoal());
        screenGauge.setEndValue(user.screenTimeGoal());
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(LOGTAG,"onStart()");

        mega.todayData();
        todayObject = mega.todayObject();
        eCounter = new Counter(todayObject.sportSec(),findViewById(R.id.sportTV));
        sCounter = new Counter(todayObject.screenSec(),findViewById(R.id.screenTV));

        sportSwitch = findViewById(R.id.Sport_switch);
        screenSwitch = findViewById(R.id.Screen_switch);

    }

    public void sportTimeClicked(View v) {
        CustomGauge sportGauge = findViewById(R.id.sportTV);
        if (!eRunning) {
            eCounter.run();
            eRunning = true;
            if(sRunning){
                sCounter.stop();
                sRunning = false;
                screenSwitch.setChecked(false);
            }
        } else {
            eCounter.stop();
            eRunning = false;
        }
    }

    public void goProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("urheilu", eCounter.getCurrent());
        intent.putExtra("ruutu",sCounter.getCurrent());
        intent.putExtra("paino",todayObject.returnWeight());
        startActivity(intent);
    }

    public void screenTimeClicked(View v) {
        CustomGauge screenGauge = findViewById(R.id.screenTV);
        if (!sRunning) {
            sCounter.run();
            sRunning = true;
            if(eRunning){
                eCounter.stop();
                eRunning = false;
                sportSwitch.setChecked(false);
            }
        } else {
            sCounter.stop();
            sRunning = false;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(LOGTAG,"onStop()");
        todayObject.insertSport(eCounter.getCurrent());
        todayObject.insertScreen(sCounter.getCurrent());
        Log.d(LOGTAG,"Counter values (sport, screen) "+eCounter.getCurrent()+", "+sCounter.getCurrent());
        mega.saveToday();
    }

}
