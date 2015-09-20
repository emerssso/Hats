package com.emerssso.hats;

import android.content.Intent;

import com.emerssso.hats.models.Hat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AddHatIntentServiceTests {

    public static final String HAT_NAME = "test";
    AddHatIntentService sut;
    @Mock RealmWrapper wrapper;
    @Captor ArgumentCaptor<Hat> hatCaptor;

    @Before public void setUp() {
        initMocks(this);

        sut = Robolectric.buildService(AddHatIntentService.class).create().attach().get();

        sut.setWrapper(wrapper);
    }

    @Test public void shouldCloseRealmOnDestroy() {
        sut.onDestroy();

        verify(wrapper).close();
    }

    @Test public void shouldInsertNewHatWithPassedName() {
        Intent intent = new Intent(RuntimeEnvironment.application, AddHatIntentService.class);
        intent.putExtra(HatsIntents.EXTRA_HAT_NAME, HAT_NAME);

        sut.onHandleIntent(intent);

        verify(wrapper).copyToRealmOrUpdate(hatCaptor.capture());

        assertEquals(HAT_NAME, hatCaptor.getValue().getName());
    }

    @Test public void shouldInsertNoHatIfNoNameInExtra() {
        Intent intent = new Intent(RuntimeEnvironment.application, AddHatIntentService.class);

        sut.onHandleIntent(intent);

        verify(wrapper).copyToRealmOrUpdate(hatCaptor.capture());

        assertEquals(Hat.NO_HAT_NAME, hatCaptor.getValue().getName());
    }
}