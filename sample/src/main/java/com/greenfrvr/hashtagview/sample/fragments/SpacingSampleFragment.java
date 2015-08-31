package com.greenfrvr.hashtagview.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenfrvr.hashtagview.HashtagView;
import com.greenfrvr.hashtagview.sample.R;
import com.greenfrvr.hashtagview.sample.utils.Transformers;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by greenfrvr
 */
public class SpacingSampleFragment extends BaseFragment {

    private static final String data = "Android library collection hashtags min14SDK UI view github opensource project";

    protected @Bind(R.id.hashtags1) HashtagView hashtagView1;
    protected @Bind(R.id.hashtags2) HashtagView hashtagView2;
    protected @Bind(R.id.hashtags3) HashtagView hashtagView3;

    public static SpacingSampleFragment newInstance() {
        Bundle args = new Bundle();

        SpacingSampleFragment fragment = new SpacingSampleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spacing_sample, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hashtagView1.setData(Arrays.asList(data.split("\\s")), Transformers.HASH);
        hashtagView2.setData(Arrays.asList(data.split("\\s")), Transformers.HASH);
        hashtagView3.setData(Arrays.asList(data.split("\\s")), Transformers.HASH);
    }

    @OnClick(R.id.prev_button)
    void previousButtonClicked() {
        getPagerListener().onPreviousClicked();
    }


    @OnClick(R.id.next_button)
    void nextButtonClicked() {
        getPagerListener().onNextClicked();
    }
}
