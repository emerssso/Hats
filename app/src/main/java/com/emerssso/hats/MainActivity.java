package com.emerssso.hats;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.emerssso.hats.realm.models.Hat;
import com.emerssso.hats.realm.models.WearStart;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Main activity for the app that uses tabbed navigation
 */
public class MainActivity extends AppCompatActivity
        implements ManageHatsFragment.Callbacks, DataProvider {

    private static final String TAG = "MainActivity";
    ManageHatsFragment manageHatsFragment;
    HatHistoryFragment historyFragment;
    private Realm realm;

    public RealmResults<Hat> getAllHats() {
        //TODO: take this offline or use RealmAdapter
        long start = SystemClock.currentThreadTimeMillis();
        RealmResults<Hat> hats = realm.where(Hat.class).findAll();
        long end = SystemClock.currentThreadTimeMillis();
        Log.d(TAG, "online realm query took " + (end - start) + " millis");
        return hats;
    }

    @Override public RealmResults<WearStart> getAllWearStarts() {
        //TODO: take this offline or use RealmAdapter
        long start = SystemClock.currentThreadTimeMillis();
        RealmResults<WearStart> starts = realm.where(WearStart.class)
                .findAllSorted(WearStart.START, false);
        long end = SystemClock.currentThreadTimeMillis();
        Log.d(TAG, "online realm query took " + (end - start) + " millis");
        return starts;
    }

    @Override public Hat getCurrentHat() {
        return getAllWearStarts().first().getHat();
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        manageHatsFragment = new ManageHatsFragment();
        historyFragment = new HatHistoryFragment();

        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override public int getCount() {
                return 2;
            }

            @Override public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.fragment_title_hats);
                    case 1:
                        return getString(R.string.fragment_title_history);
                    default:
                        return getString(R.string.blank);
                }
            }

            @Override public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return manageHatsFragment;
                    case 1:
                        return historyFragment;
                    default:
                        return null;
                }
            }
        };
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override public void saveWithSnackbar(String name) {
        manageHatsFragment.saveWithSnackbar(name);
    }
}
