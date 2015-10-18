package com.emerssso.hats;

import com.emerssso.hats.realm.models.Hat;
import com.emerssso.hats.realm.models.WearStart;

import io.realm.RealmResults;

/**
 * Interface used by fragments to get data from MainActivity
 */
public interface DataProvider {
    RealmResults<Hat> getAllHats();

    RealmResults<WearStart> getAllWearStarts();

    Hat getCurrentHat();
}
