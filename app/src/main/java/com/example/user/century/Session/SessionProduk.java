package com.example.user.century.Session;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by yacob on 9/9/2017.
 */

public class SessionProduk {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editorProduk;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME_PRODUK = "YacobPref";

    // All Shared Preferences Keys
    private static final String IS_PRODUK= "IsLoggedIn";

    public static final String KEY_ID= "id_produk";


    // Constructor
    public SessionProduk(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME_PRODUK, PRIVATE_MODE);
        editorProduk = pref.edit();
    }


    public void createProduk(String id_produk){
        // Storing login value as TRUE
        editorProduk.putBoolean(IS_PRODUK, true);
        // Storing name in pref
        editorProduk.putString(KEY_ID, id_produk);
        // commit changes
        editorProduk.commit();
    }


    public HashMap<String, String> getProdukDetails(){
        HashMap<String, String> produk = new HashMap<String, String>();
        // user name
        produk.put(KEY_ID, pref.getString(KEY_ID, null));

        // return user
        return produk;
    }



}