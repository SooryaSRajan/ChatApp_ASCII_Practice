package com.ssr_projects.asciichatapp.Utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String getCurrentDate(){
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy");
        Date dateObject = new Date();
        return dateFormatter.format(dateObject);
    }

    public static String getCurrentTime(){
        @SuppressLint("SimpleDateFormat")
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        Date timeObject = new Date();
        return timeFormatter.format(timeObject);
    }
}
