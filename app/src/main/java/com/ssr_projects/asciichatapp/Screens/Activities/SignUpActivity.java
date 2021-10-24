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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssr_projects.asciichatapp.Models.SignUpModel;
import com.ssr_projects.asciichatapp.R;

import java.util.Objects;

import static com.ssr_projects.asciichatapp.Utils.Constants.USER_NAME_NODE;
import static com.ssr_projects.asciichatapp.Utils.Constants.USER_NODE;

public class SignUpActivity extends AppCompatActivity {

    private SignUpModel signUpModel;
    private EditText userName;
    private EditText email;
    private EditText password;
    private EditText passwordRepeat;
    private FirebaseAuth mAuth;
    private final String TAG = getClass().getName();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Signup");

        initialiseWidgets();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        signUpModel = new SignUpModel(this);
        mAuth = FirebaseAuth.getInstance();

    }

    private void initialiseWidgets() {
        userName = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordRepeat = findViewById(R.id.password_repeat);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    public void Signup(View view) {
        signUpModel.setName(userName.getText().toString());
        signUpModel.setEmail(email.getText().toString());
        signUpModel.setPassword(password.getText().toString());
        signUpModel.setPasswordRepeat(passwordRepeat.getText().toString());

        if (signUpModel.isModelValid()) {
            authenticateUserSignUp(signUpModel.getEmail(), signUpModel.getPassword(), signUpModel.getName());
        }

    }

    private void authenticateUserSignUp(String email, String password, String userName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            databaseReference
                                    .child(USER_NODE)
                                    .child(user.getUid())
                                    .child(USER_NAME_NODE)
                                    .setValue(userName)
                                    .addOnCompleteListener(task1 -> Log.d(TAG, "authenticateUserSignUp: Successfully written to database"))
                                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e));

                            Toast.makeText(this, "Account Creation Successful!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "authenticateUserSignup: " + user.getEmail() + " " + user.getUid());

                            Intent intent = new Intent(this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Signup failed, exception: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}