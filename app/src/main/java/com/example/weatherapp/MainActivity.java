package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private EditText email, pass;
    private CheckBox viewPass;
    private Button login;
    private TextView register, forgotPass;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        email = findViewById(R.id.txtEmail);
        pass = findViewById(R.id.txtPassword);
        viewPass = findViewById(R.id.chkPassword);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.tvRegister);
        forgotPass = findViewById(R.id.tvForgotPass);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(MainActivity.this, Home.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String uEmail = email.getText().toString().trim();
                String uPass = pass.getText().toString().trim();

                if(TextUtils.isEmpty(uEmail)){
                    email.setError("Email required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if(TextUtils.isEmpty(uPass)){
                    pass.setError("Password required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                check(email.getText().toString(),pass.getText().toString());

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        viewPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


    }

    private void check(String uName, String uPass){
        firebaseAuth.signInWithEmailAndPassword(uName, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    verifyEmail();
//                    Toast.makeText(MainActivity.this, "Login Successful.",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this,Home.class);
//                    startActivity(intent);

                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Login failed.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyEmail(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailVerify = firebaseUser.isEmailVerified();
        if(emailVerify){
            finish();
            startActivity(new Intent(MainActivity.this,Home.class));
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please verify your email.", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
