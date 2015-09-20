package com.emerssso.hats;

import android.content.Intent;

import com.emerssso.hats.realm.models.Hat;
import com.emerssso.hats.realm.RealmIntentService;

import org.apache.commons.lang3.StringUtils;

import static com.emerssso.hats.HatsIntents.EXTRA_HAT_NAME;

/**
 * Intent service to add a hat. Call explicitly with EXTRA_HAT_NAME extra included with name of new
 * hat.
 */
public class AddHatIntentService extends RealmIntentService {
    public static final String TAG = "AddHatService";

    public AddHatIntentService() {
        super(TAG);
    }

    @Override protected void onHandleIntent(Intent intent) {
        String hatName = intent.getStringExtra(EXTRA_HAT_NAME);

        if(StringUtils.isBlank(hatName)) {
            hatName = Hat.NO_HAT_NAME;
        }

        Hat hat = new Hat(hatName);

        getWrapper().copyToRealmOrUpdate(hat);
    }
}
