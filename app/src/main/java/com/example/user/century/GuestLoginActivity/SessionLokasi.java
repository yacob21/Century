package com.example.user.century.GuestLoginActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.user.century.slider.FirstSlider;

import java.util.HashMap;

/**
 * Created by yacob on 9/9/2017.
 */

public class SessionLokasi {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editorLokasi;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME_LOKASI = "YacobPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN_LOKASI= "IsLoggedIn";

    public static final String KEY_ID_LOKASI= "id_lokasi";


    // Constructor
    public SessionLokasi(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME_LOKASI, PRIVATE_MODE);
        editorLokasi = pref.edit();
    }


    public void createLokasi(String id_lokasi){
        // Storing login value as TRUE
        editorLokasi.putBoolean(IS_LOGIN_LOKASI, true);
        // Storing name in pref
        editorLokasi.putString(KEY_ID_LOKASI, id_lokasi);
        // commit changes
        editorLokasi.commit();
    }


    public HashMap<String, String> getLokasiDetails(){
        HashMap<String, String> lokasi = new HashMap<String, String>();
        // user name
        lokasi.put(KEY_ID_LOKASI, pref.getString(KEY_ID_LOKASI, null));

        // return user
        return lokasi;
    }

    public void checkLokasi() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, FirstSlider.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN_LOKASI, false);
    }

    public void hapusLokasi() {
        // Clearing all data from Shared Preferences
        editorLokasi.clear();
        editorLokasi.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, FirstSlider.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

}