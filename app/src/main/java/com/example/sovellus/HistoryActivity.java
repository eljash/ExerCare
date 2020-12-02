package com.example.sovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> urheiluSarja,ruutuSarja;
    private BarGraphSeries<DataPoint> painoSarja;

    private TextView startDate;
    private TextView endDate;
    private DatePickerDialog.OnDateSetListener startDateListener,endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setDateListener();
        buildGraph();
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

            String date=day+"."+month+"."+year;
            startDate.setText(date);
            getData();
        };

        endDateListener = (datePicker, year, month, day) -> {
            month++; // Koska tammikuu = 0

            String date=day+"."+month+"."+year;
            endDate.setText(date);
            getData();
        };
    }

    private void getData(){
        buildGraph();
    }

    private void buildGraph(){
        GraphView graph = findViewById(R.id.Graph);
        graph.removeAllSeries();
        painoSarja = new BarGraphSeries<>();

        int num = 31;
        for(int i = 0; i < num; i++){
            double y = 60 + Math.random()*10;
            painoSarja.appendData(new DataPoint(i,y), true , 100);
        }
        graph.addSeries(painoSarja);
    }
}