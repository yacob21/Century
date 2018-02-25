package com.example.user.century;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.user.century.GuestLoginActivity.GuestActivity;
import com.example.user.century.GuestLoginActivity.SessionLokasi;
import com.example.user.century.Home.MainActivity;
import com.example.user.century.Session.SessionManagement;

import java.util.HashMap;
import java.util.List;

public class Splash extends AppCompatActivity {

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashTread;
    SessionManagement sessionManagement;
    SessionLokasi sessionLokasi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        StartAnimations();

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Animation rotate;
        Animation atas;
        anim.reset();
        RelativeLayout l = (RelativeLayout) findViewById(R.id.lin_lay);
//        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        atas = AnimationUtils.loadAnimation(this, R.anim.translateatas);
        //anim.reset();


        ImageView iv = (ImageView) findViewById(R.id.splash);
        ImageView judul = (ImageView) findViewById(R.id.judul);
        ImageView subjudul = (ImageView) findViewById(R.id.subjudul);

        iv.clearAnimation();
        judul.clearAnimation();
        subjudul.clearAnimation();
        iv.startAnimation(rotate);


        judul.setAnimation(atas);
        subjudul.setAnimation(anim);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        else {
            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        // Splash screen pause time
                        while (waited < 3500) {
                            sleep(100);
                            waited += 100;
                        }
                            Intent intent = new Intent(Splash.this, GuestActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        Splash.this.finish();
                    } catch (InterruptedException e) {
                        // do nothing
                    } finally {
                        Splash.this.finish();
                    }

                }
            };
            splashTread.start();
        }

    }

}