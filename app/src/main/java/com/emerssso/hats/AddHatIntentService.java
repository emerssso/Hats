package com.emerssso.hats;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.emerssso.hats.realm.models.Hat;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import io.realm.Realm;

import static com.emerssso.hats.HatsApplication.getApplicationComponent;
import static com.emerssso.hats.HatsIntents.EXTRA_HAT_NAME;

/**
 * Intent service to add a hat. Call explicitly with EXTRA_HAT_NAME extra included with name of new
 * hat.
 */
public class AddHatIntentService extends IntentService {
    private static final String TAG = "AddHatService";

    @Inject Realm realm;

    public AddHatIntentService() {
        super(TAG);
    }

    @Override protected void onHandleIntent(Intent intent) {
        getApplicationComponent(getApplication()).inject(this);

        String hatName = intent.getStringExtra(EXTRA_HAT_NAME);

        if(StringUtils.isBlank(hatName)) {
            hatName = Hat.NO_HAT_NAME;
        }

        Hat hat = new Hat(hatName);

        realm.beginTransaction();

        realm.copyToRealmOrUpdate(hat);

        realm.commitTransaction();

        realm.close();
        Log.d(TAG, hatName + " hat created");
    }
}
