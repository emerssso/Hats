package com.emerssso.hats.realm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.emerssso.hats.realm.models.Hat;
import com.emerssso.hats.realm.models.WearStart;

import java.io.Closeable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Wrap Realm usage so that we can unit test consumers in Robolectric.
 */
public class RealmWrapper implements Closeable {
    private Realm realm;

    public RealmWrapper() {
        realm = Realm.getDefaultInstance();
    }

    public <E extends RealmObject> E copyToRealmOrUpdate(E realmObject) {
        realm.beginTransaction();

        E newRealmObject = realm.copyToRealmOrUpdate(realmObject);

        realm.commitTransaction();

        return newRealmObject;
    }

    @Nullable public Hat getHatWithName(String name) {
        return realm.where(Hat.class).equalTo(Hat.NAME, name).findFirst();
    }

    public void removeHat(@NonNull String hatName) {
        Hat hat = getHatWithName(hatName);

        //then, remove hat
        if(hat != null) {
            realm.beginTransaction();

            //remove all WearStarts for Hat
            RealmResults<WearStart> starts = realm.where(WearStart.class)
                    .equalTo("hat.name", hatName)
                    .findAll();

            starts.clear();

            hat.removeFromRealm();

            realm.commitTransaction();
        }
    }

    @Override public void close() {
        realm.close();
    }
}
