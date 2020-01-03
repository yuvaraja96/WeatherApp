package com.example.weatherapp;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("ALL")
public class Home extends AppCompatActivity {

    private final String CHANNEL_ID = "Weather Notifications";
    private final int NOTIFICATION_ID = 001;


    private Button farmTrack, menu, schedule;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        farmTrack = findViewById(R.id.btnFarmTrack);
        menu = findViewById(R.id.btnMenu);
        schedule = findViewById(R.id.btnSchedule);

        firebaseAuth = FirebaseAuth.getInstance();

        farmTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("http://192.168.8.108/");
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Catalog.class);
                startActivity(i);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Schedule.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (item.getItemId()) {

            case R.id.logoutMenu: {
                //AlertDialog to verify exit app
                builder.setMessage("Are you sure want to Logout?")
                        .setTitle("Logout")
                        .setIcon(R.drawable.ic_info)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do logout
                                firebaseAuth.signOut();
                                finish();
                                Intent intent = new Intent(Home.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            return true;

            case R.id.weatherAlert: {
                //Enable WeatherAlert
                builder.setMessage("Are you sure want to enable WeatherAlert?")
                        .setTitle("WeatherAlert")
                        .setIcon(R.drawable.ic_info)
                        .setCancelable(false)
                        .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Enabled WeatherAlert
                                Toast.makeText(Home.this, "WeatherAlert Enabled", Toast.LENGTH_SHORT).show();

                                //Firebase code for weather changes
                                ////////////////////////////////////

                                //Display Notifications
                                //if sudden changes in weather --> displayNotification()
                                displayNotification();

                            }
                        })

                        .setNegativeButton("Disable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(Home.this, "WeatherAlert Disabled", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //Notifications for Android 8.0 and below
    public void displayNotification() {

        //Specify the Android 8.0 and above
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("Weather Notifications");
        builder.setContentText("There is a change in weather");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    //Notifications for Android 8.0 and above
    public void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Weather Notifications";
            String description = "Include all the weather notifications";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

}
