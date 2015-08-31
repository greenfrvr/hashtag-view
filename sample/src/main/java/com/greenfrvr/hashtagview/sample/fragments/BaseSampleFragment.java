package com.greenfrvr.hashtagview.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.greenfrvr.hashtagview.HashtagView;
import com.greenfrvr.hashtagview.sample.R;
import com.greenfrvr.hashtagview.sample.utils.Transformers;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by greenfrvr
 */
public class BaseSampleFragment extends BaseFragment {

    private static final String data = "Android library collection hashtags min14SDK UI view github opensource project";

    @Bind(R.id.prev_button) Button previousButton;
    @Bind(R.id.hashtags) HashtagView hashtagView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        previousButton.setVisibility(View.GONE);

        hashtagView.setData(Arrays.asList(data.split("\\s")), Transformers.HASH);
    }

}
