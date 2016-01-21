package com.emerssso.hats.dagger;

import com.emerssso.hats.HatsApplication;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Application level/scope module
 */
@Module public class ApplicationModule {
    HatsApplication application;

    public ApplicationModule(HatsApplication application) {
        this.application = application;
    }

    @Provides public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
