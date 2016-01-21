package com.emerssso.hats;

import android.app.IntentService;
import android.content.Intent;

import com.emerssso.hats.realm.models.Hat;
import com.emerssso.hats.realm.models.WearStart;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.emerssso.hats.HatsApplication.getApplicationComponent;
import static com.emerssso.hats.HatsIntents.EXTRA_HAT_NAME;

/**
 * Removes a hat and all wear times for that hat. Will NOT assume that null = "no hat", if "no hat"
 * needs to be deleted, it needs to be passed explicitly.
 */
public class RemoveHatDataIntentService extends IntentService {

    private static final String TAG = "RemoveHatData";

    @Inject Realm realm;

    public RemoveHatDataIntentService() {
        super(TAG);
    }

    @Override protected void onHandleIntent(Intent intent) {
        String hatName = intent.getStringExtra(EXTRA_HAT_NAME);
        getApplicationComponent(getApplication()).inject(this);

        if(hatName != null) {

            Hat hat = realm.where(Hat.class).equalTo(Hat.NAME, hatName).findFirst();

            //then, remove hat
            if (hat != null) {
                realm.beginTransaction();

                //remove all WearStarts for Hat
                RealmResults<WearStart> starts = realm.where(WearStart.class)
                        .equalTo("hat.name", hatName)
                        .findAll();

                starts.clear();

                hat.removeFromRealm();

                realm.commitTransaction();
            }

            realm.close();
        }
    }
}
