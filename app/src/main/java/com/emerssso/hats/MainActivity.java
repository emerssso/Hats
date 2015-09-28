package com.emerssso.hats;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Main activity for the app that uses tabbed navigation
 */
public class MainActivity extends AppCompatActivity {

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            ManageHatsFragment manageHatsFragment = new ManageHatsFragment();
            HatHistoryFragment historyFragment = new HatHistoryFragment();

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
}
