package com.cryptocallback.cryptocallback.OneSignalAlerts;

import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;

/**
 * Created by User on 4/7/2018.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
        //MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.
        OneSignal.startInit(this)
                .setNotificationReceivedHandler( new MyNotificationReceiver() )
                .init();
    }

}
