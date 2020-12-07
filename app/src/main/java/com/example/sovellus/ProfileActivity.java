package com.example.sovellus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private UserProfileEditor profile;
    private User userProfile;

    private static final String LOGTAG = "ProfileActivity.java";

    SuperMetodit SM = new SuperMetodit();

    private String weight;
    private String sport;
    private String screen;

    private TextView profN;
    private TextView brthD;
    private TextView weightGoal;
    private TextView sportGoal;
    private TextView screenGoal;
    private TextView weightValue;
    private TextView sportValue;
    private TextView screenValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Etsitään alanavigaatio elementti
        BottomNavigationView botNav = findViewById(R.id.navigationView);

        //Kerrotaan mikä valittavista on auki: MainActivity = navigation_home
        botNav.setSelectedItemId(R.id.navigation_profile);

        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        goMain(botNav);
                        return true;

                    case R.id.navigation_history:
                        goHistory(botNav);
                        return true;

                }
                return false;
            }
        });

        Intent intent = getIntent();
        this.weight = String.valueOf(intent.getDoubleExtra("paino",0));
        this.screen = Integer.toString(intent.getIntExtra("ruutu",0));
        this.sport = Integer.toString(intent.getIntExtra("urheilu",0));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    public void goHistory(View v){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    public void goMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(LOGTAG,"onStart()");
        profile = new UserProfileEditor(this);
        userProfile = profile.returnProfile();

        updateTextValues();
    }

    public void changeName(View v){

    }

    private void updateTextValues(){
        profN = findViewById(R.id.profileName);
        brthD = findViewById(R.id.birthDate);
        weightGoal = findViewById(R.id.weightGoalTextValue);
        sportGoal = findViewById(R.id.sportGoalTextValue);
        screenGoal = findViewById(R.id.screenGoalTextValue);
        weightValue = findViewById(R.id.weightTextValue);
        sportValue = findViewById(R.id.sportTextValue);
        screenValue = findViewById(R.id.screenTextValue);

        profN.setText(userProfile.name());
        brthD.setText(userProfile.day()+"."+userProfile.month()+"."+userProfile.year());
        weightGoal.setText(String.valueOf(userProfile.weightGoal()));
        sportGoal.setText(SM.convertSeconds(userProfile.sportTimeGoal()));
        screenGoal.setText(SM.convertSeconds(userProfile.screenTimeGoal()));
        weightValue.setText(weight);
        sportValue.setText(sport);
        screenValue.setText(screen);
    }
}