package com.greenfrvr.hashtagview.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.greenfrvr.hashtagview.sample.fragments.BaseSampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.EventsSampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.GravitySampleFragment;
import com.greenfrvr.hashtagview.sample.fragments.SpacingSampleFragment;

/**
 * Created by greenfrvr
 */
public class SamplesAdapter extends FragmentStatePagerAdapter {

    private static final int COUNT = 4;
    private static final SparseArray<String> fragments = new SparseArray<>(COUNT);

    static {
        fragments.append(0, BaseSampleFragment.class.getName());
        fragments.append(1, GravitySampleFragment.class.getName());
        fragments.append(2, SpacingSampleFragment.class.getName());
        fragments.append(3, EventsSampleFragment.class.getName());
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
