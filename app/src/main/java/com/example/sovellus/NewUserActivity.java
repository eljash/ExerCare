package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewUserActivity extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        EditText nameET = findViewById(R.id.new_user_name);
        EditText weightET = findViewById(R.id.new_user_weight);
        EditText sportGoalET = findViewById(R.id.new_user_sport_goal);
        EditText screenGoalET = findViewById(R.id.new_user_screen_time_goal);

        nameET.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                String name = nameET.getText().toString();
                if (name.length() <= 0) {
                    nameET.setError("Enter your name");
                } else {
                    nameET.setError(null);
                }
            }
        });

        weightET.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                String weight = weightET.getText().toString();
                if (weight.length() <= 0) {
                    weightET.setError("Enter your weight");
                } else {
                    weightET.setError(null);
                }
            }
        });

        sportGoalET.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                String sportGoal = sportGoalET.getText().toString();
                if (sportGoal.length() <= 0) {
                    sportGoalET.setError("Enter the daily time that you wish to spend on sporting activity (minutes)");
                } else {
                    sportGoalET.setError(null);
                }
            }
        });

        screenGoalET.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                String screenGoal = screenGoalET.getText().toString();
                if (screenGoal.length() <= 0) {
                    screenGoalET.setError("Enter the daily time that you wish to spend on using technology (minutes)");
                } else {
                    screenGoalET.setError(null);
                }
            }
        });
    }

    public void submitNewUserInfo(View v) {

        EditText nameET = findViewById(R.id.new_user_name);
        String name = nameET.getText().toString();

        EditText weightET = findViewById(R.id.new_user_weight);
        String weight = weightET.getText().toString();

        EditText sportGoalET = findViewById(R.id.new_user_sport_goal);
        String sportGoal = sportGoalET.getText().toString();

        EditText screenGoalET = findViewById(R.id.new_user_screen_time_goal);
        String screenGoal = screenGoalET.getText().toString();

        String doubleRegex = "[0-9, .]";
        String regex = "[0-9]";

        /** TARKISTETAAN, ETTÄ KAIKKI KENTÄT TÄYTETTY ENNENKUIN SIIRRYTÄÄN ETEENPÄIN. MUUNNETAAN MINUUTIT SEKUNNEIKSI */

        if (!(name.equals("")) && (weight.matches(doubleRegex)) && (sportGoal.matches(regex) && screenGoal.matches(regex))) {
            double doubleWeight = Double.parseDouble(weight);
            int intSportGoal = Integer.parseInt(sportGoal);
            int intScreenGoal = Integer.parseInt(screenGoal);
            user = new User(name, doubleWeight, intSportGoal * 60, intScreenGoal * 60);
            UserProfileEditor upe = new UserProfileEditor(this);
            upe.saveProfile(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {

        }
    }
}