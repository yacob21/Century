package com.example.user.century.Session;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by yacob on 9/9/2017.
 */

public class SessionSearch {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editorSearch;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME_SEARCH = "YacobPref";

    // All Shared Preferences Keys
    private static final String IS_SEARCH = "IsLoggedIn";

    public static final String KEY_SEARCH= "search";


    // Constructor
    public SessionSearch(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME_SEARCH, PRIVATE_MODE);
        editorSearch = pref.edit();
    }


    public void createSearch(String search){
        // Storing login value as TRUE
        editorSearch.putBoolean(IS_SEARCH , true);
        // Storing name in pref
        editorSearch.putString(KEY_SEARCH, search);
        // commit changes
        editorSearch.commit();
    }


    public HashMap<String, String> getSearchDetails(){
        HashMap<String, String> search = new HashMap<String, String>();

        search.put(KEY_SEARCH, pref.getString(KEY_SEARCH, null));

        // return user
        return search;
    }



}