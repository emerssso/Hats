package com.emerssso.hats.dagger;

import com.emerssso.hats.AddHatIntentService;
import com.emerssso.hats.MainActivity;
import com.emerssso.hats.RemoveHatDataIntentService;
import com.emerssso.hats.StartWearingHatIntentService;
import com.emerssso.hats.manage.ManageHatsFragment;

import dagger.Component;

/**
 * Application level/scope component
 */
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(AddHatIntentService service);

    void inject(MainActivity activity);

    void inject(ManageHatsFragment fragment);

    void inject(RemoveHatDataIntentService removeHatDataIntentService);

    void inject(StartWearingHatIntentService startWearingHatIntentService);
}
