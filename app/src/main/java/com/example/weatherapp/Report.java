package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;

public class Report extends AppCompatActivity {

    private TextView highTemp;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        highTemp = findViewById(R.id.highTemp);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("201911041600");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double temp = 0;

                //DataSnapshot windSpeed = dataSnapshot.child("WindSpeed");
                //Iterable<DataSnapshot> avgWind = windSpeed.getChildren();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object temperature = map.get("Temp");

                    String s = String.valueOf(temperature);
                    double tempValue = Double.parseDouble(s.substring(0, 4));

                    if (tempValue > temp) {
                        temp = tempValue;
                        highTemp.setText(tempValue + " \u2103");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
