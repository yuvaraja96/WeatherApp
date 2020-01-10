package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class Max extends AppCompatActivity {

    AnyChartView anyChartView;
    String[] days = {"Day1", "Day2", "Day3", "Day4", "Day5", "Day6", "Day7"};
    ArrayList<Double> myValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max);
        anyChartView = findViewById(R.id.anyChartView3);

        myValue.clear();
        myValue.add(0.3);
        myValue.add(0.3);
        myValue.add(0.4);
        myValue.add(0.2);
        myValue.add(0.2);
        myValue.add(0.4);
        myValue.add(0.5);
        String title = "Maximum Wind Speed (m/s)";
        setupPieChart(title);
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
