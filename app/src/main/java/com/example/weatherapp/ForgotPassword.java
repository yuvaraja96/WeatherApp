package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("ALL")
public class ForgotPassword extends AppCompatActivity {
    private EditText email;
    private Button resetPass;
    private TextView backLogin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.txtEmail);
        resetPass = findViewById(R.id.btnChangePass);
        backLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String uEmail = email.getText().toString().trim();
                if (uEmail.equals("")) {
                    email.setError("Email required.");
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    firebaseAuth.sendPasswordResetEmail(uEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "Email sent for password reset.", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(ForgotPassword.this, "Email not sent. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

