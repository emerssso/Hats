package com.emerssso.hats;

import android.content.Intent;

import com.emerssso.hats.realm.RealmWrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class RemoveHatDataIntentServiceTests {

    public static final String HAT_NAME = "test";
    RemoveHatDataIntentService sut;
    @Mock
    RealmWrapper wrapper;

    @Before public void setUp() {
        initMocks(this);

        sut = Robolectric.buildService(RemoveHatDataIntentService.class).create().attach().get();

        sut.setWrapper(wrapper);
    }

    @Test public void shouldRemoveHatIfPassed() {
        Intent intent = new Intent();
        intent.putExtra(HatsIntents.EXTRA_HAT_NAME, HAT_NAME);

        sut.onHandleIntent(intent);

        verify(wrapper).removeHat(HAT_NAME);
    }

    @Test public void shouldDoNOthingIfNoNamePassed() {
        Intent intent = new Intent();

        sut.onHandleIntent(intent);

        verify(wrapper, never()).removeHat(anyString());
    }
}
