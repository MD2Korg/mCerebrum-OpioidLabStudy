package org.md2k.mcerebrum.opioidlabstudy;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.md2k.mcerebrum.core.access.MCerebrum;

public class MyApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        MCerebrum.init(getApplicationContext(), MyMCerebrumInit.class);
        TypefaceProvider.registerDefaultIconSets();
    }
}