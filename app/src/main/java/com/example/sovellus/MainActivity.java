package com.example.sovellus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity {
    private Mega mega;
    private SuperMetodit SM;

    private static final String LOGTAG = "MainActivity.java";
    private boolean eRunning = false;
    private boolean sRunning = false;
    private Counter eCounter;
    private Counter sCounter;
    private User user;
    private UserProfileEditor profile;

    private SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");

    private SwitchCompat sportSwitch;
    private SwitchCompat screenSwitch;

    private dataOlio todayObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Etsitään alanavigaatio elementti
        BottomNavigationView botNav = findViewById(R.id.navigationView);

        //Kerrotaan mikä valittavista on auki: MainActivity = navigation_home
        botNav.setSelectedItemId(R.id.navigation_home);

        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_history:
                        goHistory(botNav);
                        return true;

                    case R.id.navigation_profile:
                        goProfile(botNav);
                        return true;

                }
                return false;
            }
        });

        this.mega = new Mega(this);
        this.SM = new SuperMetodit();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(LOGTAG,"onStart()");
        checkForProfile();
        CustomGauge sportGauge = findViewById(R.id.sportTV);
        CustomGauge screenGauge = findViewById(R.id.screenTV);
        sportGauge.setEndValue(user.sportTimeGoal());
        screenGauge.setEndValue(user.screenTimeGoal());
        sportGauge.setPointStartColor(Color.BLUE);
        sportGauge.setPointEndColor(Color.BLUE);
        screenGauge.setPointStartColor(Color.rgb(255,102,0));
        screenGauge.setPointEndColor(Color.RED);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOGTAG,"onResume()");

        mega.todayData();
        todayObject = mega.todayObject();

        sportSwitch = findViewById(R.id.Sport_switch);
        screenSwitch = findViewById(R.id.Screen_switch);

        checkLastExit();

        correctCounterStates();
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

    public void goDebug(View v){
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//siirrytään oikealle ->
    }

    public void goProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("urheilu", eCounter.getCurrent());
        intent.putExtra("ruutu",sCounter.getCurrent());
        intent.putExtra("paino",todayObject.returnWeight());
        startActivity(intent);

        //Lisätään animaatio aktiviteetin vaihtoon
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//siirrytään oikealle ->
    }

    public void goHistory(View v){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//siirrytään oikealle ->
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

    /** METODI VARMISTAA, ETTÄ SWITCHIN BOOLEAN ON SAMA KUIN AJASTIMEN PYÖRIMIS BOOLEAN */
    private void correctCounterStates(){
        if(sportSwitch.isChecked()!=eCounter.isRunning() || screenSwitch.isChecked() != sCounter.isRunning()){

            // KATSOO URHEILU SWITCHIN JA AJASTIMEN
            if(sportSwitch.isChecked() && !eCounter.isRunning()){
                eCounter.run();
            } else if(!sportSwitch.isChecked() && eCounter.isRunning()){
                eCounter.stop();
            }

            // KATSOO RUUTU SWITCHIN JA AJASTIMEN
            if(screenSwitch.isChecked() && !sCounter.isRunning()){
                sCounter.run();
            } else if(!screenSwitch.isChecked() && sCounter.isRunning()){
                sCounter.stop();
            }
        }
    }

    /** METODI KATSOO ONKO SOVELLUKSESSA LUOTU JO PROFIILIA */
    private void checkForProfile(){
        profile = new UserProfileEditor(this);
        user = profile.returnProfile();
        if (user.name().equals("No name")) {
            Log.d("Message", "No user found, switching to NewUserActivity");
            Intent intent = new Intent(this, NewUserActivity.class);
            startActivity(intent);
        }
    }

    /** KATSOO MILLAINEN TILA TALLENNETTIIN VIIMEKSI AKTIVITEETISTA POISTUESSA */
    public void checkLastExit(){
        Log.d(LOGTAG,"checkLastExit()");
        Date timeNow = new Date();

        // HAKEE SHAREDPREFERENCES:ISTÄ TIEDOSTON JOHON TILA TALLENNETTIIN
        SharedPreferences shpref = this.getSharedPreferences("AppState", Activity.MODE_PRIVATE);

        String dateExit = shpref.getString("exitDate","0");
        String dateNow = DateFor.format(timeNow);
        Log.d(LOGTAG,"quit date was: "+dateExit+" date now: "+dateNow);

        eRunning = shpref.getBoolean("sportON",false);
        sRunning = shpref.getBoolean("screenON",false);

        // ASETTAA VARMUUDEN VUOKSI AKTIVITEETIN SWITCH:IT OFF TILAAN
        sportSwitch.setChecked(false);
        screenSwitch.setChecked(false);

        // TÄMÄ BOOLEAN TOTEUTUU JOS URHEILU SWITCH OLI PÄÄLLÄ AKTIVITEETISTÄ POISTUESSA
        if(eRunning){
            sportSwitch.setChecked(true);
            Date exitTime = new Date(shpref.getLong("sportOnDate",0));
            long seconds = (timeNow.getTime()-exitTime.getTime())/1000;

            // JOS POISTUMIS PÄIVÄ OLI SAMA KUIN UUDELLEEN AVAUS PÄIVÄ TAI SIITÄ ON ALLE 8 TUNTIA (28800SEC)
            // LAUSEKE LISÄÄ NYKYISELLE PÄIVÄLLE KULUNEEN AJAN, ELI JOS VUOROKAUSI ON KERENNYT VAIHTUA
            // MUTTA AIKAERO ON ALLE 8 TUNTIA ASETETAAN KULUNUT AIKA NYKYISELLE PÄIVÄLLE
            if(dateExit.equals(dateNow)|| seconds < 28800){
                todayObject.insertSport(todayObject.sportSec()+(int)seconds);
            } else {
                eRunning = false;
                sRunning = false;
            }

        }else if(sRunning){ // TÄMÄ BOOLEAN TOTEUTUU JOS RUUTU SWITCH OLI PÄÄLLÄ POISTUESSA
            screenSwitch.setChecked(true);
            Date exitTime = new Date(shpref.getLong("screenOnDate",0));
            long seconds = (timeNow.getTime()-exitTime.getTime())/1000;

            // JOS POISTUMIS PÄIVÄ OLI SAMA KUIN UUDELLEEN AVAUS PÄIVÄ TAI SIITÄ ON ALLE 8 TUNTIA (28800SEC)
            // LAUSEKE LISÄÄ NYKYISELLE PÄIVÄLLE KULUNEEN AJAN, ELI JOS VUOROKAUSI ON KERENNYT VAIHTUA
            // MUTTA AIKAERO ON ALLE 8 TUNTIA ASETETAAN KULUNUT AIKA NYKYISELLE PÄIVÄLLE
            if(dateExit.equals(dateNow)|| seconds < 28800){
                todayObject.insertScreen(todayObject.screenSec()+(int)seconds);
            } else {
                eRunning = false;
                sRunning = false;
            }

        }

        // LUO UUDET AJASTIMET
        eCounter = new Counter(todayObject.sportSec(),findViewById(R.id.sportTV),user.sportTimeGoal());
        sCounter = new Counter(todayObject.screenSec(),findViewById(R.id.screenTV),user.screenTimeGoal());

        // LAITTAA AJASTIMEN PÄÄLLE SEN PERUSTEELLA OLIKO JOMPI KUMPI PÄÄLLÄ VIIMEKSI
        if(eRunning)eCounter.run();
        else if(sRunning)sCounter.run();

        // NOLLATAAN TALLENNETTU "POISTUMIS" TILA

        SharedPreferences.Editor predit = shpref.edit();
        predit.putBoolean("sportON",false);
        predit.putBoolean("screenON",false);
        predit.putString("exitDate", "0");

        predit.putString("exitPackage", "0");
        predit.putLong("sportOnDate",0);
        predit.putLong("screenOnDate",0);

        predit.apply();
    }

    /** TALLENTAA AKTIVITEETIN TILAN ENNEN SEN SULKEUTUMISTA */
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
    protected void onPause(){
        super.onPause();
        Log.d(LOGTAG,"onPause()");
        todayObject.insertSport(eCounter.getCurrent());
        todayObject.insertScreen(sCounter.getCurrent());
        Log.d(LOGTAG,"Counter values (sport, screen) "+eCounter.getCurrent()+", "+sCounter.getCurrent());
        mega.saveToday();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(LOGTAG,"onStop()");
        saveExitState();
    }

}
