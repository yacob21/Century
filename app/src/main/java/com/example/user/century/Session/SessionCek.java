package com.example.user.century.Session;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by yacob on 9/9/2017.
 */

public class SessionCek{
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editorCek;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME_CEK = "YacobPref";

    // All Shared Preferences Keys
    private static final String IS_CEK = "IsLoggedIn";

    public static final String KEY_CEK = "cek";


    // Constructor
    public SessionCek(Context context) {

        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME_CEK, PRIVATE_MODE);
        editorCek = pref.edit();
    }


    public void createCek(String cek){
        // Storing login value as TRUE
        editorCek.putBoolean(IS_CEK, true);
        // Storing name in pref
        editorCek.putString(KEY_CEK, cek);
        // commit changes
        editorCek.commit();
    }


    public HashMap<String, String> getCekDetails(){
        HashMap<String, String> cek = new HashMap<String, String>();
        // user name
        cek.put(KEY_CEK, pref.getString(KEY_CEK, null));

        // return user
        return cek;
    }


}