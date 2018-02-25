package com.example.user.century;

import android.app.Application;
import android.content.Intent;

/**
 * Created by Administrator on 12/6/2017.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, ServiceCentury.class));
    }
}