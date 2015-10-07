package com.greenfrvr.hashtagview.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenfrvr.hashtagview.HashtagView;
import com.greenfrvr.hashtagview.sample.R;
import com.greenfrvr.hashtagview.sample.utils.Transformers;

import butterknife.Bind;

/**
 * Created by greenfrvr
 */
public class SpacingSampleFragment extends BaseFragment {

    protected @Bind(R.id.hashtags1) HashtagView hashtagView1;
    protected @Bind(R.id.hashtags2) HashtagView hashtagView2;
    protected @Bind(R.id.hashtags3) HashtagView hashtagView3;
    protected @Bind(R.id.hashtags4) HashtagView hashtagView4;
    protected @Bind(R.id.hashtags5) HashtagView hashtagView5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spacing_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hashtagView1.setData(DATA, Transformers.HASH);
        hashtagView2.setData(DATA, Transformers.HASH);
        hashtagView3.setData(DATA, Transformers.HASH);
        hashtagView4.setData(DATA, Transformers.HASH);
        hashtagView5.setData(TAGS, Transformers.HASH);
    }

}
