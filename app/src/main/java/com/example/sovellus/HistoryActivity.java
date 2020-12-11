package com.example.sovellus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;

import Classes.HistoryManager;
import Classes.dataOlio;

/**
 * @author Eljas Hirvelä ja Arttu Pösö
 */
public class HistoryActivity extends AppCompatActivity {

    private HistoryManager HM;
    private int startY, startM, startD, endY, endM, endD;
    private ArrayList<dataOlio> list = new ArrayList<>();
    private LineGraphSeries<DataPoint> lineaarinenSarja;
    private BarGraphSeries<DataPoint> painoSarja;
    private int size;

    private String format;

    private TextView startDate;
    private TextView endDate;
    private DatePickerDialog.OnDateSetListener startDateListener,endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Etsitään alanavigaatio elementti
        BottomNavigationView botNav = findViewById(R.id.navigationView);

        //Kerrotaan mikä valittavista on auki: MainActivity = navigation_home
        botNav.setSelectedItemId(R.id.navigation_history);

        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        goMain(botNav);
                        return true;

                    case R.id.navigation_profile:
                        goProfile(botNav);
                        return true;

                }
                return false;
            }
        });

        HM = new HistoryManager(this);
        setDateListener();
        buildGraphWeight();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    /**
     * Metodilla luodaan Radio-painikkeiden toiminnot.
     */
    public void radioButtonClick(View v){
        //Onko painettu radio nappi päällä
        boolean checked = ((RadioButton) v).isChecked();

        //Katsotaan mikä radio napeista on painettuna
        switch(v.getId()){
            case R.id.history_radio_screen://ruutuaika
                if(checked)
                    buildGraphScreen();
                    break;
            case R.id.history_radio_sport://urheilu
                if(checked)
                    buildGraphSport();
                    break;
            case R.id.history_radio_weight://paino
                if(checked)
                    buildGraphWeight();
                    break;
        }
    }

    /**
     * Metodilla siirrytään informatiivisempaan historia näkymään, jossa näkyy halutun aikavälin
     * kaikki datat erikseen.
     */
    public void goInformative(View v){
        Intent intent = new Intent(getApplicationContext(),InformativeHistoryActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        intent.putExtra("historyList",json);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//siirrytään oikealle ->
    }

    /**
     * Metodilla siirrytään koti aktiviteettiin.
     */
    public void goMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    /**
     * Metodilla siirrytään profiili aktiviteettiin.
     */
    public void goProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//siirrytään oikealle ->
    }

    /**
     * Asettaa aktiviteetin päivämäärien asettelu view-elementtien toiminnot.
     */
    private void setDateListener(){
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);

        startDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    HistoryActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    startDateListener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        endDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    HistoryActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    endDateListener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        startDateListener = (datePicker, year, month, day) -> {
            month++; // Koska tammikuu = 0

            startY = year;
            startM = month;
            startD = day;
            String date=day+"."+month+"."+year;
            startDate.setText(date);
            getData();
        };

        endDateListener = (datePicker, year, month, day) -> {
            month++; // Koska tammikuu = 0

            endY = year;
            endM = month;
            endD = day;
            String date=day+"."+month+"."+year;
            endDate.setText(date);
            getData();
        };
    }

    /**
     * Metodi kutsuu HistoryManager.java luokan "getDayData" metodia, jolla saadaan halutun aikavälin kaikki tallennetut päivät olioina.
     */
    private void getData(){
        list = HM.getDayData(startY, startM, startD, endY, endM, endD);
        if (list!=null){
            size = list.size();
        }
        buildGraphWeight();
    }

    private void buildGraphWeight() {
        if (size > 0) {

            //Etsitään suurin paino hakuaikavälin päivistä
            double maxWeight = 50;
            for(int i = 0; i < size; i++){
                double tmpWeight = list.get(i).returnWeight();
                if(tmpWeight > maxWeight){
                    maxWeight=tmpWeight+5;
                }
            }

            GraphView graph = findViewById(R.id.Graph);
            graph.removeAllSeries();
            graph.getViewport().setScalable(true);

            //Määritellään "GraphView" elementin grafiikka sarja
            //Paino näytetään nollatasosta ylös nousevana palkkina "BarGraphSeries"
            painoSarja = new BarGraphSeries<>();


            for (int i = 0; i < size; i++) {
                double paino = list.get(i).returnWeight();
                painoSarja.appendData(new DataPoint(i, paino), true, size);
            }

            //Lisätään GraphView luokalle aikavälin painon arvot sille ymmärrettävässä muodossa
            graph.addSeries(painoSarja);

            //Varmistetaan että eka ja vika arvo näkyvät "kokonaisina" palkkeina
            graph.getViewport().setXAxisBoundsManual(true);
            {
                graph.getViewport().setMinX(painoSarja.getLowestValueX()-(1.0/2.0));
                graph.getViewport().setMaxX(painoSarja.getHighestValueX()+(1.0/2.0));
            }

            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                @Override
                public String formatLabel(double value, boolean isValueX){
                    if(isValueX){
                        return super.formatLabel(value, isValueX);
                    }
                    return super.formatLabel(value,isValueX)+"kg";
                }
            });
        }
    }

    private void buildGraphSport() {
        if (size > 0) {

            format = "sec";

            //Etsitään suurin paino hakuaikavälin päivistä
            int maxSport = 0;
            for(int i = 0; i < size; i++){
                int tmpSport = list.get(i).sportSec();
                if(tmpSport > maxSport){
                    maxSport=tmpSport+5;
                }
            }

            GraphView graph = findViewById(R.id.Graph);
            graph.removeAllSeries();
            graph.getViewport().setScalable(true);

            //Määritellään "GraphView" elementin grafiikka sarja
            //Paino näytetään nollatasosta ylös nousevana palkkina "BarGraphSeries"
            lineaarinenSarja = new LineGraphSeries<>();

            //Muutetaan urheiluarvot helpoiten luettavaan muotoon
            if(maxSport >= 3600){//Tunneissa
                for (int i = 0; i < size; i++) {
                    int sport = list.get(i).sportSec()/3600;
                    lineaarinenSarja.appendData(new DataPoint(i, sport), true, size);
                    format = "h";
                }
            } else if (maxSport >= 60){//Minuuteissa
                for (int i = 0; i < size; i++) {
                    int sport = list.get(i).sportSec()/60;
                    lineaarinenSarja.appendData(new DataPoint(i, sport), true, size);
                    format = "min";
                }
            } else {//Sekunneissa
                for (int i = 0; i < size; i++) {
                    int sport = list.get(i).sportSec();
                    lineaarinenSarja.appendData(new DataPoint(i, sport), true, size);
                }
            }
            //Lisätään GraphView luokalle aikavälin painon arvot sille ymmärrettävässä muodossa
            graph.addSeries(lineaarinenSarja);

            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                @Override
                public String formatLabel(double value, boolean isValueX){
                    if(isValueX){
                        return super.formatLabel(value, isValueX);
                    }
                    return super.formatLabel(value,isValueX)+format;
                }
            });
        }
    }

    private void buildGraphScreen() {
        if (size > 0) {

            format = "sec";

            //Etsitään suurin paino hakuaikavälin päivistä
            int maxScreen = 0;
            for(int i = 0; i < size; i++){
                int tmpSport = list.get(i).sportSec();
                if(tmpSport > maxScreen){
                    maxScreen=tmpSport+5;
                }
            }

            GraphView graph = findViewById(R.id.Graph);
            graph.removeAllSeries();
            graph.getViewport().setScalable(true);

            //Määritellään "GraphView" elementin grafiikka sarja
            //Paino näytetään nollatasosta ylös nousevana palkkina "BarGraphSeries"
            lineaarinenSarja = new LineGraphSeries<>();

            //Muutetaan ruutuarvot helpoiten luettavaan muotoon
            if(maxScreen >= 3600){//Tunneissa
                for (int i = 0; i < size; i++) {
                    int screen = list.get(i).screenSec()/3600;
                    lineaarinenSarja.appendData(new DataPoint(i, screen), true, size);
                    format="h";
                }
            } else if (maxScreen >= 60){//Minuuteissa
                for (int i = 0; i < size; i++) {
                    int screen = list.get(i).screenSec()/60;
                    lineaarinenSarja.appendData(new DataPoint(i, screen), true, size);
                    format="min";
                }
            } else {//Sekunneissa
                for (int i = 0; i < size; i++) {
                    int screen = list.get(i).screenSec();
                    lineaarinenSarja.appendData(new DataPoint(i, screen), true, size);
                }
            }
            //Lisätään GraphView luokalle aikavälin painon arvot sille ymmärrettävässä muodossa
            graph.addSeries(lineaarinenSarja);

            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                @Override
                public String formatLabel(double value, boolean isValueX){
                    if(isValueX){
                        return super.formatLabel(value, isValueX);
                    }
                    return super.formatLabel(value,isValueX)+format;
                }
            });
        }
    }
}