package com.sunnygroup.backoffice.sunboxwebview;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebAppInterface {
    private MainActivity mainActivity;

    /** Instantiate the interface and set the context */
    WebAppInterface(MainActivity m) {
        mainActivity = m;
    }

    @JavascriptInterface
    public String getStoredUsername() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mainActivity.getApplicationContext());
        return sharedPref.getString(Constants.USERNAME, null);
    }

    @JavascriptInterface
    public String getStoredPassword() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mainActivity.getApplicationContext());
        return sharedPref.getString(Constants.PASSWORD, null);
    }

    @JavascriptInterface
    public void logout() {
        Toast.makeText(mainActivity, "You have been logged out.", Toast.LENGTH_LONG).show();
        mainActivity.removeCredentials();
        mainActivity.finish();
    }

}