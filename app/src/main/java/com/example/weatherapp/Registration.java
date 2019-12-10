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
public class Registration extends AppCompatActivity {

    private EditText name, email, pass;
    private CheckBox viewPass;
    private Button register;
    private TextView backLogin;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);
        pass = findViewById(R.id.txtPassword);
        viewPass = findViewById(R.id.chkPassword);
        register = findViewById(R.id.btnRegister);
        backLogin = findViewById(R.id.tvLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String uName = name.getText().toString().trim();
                String uEmail = email.getText().toString().trim();
                String uPass = pass.getText().toString().trim();

                //check for empty fields
                if(uName.isEmpty() || uEmail.isEmpty() || uPass. isEmpty()){
                    Toast.makeText(Registration.this, "Complete all fields.", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(uName)){
                    name.setError("Name required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

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

                if(pass.length() < 6){
                    pass.setError("Password must be >= 6 characters.");
                    progressBar.setVisibility(View.INVISIBLE);
                }



                progressBar.setVisibility(View.VISIBLE);

                //register the user in Firebase

                firebaseAuth.createUserWithEmailAndPassword(uEmail,uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            sendEmailVerification();
//                            Toast.makeText(Registration.this, "User Created.", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{

                            Toast.makeText(Registration.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

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

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Registration.this, "Email verification sent to your email.", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Registration.this, MainActivity.class));
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Registration.this, "Database is down. Please wait.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
