package com.example.user.century.Session;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by yacob on 9/9/2017.
 */

public class SessionKategori {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editorKategori;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME_KATEGORI = "YacobPref";

    // All Shared Preferences Keys
    private static final String IS_KATEGORI = "IsLoggedIn";

    public static final String KEY_KATEGORI= "kategori";


    // Constructor
    public SessionKategori(Context context) {

        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME_KATEGORI, PRIVATE_MODE);
        editorKategori = pref.edit();
    }


    public void createKategori(String kategori){
        // Storing login value as TRUE
        editorKategori.putBoolean(IS_KATEGORI, true);
        // Storing name in pref
        editorKategori.putString(KEY_KATEGORI, kategori);
        // commit changes
        editorKategori.commit();
    }


    public HashMap<String, String> getKategoriDetails(){
        HashMap<String, String> kategori = new HashMap<String, String>();
        // user name
        kategori.put(KEY_KATEGORI, pref.getString(KEY_KATEGORI, null));

        // return user
        return kategori;
    }


}