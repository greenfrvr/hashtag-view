package com.greenfrvr.hashtagview.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenfrvr.hashtagview.HashtagView;
import com.greenfrvr.hashtagview.sample.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by greenfrvr
 */
public class DistributionSampleFragment extends BaseFragment {
    public static final List<String> DISTR_DATA = Arrays.asList("12345", "234567",
            "1234", "456", "891", "59", "1", "3", "7", "9");

    protected @Bind(R.id.hashtags1) HashtagView hashtagView1;
    protected @Bind(R.id.hashtags2) HashtagView hashtagView2;
    protected @Bind(R.id.hashtags3) HashtagView hashtagView3;
    protected @Bind(R.id.hashtags4) HashtagView hashtagView4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_distribution_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hashtagView1.setData(DISTR_DATA);
        hashtagView2.setData(DISTR_DATA);
        hashtagView3.setData(DISTR_DATA);
        hashtagView4.setData(DISTR_DATA);
    }

}
