package com.example.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Schedule extends AppCompatActivity {

    private static final String TAG = "Schedule";
    private static final int STORAGE_CODE = 1000;
    DatabaseReference reff;
    TextView windDirection, windSpeed, humidity, temperature, airPressure;
    Button btnReport;
    private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mCalendarView = findViewById(R.id.calendarView);
        windDirection = findViewById(R.id.wdTv);
        windSpeed = findViewById(R.id.wsTv);
        humidity = findViewById(R.id.humidityTv);
        temperature = findViewById(R.id.tempTv);
        airPressure = findViewById(R.id.airTv);
        btnReport = findViewById(R.id.btnReport);

        /*btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        savePdf();
                    }
                } else {
                    savePdf();
                }
            }
        });*/

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Schedule.this, Report.class));
            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (year == 2019 && month == 10 && dayOfMonth == 4) {

                    reff = FirebaseDatabase.getInstance().getReference().child("201911041600").child("Second20");
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String wd = dataSnapshot.child("WindDirection").child("Avg").getValue().toString();
                            wd = wd.substring(0, 3);

                            String ws = dataSnapshot.child("WindSpeed").child("Avg").getValue().toString();
                            ws = ws.substring(0, 3);

                            String humi = dataSnapshot.child("Humidity").getValue().toString();
                            humi = humi.substring(0, 3);

                            String temp = dataSnapshot.child("Temp").getValue().toString();
                            temp = temp.substring(0, 3);

                            String airP = dataSnapshot.child("AirPressure").getValue().toString();
                            airP = airP.substring(0, 5);

                            windDirection.setText(wd + " degree");
                            windSpeed.setText(ws + " m/s");
                            humidity.setText(humi + " %RH");
                            temperature.setText(temp + " \u2109");
                            airPressure.setText(airP + " hPa");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    windDirection.setText("");
                    windSpeed.setText("");
                    humidity.setText("");
                    temperature.setText("");
                    airPressure.setText("");
                }
            }
        });

    }

    private void savePdf() {
        Document mDoc = new Document();

        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";

        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();

            String wdText = windDirection.getText().toString();
            String wsText = windSpeed.getText().toString();
            String tempText = temperature.getText().toString();
            String humiText = humidity.getText().toString();
            String airPress = airPressure.getText().toString();

            mDoc.add(new Paragraph("Weather Report"));
            mDoc.add(new Paragraph("Wind Direction: " + wdText));
            mDoc.add(new Paragraph("Wind Speed: " + wsText));
            mDoc.add(new Paragraph("Temperature: " + tempText));
            mDoc.add(new Paragraph("Humidity: " + humiText));
            mDoc.add(new Paragraph("Air Pressure: " + airPress));

            mDoc.close();

            Toast.makeText(this, mFileName + ".pdf\nis saved to\n" + mFilePath, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePdf();
                } else {
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
