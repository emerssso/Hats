package com.emerssso.hats;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override public int getCount() {
                return 0;
            }

            @Override public boolean isViewFromObject(View view, Object object) {
                return false;
            }
        };
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
    }
}
