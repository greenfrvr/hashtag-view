package com.greenfrvr.hashtagview.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.greenfrvr.hashtagview.sample.fragments.BaseSampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.ContactsFragment;
import com.greenfrvr.hashtagview.sample.fragments.DistributionSampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.EventsSampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.GravitySampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.SpacingSampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.StylingSampleFragment;

/**
 * Created by greenfrvr
 */
public class SamplesAdapter extends FragmentStatePagerAdapter {

    private static final int COUNT = 7;
    private static final SparseArray<String> fragments = new SparseArray<>(COUNT);

    static {
        fragments.append(0, BaseSampleFragment.class.getName());
        fragments.append(1, GravitySampleFragment.class.getName());
        fragments.append(2, DistributionSampleFragment.class.getName());
        fragments.append(3, SpacingSampleFragment.class.getName());
        fragments.append(4, EventsSampleFragment.class.getName());
        fragments.append(5, StylingSampleFragment.class.getName());
        fragments.append(6, ContactsFragment.class.getName());
    }

    private Context context;

    public SamplesAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(context, fragments.get(position));
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
