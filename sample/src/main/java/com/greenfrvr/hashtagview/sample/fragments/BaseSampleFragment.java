package com.greenfrvr.hashtagview.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenfrvr.hashtagview.HashtagView;
import com.greenfrvr.hashtagview.sample.R;

import java.util.Arrays;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by greenfrvr
 */
public class BaseSampleFragment extends BaseFragment implements HashtagView.TagsClickListener {

    private static final String data = "Test data can be very boring isn't it?";

    protected @Bind(R.id.hashtags) HashtagView hashtagView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hashtagView.setData(Arrays.asList(data.split("\\s")));
        hashtagView.setOnTagClickListener(this);
    }

    @Override
    public void onItemClicked(Object item) {
        Timber.i("Item [%s] clicked", item.toString());
    }
}
