package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Report extends AppCompatActivity {

    private DatabaseReference mDatabase;

    AnyChartView anyChartView;
    String[] days = {"Day1", "Day2", "Day3", "Day4", "Day5", "Day6", "Day7"};
    ArrayList<Double> myValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        anyChartView = findViewById(R.id.anyChartView);

        /*String[] arraySpinner = new String[]{
                "Temperature", "Min Wind Speed", "Max Wind Speed", "Avg Wind Speed"
        };*/

        myValue.clear();
        myValue.add(28.2);
        myValue.add(29.7);
        myValue.add(26.5);
        myValue.add(31.4);
        myValue.add(27.5);
        myValue.add(24.7);
        myValue.add(22.2);
        String title = "Temperature in Celsius " + "\u2103";
        setupPieChart(title);

        /*Spinner s = findViewById(R.id.choiceSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                if(text.equals("Temperature")){ */
        for (int i = 3; i < 10; i++) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("2019110" + i + "1600");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double temp = 0;
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object temperature = map.get("Temp");


                        String s = String.valueOf(temperature);
                        double tempValue = Double.parseDouble(s.substring(0, 4));

                        if (tempValue > temp) {
                            temp = tempValue;
                        }
                        count++;

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setupPieChart(String title) {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            dataEntries.add(new ValueDataEntry(days[i], myValue.get(i)));
        }

        pie.data(dataEntries);
        pie.title(title);
        anyChartView.setChart(pie);
    }
}
