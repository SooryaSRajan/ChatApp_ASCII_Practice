package com.ssr_projects.asciichatapp.Models;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

public class LoginModel {

    private String email;
    private String password;
    private final Activity activity;

    public LoginModel(Activity activity) {
        this.activity = activity;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isModelValid() {
        boolean isValid = true;

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            if(email == null || email.isEmpty())
                Toast.makeText(activity, "Please fill email field", Toast.LENGTH_SHORT).show();

            if(password == null || password.isEmpty())
                Toast.makeText(activity, "Please fill password field", Toast.LENGTH_SHORT).show();

            isValid = false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(activity, "Invalid email id", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }

        return isValid;

    }
}
