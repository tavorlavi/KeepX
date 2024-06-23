package com.example.keepx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    private EditText email, password, confirmpassword;
    private TextView login;
    private ProgressBar progressbar;
    private Button signup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.regpassword);
        confirmpassword = findViewById(R.id.regconfirmpassword);
        signup = findViewById(R.id.regButton);
        login = findViewById(R.id.signupText);
        mAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.regprogressBar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.GONE);
                registerNewUser(email.getText().toString(), password.getText().toString(), confirmpassword.getText().toString(), progressbar);
                login.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
            }
        });


    }
    private void registerNewUser(String email1, String password1, String rep, ProgressBar progressBar1)
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings

        // Validations for input email and password
        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(getApplicationContext(),
                            "Please provide email",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(getApplicationContext(),
                            "Please provide password",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (password1.length() < 6) {
            Toast.makeText(getApplicationContext(),
                            "Password too short, enter minimum 6 characters",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email1).matches()) {
            Toast.makeText(getApplicationContext(),
                            "Please provide valid email",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (!password1.equals(rep)) {
            Toast.makeText(getApplicationContext(),
                            "Passwords do not match",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                            "Registration successful!",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressBar1.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(Signup.this,
                                    MainNav.class);
                            startActivity(intent);
                            intent.putExtra("source", "good");
                            finish();
                        }
                        else {

                            // Registration failed
                            Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressBar1.setVisibility(View.GONE);
                        }
                    }
                });
    }
}