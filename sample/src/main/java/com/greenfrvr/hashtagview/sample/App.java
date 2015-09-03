package com.greenfrvr.hashtagview.sample;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by greenfrvr
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }
}
