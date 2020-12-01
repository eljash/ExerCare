package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity {
    private Mega mega;
    private SuperMetodit SM = new SuperMetodit();

    private static final String LOGTAG = "MainActivity.java";
    private boolean eRunning = false;
    private boolean sRunning = false;
    private Counter eCounter;
    private Counter sCounter;
    private User user = new User("user");

    private Switch sportSwitch;
    private Switch screenSwitch;

    private SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");

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

        sportSwitch = findViewById(R.id.Sport_switch);
        screenSwitch = findViewById(R.id.Screen_switch);

        checkLastExit();


    }

    public void clearSave(View v){
        mega.clearData();
        sCounter.setCurrent(0);
        eCounter.setCurrent(0);
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

    public void checkLastExit(){
        Log.d(LOGTAG,"checkLastExit()");
        Date timeNow = new Date();

        SharedPreferences shpref = this.getSharedPreferences("AppState", Activity.MODE_PRIVATE);

        String dateExit = shpref.getString("exitDate","0");
        String dateNow = DateFor.format(timeNow);
        Log.d(LOGTAG,"quit date was: "+dateExit+" date now: "+dateNow);

        eRunning = shpref.getBoolean("sportON",false);
        sRunning = shpref.getBoolean("screenON",false);
        sportSwitch.setChecked(false);
        screenSwitch.setChecked(false);

        if(eRunning){
            sportSwitch.setChecked(true);
            Date exitTime = new Date(shpref.getLong("sportOnDate",0));
            long seconds = (timeNow.getTime()-exitTime.getTime())/1000;

            if(dateExit.equals(dateNow)|| seconds < 28800){
                todayObject.insertSport(todayObject.sportSec()+(int)seconds);
            } else {
                eRunning = false;
                sRunning = false;
            }

        }else if(sRunning){
            screenSwitch.setChecked(true);
            Date exitTime = new Date(shpref.getLong("screenOnDate",0));
            long seconds = (timeNow.getTime()-exitTime.getTime())/1000;

            if(dateExit.equals(dateNow)|| seconds < 28800){
                todayObject.insertScreen(todayObject.screenSec()+(int)seconds);
            } else {
                eRunning = false;
                sRunning = false;
            }

        }

        eCounter = new Counter(todayObject.sportSec(),findViewById(R.id.sportTV));
        sCounter = new Counter(todayObject.screenSec(),findViewById(R.id.screenTV));

        if(eRunning)eCounter.run();
        else if(sRunning)sCounter.run();
    }

    public void saveExitState(){
        Log.d(LOGTAG,"saveExitState()");

        SharedPreferences shpref = this.getSharedPreferences("AppState", Activity.MODE_PRIVATE);
        SharedPreferences.Editor predit = shpref.edit();

        predit.putBoolean("sportON",sportSwitch.isChecked());
        predit.putBoolean("screenON",screenSwitch.isChecked());

        Date timeNow = new Date();
        predit.putString("exitDate", DateFor.format(timeNow));
        Log.d(LOGTAG,"quit date: "+DateFor.format(timeNow));

        predit.putString("exitPackage", todayObject.getPackageName());
        Log.d(LOGTAG,"package used before quit: "+todayObject.getPackageName());

        if(sportSwitch.isChecked()){
            predit.putLong("sportOnDate",timeNow.getTime());
        } else {
            predit.putLong("sportOnDate",0);
        }
        if(screenSwitch.isChecked()){
            predit.putLong("screenOnDate",timeNow.getTime());
        } else {
            predit.putLong("screenOnDate",0);
        }

        predit.apply();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(LOGTAG,"onStop()");
        todayObject.insertSport(eCounter.getCurrent());
        todayObject.insertScreen(sCounter.getCurrent());
        Log.d(LOGTAG,"Counter values (sport, screen) "+eCounter.getCurrent()+", "+sCounter.getCurrent());
        mega.saveToday();
        saveExitState();
        eCounter.stop();
        sCounter.stop();
    }

}
