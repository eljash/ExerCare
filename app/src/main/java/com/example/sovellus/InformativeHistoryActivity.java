package com.example.sovellus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import Classes.SuperMetodit;
import Classes.dataOlio;

/**
 * @author Jukka Hallikainen
 */
public class InformativeHistoryActivity extends AppCompatActivity {

    private ArrayList<dataOlio> historyList;
    private SuperMetodit SM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informative_history);

        SM = new SuperMetodit();
        Bundle extras = getIntent().getExtras();

        String json = extras.getString("historyList",null);
        Gson gson = new Gson();
        if(json != null){
            historyList = gson.fromJson(json, new TypeToken<ArrayList<dataOlio>>(){}.getType());
        }
        if(historyList!=null)insertValues();

        //Etsitään alanavigaatio elementti
        BottomNavigationView botNav = findViewById(R.id.navigationView);

        //Asetetaan navigaatio vaihtoehdoille toiminnot
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

                    case R.id.navigation_profile:
                        goProfile(botNav);
                        return true;
                }
                return false;
            }
        });
    }

    private void insertValues(){

        ArrayList<String> text = new ArrayList<>();
        for(int i = 0; i < historyList.size();i++){
            dataOlio x = historyList.get(i);
            String content = "Päiväys: "+ x.getDay()+"."+x.getMonth()+"."+x.getYear()+
                    "\nurheiluaika: "+SM.convertSeconds(x.sportSec())+"\nruutuaika: "+SM.convertSeconds(x.screenSec())
                    +"\npaino: "+x.returnWeight();
            text.add(content);
        }

        ListView lv = findViewById(R.id.historyList);
        lv.setAdapter(new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                text
        ));
    }

    /**
     * Metodilla siirrytään profiili aktiviteettiin.
     */
    public void goProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään oikealle <-
    }

    /**
     * Metodilla siirrytään historia aktiviteettiin.
     */
    public void goHistory(View v){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    /**
     * Metodilla siirrytään koti aktiviteettiin.
     */
    public void goMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }
}