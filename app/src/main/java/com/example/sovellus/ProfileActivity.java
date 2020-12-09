package com.example.sovellus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    private UserProfileEditor profile;
    private User userProfile;

    private static final String LOGTAG = "ProfileActivity.java";

    SuperMetodit SM = new SuperMetodit();
    Mega mega = new Mega(this);

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

    private EditText input;
    private Button changeNameButton;
    private Button changeBirthButton;

    private DatePickerDialog.OnDateSetListener newBirthDay;

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

        changeNameButton = findViewById(R.id.changeProfileNameButton);
        changeBirthButton = findViewById(R.id.changeBirthDateButton);

        createChangeButtons();

        Intent intent = getIntent();
        this.weight = intent.getDoubleExtra("paino",mega.todayObject().returnWeight())+" kg";
        this.screen = SM.convertSeconds(intent.getIntExtra("ruutu",mega.todayObject().screenSec()));
        this.sport = SM.convertSeconds(intent.getIntExtra("urheilu",mega.todayObject().sportSec()));
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    public void goSettings(View v){
        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//siirrytään oikealle ->
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

    private void createChangeButtons(){

        input = new EditText(this);

        //Luodaan dialogi nimen vaihdolle
        //Mahdollistaa nappia painaessa ponnahdusikkunan esiin tulon joka kysyy uutta nimeä
        AlertDialog.Builder nameBuilder = new AlertDialog.Builder(this);

        //Asetetaan otsikko, ikoni ja viesti dialogiin
        nameBuilder.setTitle(R.string.new_user_name);
        nameBuilder.setIcon(R.drawable.retrad);
        nameBuilder.setMessage(R.string.name);

        nameBuilder.setView(input);

        //Lisätään "syötä" napin toiminnallisuus sekä napin teksti nimen vaihdolle
        nameBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                userProfile.setName(text);
                profile.saveProfile(userProfile);
                updateTextValues();
                Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
            }
        });

        //Lisätään "peruuta" napin toiminnallisuus sekä napin teksti nimen vaihdolle
        nameBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog changeName = nameBuilder.create();

        //Lisätään aktiviteetin nappiin toiminnallisuus joka avaa nimenvaihto dialogin
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName.show();
            }
        });

        //Lisätään syntymäpäivän vaihtoon toiminnallisuus joka avaa
        //kalenteri ponnahdusikkunan
        changeBirthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getApplicationContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        newBirthDay,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //Luetaan kalenteri ikkunaan syötetyt arvot profiiliin
        newBirthDay = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++; // Koska tammikuu = 0

                userProfile.setYear(year);
                userProfile.setMonth(month);
                userProfile.setDay(day);
                profile.saveProfile(userProfile);
                ProfileActivity.this.updateTextValues();
            }
        };

    }

}