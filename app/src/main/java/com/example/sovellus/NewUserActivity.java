package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Classes.User;
import Classes.UserProfileEditor;

/**
 * @author Jukka Hallikainen
 */
public class NewUserActivity extends AppCompatActivity {

    User user;
    private int d;
    private int m;
    private int y;
    private DatePickerDialog.OnDateSetListener birthdayListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        EditText nameET = findViewById(R.id.new_user_name);
        EditText birthdayET = findViewById(R.id.new_user_birthday);
        EditText weightET = findViewById(R.id.new_user_weight);
        EditText weightGoalET = findViewById(R.id.new_user_weight_goal);
        EditText sportGoalET = findViewById(R.id.new_user_sport_goal);
        EditText screenGoalET = findViewById(R.id.new_user_screen_time_goal);

        /** MÄÄRITETÄÄN LISTENERIT, JOTTA SAADAAN KÄYTTÄJÄLLE VIRHEILMOITUS JOS KENTTÄ JÄÄ TYHJÄKSI */

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

        birthdayET.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    NewUserActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    birthdayListener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        birthdayListener = (datePicker, year, month, day) -> {
            month++; // Koska tammikuu = 0
            String date=day+"."+month+"."+year;
            birthdayET.setText(date);
            d = day;
            m = month;
            y = year;
        };

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

        weightGoalET.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                String weightGoal = weightGoalET.getText().toString();
                if (weightGoal.length() <= 0) {
                    weightGoalET.setError("Enter the the weight you wish to achieve");
                } else {
                    weightGoalET.setError(null);
                }
            }
        });
    }

    /**
     * Luetaan aktiviteetin tekstikenttien arvot, jotta ne voidaan tallentaa profiiliin.
     */
    public void submitNewUserInfo(View v) {

        EditText nameET = findViewById(R.id.new_user_name);
        String name = nameET.getText().toString();

        EditText birthdayET = findViewById(R.id.new_user_birthday);
        String birthday = birthdayET.getText().toString();

        EditText weightET = findViewById(R.id.new_user_weight);
        String weight = weightET.getText().toString();

        EditText weightGoalET = findViewById(R.id.new_user_weight_goal);
        String weightGoal = weightGoalET.getText().toString();

        EditText sportGoalET = findViewById(R.id.new_user_sport_goal);
        String sportGoal = sportGoalET.getText().toString();

        EditText screenGoalET = findViewById(R.id.new_user_screen_time_goal);
        String screenGoal = screenGoalET.getText().toString();

        String doubleRegex = "[0-9.]+";
        String regex = "[0-9]+";

        /** TARKISTETAAN, ETTÄ KAIKKI KENTÄT TÄYTETTY ENNENKUIN SIIRRYTÄÄN ETEENPÄIN */

        if (!(name.equals("")) && !(birthday.equals("")) && (weight.matches(doubleRegex)) && (weightGoal.matches(regex)) && (sportGoal.matches(regex)) && (screenGoal.matches(regex))) {

            double doubleWeight = Double.parseDouble(weight);
            double doubleWeightGoal = Double.parseDouble(weightGoal);
            int intSportGoal = Integer.parseInt(sportGoal);
            int intScreenGoal = Integer.parseInt(screenGoal);

            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            System.out.println(formatter.format(date));

            /** lUODAAN KÄYTTÄJÄ OLIOMUUTTUJINEEN JA SIIRRYTÄÄN MAINACTIVITYYN */

            user = new User(name, doubleWeight, doubleWeightGoal, intSportGoal * 60, intScreenGoal * 60, y, m, d, date);
            Log.d("DEBUG", d + "." + m + "." + y);
            UserProfileEditor upe = new UserProfileEditor(this);
            upe.saveProfile(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (name.equals("")) {
            nameET.setError("Enter your weight");
        }
        if (birthday.equals("")) {
            birthdayET.setError("Set your birthday");
        }
        if (!(weight.matches(doubleRegex))) {
            weightET.setError("Enter your weight");
        }
        if (!(weightGoal.matches(doubleRegex))) {
            weightGoalET.setError("Enter your goal weight");
        }
        if (!(sportGoal.matches(regex))) {
            sportGoalET.setError("Enter the daily time that you wish to spend on sporting activity (minutes)");
        }
        if (!(screenGoal.matches(regex))) {
            screenGoalET.setError("Enter the daily time that you wish to spend on using technology (minutes)");
        }
    }
}