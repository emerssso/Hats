package com.emerssso.hats.realm;

import android.app.IntentService;

/**
 * Custom child of IntentService that encapsulates Realm so that we can unit test business logic
 */
public abstract class RealmIntentService extends IntentService {
    private RealmWrapper wrapper;

    public RealmIntentService(String name) {
        super(name);
    }

    @Override public void onDestroy() {
        super.onDestroy();

        if(wrapper != null) {
            wrapper.close();
        }
    }

    protected RealmWrapper getWrapper() {
        if(wrapper == null) {
            wrapper = new RealmWrapper();
        }

        return wrapper;
    }

    public void setWrapper(RealmWrapper realmWrapper) {
        wrapper = realmWrapper;
    }
}
