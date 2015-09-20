package com.emerssso.hats;

import android.content.Intent;

import static com.emerssso.hats.HatsIntents.EXTRA_HAT_NAME;

/**
 * Removes a hat and all wear times for that hat. Will NOT assume that null = "no hat", if "no hat"
 * needs to be deleted, it needs to be passed explicitly.
 */
public class RemoveHatDataIntentService extends RealmIntentService {

    public static final String TAG = "RemoveHatData";

    public RemoveHatDataIntentService() {
        super(TAG);
    }

    @Override protected void onHandleIntent(Intent intent) {
        String hatName = intent.getStringExtra(EXTRA_HAT_NAME);

        if(hatName != null) {
            getWrapper().removeHat(hatName);
        }
    }
}
