package com.emerssso.hats;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HatsApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder(this).name("hats.realm").build());
    }
}
