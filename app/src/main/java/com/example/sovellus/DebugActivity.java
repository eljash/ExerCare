package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class DebugActivity extends AppCompatActivity {

    Mega mega = new Mega(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }

    public void createFakeData(View v){
        mega.insertData("2020","01",1,696969,420420,60,true);
        mega.insertData("2020","01",3,696969,420420,60,true);
        mega.insertData("2020","01",5,696969,420420,60,true);
        mega.insertData("2020","01",9,696969,420420,60,true);
        mega.insertData("2020","01",10,696969,420420,60,true);
        mega.insertData("2020","02",15,696969,420420,60,true);
        mega.insertData("2020","02",17,696969,420420,60,true);
        mega.insertData("2020","03",1,696969,420420,60,true);
        mega.insertData("2020","03",2,696969,420420,60,true);
        mega.insertData("2020","03",3,696969,420420,60,true);
        mega.insertData("2020","04",4,696969,420420,60,true);
        mega.insertData("2020","04",6,696969,420420,60,true);
        mega.insertData("2020","04",10,696969,420420,60,true);
    }

    public void clearSave(View v){
        mega.clearData();
    }
}