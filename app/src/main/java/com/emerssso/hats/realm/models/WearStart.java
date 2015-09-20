package com.emerssso.hats.realm.models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Represents a timestamp when a particular hat was put on (or the time at which the current hat was
 * removed).
 */
public class WearStart extends RealmObject {

    public static final String HAT = "hat";
    public static final String START = "start";

    private Hat hat;
    private Date start;

    //Needed for Realm. do not remove
    public WearStart() {
    }

    public WearStart(Hat hat, Date start) {
        this.start = start;
        this.hat = hat;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Hat getHat() {
        return hat;
    }

    public void setHat(Hat hat) {
        this.hat = hat;
    }
}
