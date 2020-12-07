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
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;

import Classes.HistoryManager;

public class HistoryActivity extends AppCompatActivity {

    private HistoryManager HM;
    private int startY, startM, startD, endY, endM, endD;
    private ArrayList<dataOlio> list = new ArrayList<>();
    private LineGraphSeries<DataPoint> urheiluSarja,ruutuSarja;
    private BarGraphSeries<DataPoint> painoSarja;
    private int size;

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
        buildGraph();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    public void goMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//siirrytään vasemmalle <-
    }

    public void goProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//siirrytään oikealle ->
    }

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

    private void getData(){
        list = HM.getDayData(startY, startM, startD, endY, endM, endD);
        if (list!=null){
            size = list.size();
        }
        buildGraph();
    }

    private void buildGraph() {
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

            //Määritellään "GraphView" elementin grafiikka sarjat
            //Paino näytetään nollatasosta ylös nousevana palkkina "BarGraphSeries"
            //Uhrheiluaika taas näytetään lineaarisena viivana "LineGraphSeries"
            painoSarja = new BarGraphSeries<>();
            urheiluSarja = new LineGraphSeries<>();


            for (int i = 0; i < size; i++) {
                double paino = list.get(i).returnWeight();
                double y = 0 + Math.random() * 10;
                double y2 = 20 + Math.random() * 10;
                painoSarja.appendData(new DataPoint(i, paino), true, size);
                //urheiluSarja.appendData(new DataPoint(i,y2), true, 100);
            }

            //Lisätään GraphView luokalle aikavälin painon arvot sille ymmärrettävässä muodossa
            graph.addSeries(painoSarja);
            //graph.addSeries(urheiluSarja);
            graph.getSecondScale().addSeries(painoSarja);
            graph.getSecondScale().setMinY(0);

            //Asetetaan toisen skaalan vertikaalin maksimi pituudeksi "maxWeight" muuttujan arvo
            graph.getSecondScale().setMaxY(maxWeight);
            graph.getGridLabelRenderer().setNumHorizontalLabels(size);
        }
    }
}