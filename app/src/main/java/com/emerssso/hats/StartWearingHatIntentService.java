package com.emerssso.hats;

import android.content.Intent;

import com.emerssso.hats.models.Hat;
import com.emerssso.hats.models.WearStart;

import java.util.Date;

import static com.emerssso.hats.HatsIntents.EXTRA_HAT_NAME;
import static com.emerssso.hats.HatsIntents.EXTRA_START_MILLIS;

/**
 * Intent service that inserts a passed timestamp at which the passed hat was put on.
 */
public class StartWearingHatIntentService extends RealmIntentService {

    public static final String TAG = "StartWearingHat";

    public StartWearingHatIntentService() {
        super(TAG);
        setIntentRedelivery(true);
    }

    @Override protected void onHandleIntent(Intent intent) {
        String hatName = intent.getStringExtra(EXTRA_HAT_NAME);
        long startMillis = intent.getLongExtra(EXTRA_START_MILLIS, -1);

        if(hatName == null) {
            hatName = Hat.NO_HAT_NAME;
        }

        if(startMillis == -1) {
            startMillis = System.currentTimeMillis();
        }

        Hat hat = getWrapper().getHatWithName(hatName);

        WearStart start = new WearStart(hat, new Date(startMillis));

        getWrapper().copyToRealmOrUpdate(start);
    }

}
