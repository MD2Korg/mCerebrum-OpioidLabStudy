package org.md2k.mcerebrum.opioidlabstudy;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class MyApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}