package com.emerssso.hats;

import android.content.Intent;

import com.emerssso.hats.realm.models.Hat;
import com.emerssso.hats.realm.models.WearStart;
import com.emerssso.hats.realm.RealmWrapper;

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
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class StartWearingHatIntentServiceTests {

    public static final long START_MILLIS = 1442780440545L;
    public static final String HAT_NAME = "test";
    StartWearingHatIntentService sut;
    @Mock
    RealmWrapper wrapper;
    @Captor ArgumentCaptor<WearStart> startCaptor;
    Hat hat;

    @Before public void setUp() {
        initMocks(this);

        hat = new Hat(HAT_NAME);

        sut = Robolectric.buildService(StartWearingHatIntentService.class).create().attach().get();
        sut.setWrapper(wrapper);
    }

    @Test public void shouldInsertWearStartWithHatNameAndPassedTime() {
        Intent intent = new Intent(RuntimeEnvironment.application, StartWearingHatIntentService.class);
        intent.putExtra(HatsIntents.EXTRA_HAT_NAME, HAT_NAME);
        intent.putExtra(HatsIntents.EXTRA_START_MILLIS, START_MILLIS);

        when(wrapper.getHatWithName(HAT_NAME)).thenReturn(hat);

        sut.onHandleIntent(intent);

        verify(wrapper).copyToRealmOrUpdate(startCaptor.capture());
        WearStart start = startCaptor.getValue();
        assertEquals(HAT_NAME, start.getHat().getName());
        assertEquals(START_MILLIS, start.getStart().getTime());
    }

    @Test public void shouldInsertWearStartWithoutHatName() {
        Intent intent = new Intent(RuntimeEnvironment.application, StartWearingHatIntentService.class);
        intent.putExtra(HatsIntents.EXTRA_START_MILLIS, START_MILLIS);

        hat = new Hat(Hat.NO_HAT_NAME);

        when(wrapper.getHatWithName(Hat.NO_HAT_NAME)).thenReturn(hat);

        sut.onHandleIntent(intent);

        verify(wrapper).copyToRealmOrUpdate(startCaptor.capture());
        WearStart start = startCaptor.getValue();
        assertEquals(Hat.NO_HAT_NAME, start.getHat().getName());
        assertEquals(START_MILLIS, start.getStart().getTime());
    }
}
