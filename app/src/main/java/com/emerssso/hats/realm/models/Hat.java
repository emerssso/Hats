package com.emerssso.hats.realm.models;

import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Models a Hat, i.e. Manager, Developer, etc.
 */
@SuppressWarnings("unused")
public class Hat extends RealmObject {
    public static final String NAME = "name";
    public static final String NO_HAT_NAME = "none"; //needed b/c Realm does not support null values

    @NonNull @PrimaryKey private String name;

    //Needed for Realm. Do not remove
    public Hat() {
        name = NO_HAT_NAME;
    }

    public Hat(@NonNull String name) {
        this.name = name;
    }

    @NonNull public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
