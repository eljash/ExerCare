package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Mega mega;
    private static final String LOGTAG = "MainActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mega = new Mega(this);
    }

    @Override
    protected void onStart(){
        ergrgrge
        super.onStart();
        Log.d(LOGTAG,"onStart()");
        mega.loadData("2020","1");
        mega.todayData();
        mega.loadData("2019","3");
    }

    public void debugSave(View v){
        String dateDe;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        for(int i = 0; i < 2; i++){
            dateDe = year+"/"+month+"/"+(10+i++);
            this.mega.insertData("2020",Integer.toString(i),i+3,69+i,i*10,69*i,true);
        }
        this.mega.insertData("1995","12",15,69,420,69,true);
    }

    public void saveData(View v){
        this.mega.saveData();
    }

    public void clearData(View v){
        this.mega.clearData();
    }

    public void loadData(View v){
        /**
        this.mega.loadData();
        Log.d(LOGTAG,"List size: "+this.mega.listSize());
        dataOlio perse = this.mega.searchDateFromList("2020/10/10");
        if(perse != null){
            Log.d(LOGTAG,"Object found and it has weight of: "+perse.returnWeight());
        }
        perse = this.mega.searchDateFromList("2020/10/14");
        if(perse != null){
            Log.d(LOGTAG,"Object found and it has weight of: "+perse.returnWeight());
        }
        perse = this.mega.searchDateFromList("2020/10/18");
        if(perse != null){
            Log.d(LOGTAG,"Object found and it has weight of: "+perse.returnWeight());
        }
        perse = this.mega.searchDateFromList("1999/01/01");
        if(perse != null){
            Log.d(LOGTAG,"Object found and it has weight of: "+perse.returnWeight());
        }
         */
    }
}