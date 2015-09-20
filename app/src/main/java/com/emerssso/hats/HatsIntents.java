package com.emerssso.hats;

/**
 * Implicit intent-filters, extras, actions, etc. related to Hats
 */
public class HatsIntents {
    /**
     * Required extra for hat name. If not included, "no hat" status will be used.
     */
    public static final String EXTRA_HAT_NAME = "com.emerssso.hats.extras.EXTRA_HAT_NAME";

    /**
     * Extra constant for epoch millis for time to start wearing hat.
     * Do not set if System.currentTimeMillis() is acceptable.
     * NOTE: This may be an arbitrarily long time after the intent is set.
     */
    public static final String EXTRA_START_MILLIS = "com.emerssso.hats.extras.START_MILLIS";
}
