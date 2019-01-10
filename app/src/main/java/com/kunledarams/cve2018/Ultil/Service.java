package com.kunledarams.cve2018.Ultil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Service {

    Context context;

    public Service(Context context) {
        this.context = context;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
