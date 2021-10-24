package com.ssr_projects.asciichatapp.Screens.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ssr_projects.asciichatapp.Models.LoginModel;
import com.ssr_projects.asciichatapp.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private LoginModel loginModel;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        initialiseWidgets();

        loginModel = new LoginModel(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    private void initialiseWidgets() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void Login(View view) {
        loginModel.setEmail(email.getText().toString());
        loginModel.setPassword(password.getText().toString());

        if (loginModel.isModelValid()) {
            authenticateUserLogin(loginModel.getEmail(), loginModel.getPassword());
        }

    }

    private void authenticateUserLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "authenticateUserLogin: " + user.getEmail() + " " + user.getUid());

                            Intent intent = new Intent(this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Login failed, exception: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}