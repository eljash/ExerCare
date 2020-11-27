package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private UserProfileEditor profile;
    private User userProfile;

    private static final String LOGTAG = "ProfileActivity.java";

    TextView profN;
    TextView brthD;
    TextView weightGoal;
    TextView sportGoal;
    TextView screenGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        profN.setText(userProfile.name());
        brthD.setText(userProfile.day()+"."+userProfile.month()+"."+userProfile.year());
        weightGoal.setText(String.valueOf(userProfile.weightGoal()));
        sportGoal.setText(Integer.toString(userProfile.sportTimeGoal()));
        screenGoal.setText(Integer.toString(userProfile.screenTimeGoal()));
    }
}