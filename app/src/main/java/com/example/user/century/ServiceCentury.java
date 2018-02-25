package com.example.user.century;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

public class ServiceCentury extends Service {
    public ServiceCentury() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        return START_STICKY;
    }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        // TODO Auto-generated method stub
//        Intent restartService = new Intent(getApplicationContext(),
//                this.getClass());
//        restartService.setPackage(getPackageName());
//        PendingIntent restartServicePI = PendingIntent.getService(
//                getApplicationContext(), 1, restartService,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        //Restart the service once it has been killed android
//
//
//        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);
//
//    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        //start a separate thread and start listening to your network object
    }

}
