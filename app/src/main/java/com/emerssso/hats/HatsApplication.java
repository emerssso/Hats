package com.emerssso.hats;

import android.app.Application;
import android.support.annotation.NonNull;

import com.emerssso.hats.dagger.ApplicationComponent;
import com.emerssso.hats.dagger.ApplicationModule;
import com.emerssso.hats.dagger.DaggerApplicationComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HatsApplication extends Application {
    private ApplicationComponent component;

    @NonNull public static ApplicationComponent getApplicationComponent(Application app) {
        return ((HatsApplication) app).getApplicationComponent();
    }

    @Override public void onCreate() {
        super.onCreate();
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder(this).name("hats.realm").build());

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return component;
    }
}
