package com.greenfrvr.hashtagview.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.greenfrvr.hashtagview.sample.fragments.BaseSampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.EventsSampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.GravitySampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.SpacingSampleFragment;

/**
 * Created by greenfrvr
 */
public class SamplesAdapter extends FragmentStatePagerAdapter {

    private static final int COUNT = 4;

    public SamplesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = BaseSampleFragment.newInstance();
                break;
            case 1:
                fragment = GravitySampleFragment.newInstance();
                break;
            case 2:
                fragment = SpacingSampleFragment.newInstance();
                break;
            case 3:
                fragment = EventsSampleFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
