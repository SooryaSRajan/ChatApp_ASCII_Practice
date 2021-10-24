package com.ssr_projects.asciichatapp.Models;

import android.app.Activity;
import android.util.Patterns;
import android.widget.Toast;

public class SignUpModel {

    private String name;
    private String email;
    private String password;
    private String passwordRepeat;
    private final Activity activity;

    public SignUpModel(Activity activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public boolean isModelValid() {
        boolean isValid = true;

        if (name == null || email == null || password == null || passwordRepeat == null
                || name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {

            if (name == null || name.isEmpty())
                Toast.makeText(activity, "Please fill name field", Toast.LENGTH_SHORT).show();

            if (email == null || email.isEmpty())
                Toast.makeText(activity, "Please fill email field", Toast.LENGTH_SHORT).show();

            if (password == null || password.isEmpty())
                Toast.makeText(activity, "Please fill password field", Toast.LENGTH_SHORT).show();

            if (passwordRepeat == null || passwordRepeat.isEmpty())
                Toast.makeText(activity, "Please fill second password field", Toast.LENGTH_SHORT).show();

            isValid = false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(activity, "Invalid email id", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (name.length() < 4) {
                Toast.makeText(activity, "Username must be at least four characters long", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (!password.equals(passwordRepeat)) {
                Toast.makeText(activity, "Passwords don't match", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }

        return isValid;

    }
}
