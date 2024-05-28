package com.example.keepx;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressbar = findViewById(R.id.progressBar);
        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signupText);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.setVisibility(View.GONE);
                loginUserAccount(email.getText().toString(), password.getText().toString(), progressbar);
                signup.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
            }
        });
    }
    private void loginUserAccount(String email1, String password1, ProgressBar progressbar1)
    {

        // show the visibility of progress bar to show loading
        progressbar1.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(getApplicationContext(), "Please provide email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(getApplicationContext(), "Please provide password...", Toast.LENGTH_LONG).show();
            return;
        }
        // signin existing user
        mAuth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Loged in successfully!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar1.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent
                                            = new Intent(MainActivity.this,
                                            MainNav.class);
                                    startActivity(intent);
                                    finish();
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed. Please try again",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }
}