package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("ALL")
public class Home extends AppCompatActivity {

    private Button farmTrack;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        farmTrack = findViewById(R.id.btnFarmTrack);
        firebaseAuth = FirebaseAuth.getInstance();

        farmTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openUrl("http://192.168.8.108/");
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

        switch(item.getItemId()){

            case R.id.logoutMenu:{
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
                                Intent intent = new Intent(Home.this,MainActivity.class);
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

            case R.id.weatherAlert:{
                //Enable WeatherAlert
                builder.setMessage("Are you sure want to enable WeatherAlert?")
                        .setTitle("WeatherAlert")
                        .setIcon(R.drawable.ic_info)
                        .setCancelable(false)
                        .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do enable WeatherAlert
                                Toast.makeText(Home.this, "WeatherAlert Enabled",Toast.LENGTH_SHORT).show();

                                //Firebase code for weather changes
                                ////////////////////////////////////

                            }
                        })

                        .setNegativeButton("Disable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(Home.this, "WeatherAlert Disabled",Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openUrl(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
