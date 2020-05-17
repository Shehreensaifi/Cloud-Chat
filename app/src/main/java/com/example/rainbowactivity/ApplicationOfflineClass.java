package com.example.rainbowactivity;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

public class ApplicationOfflineClass extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
}
