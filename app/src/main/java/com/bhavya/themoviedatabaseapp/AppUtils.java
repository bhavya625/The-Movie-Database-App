package com.bhavya.themoviedatabaseapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bhavya on 09/06/2017.
 */

public final class AppUtils {

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        //Checking Network Connectivity
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
