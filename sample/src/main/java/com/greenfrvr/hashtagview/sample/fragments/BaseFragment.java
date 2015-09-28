package com.greenfrvr.hashtagview.sample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.greenfrvr.hashtagview.sample.R;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by greenfrvr
 */
public abstract class BaseFragment extends Fragment {

    public static final List<String> DATA = Arrays.asList("android", "library", "collection",
            "hashtags", "min14sdk", "UI", "view", "github", "opensource", "project", "widget");

    public static final List<String> TAGS = Arrays.asList("cupcake", "donut", "eclair", "froyo",
            "gingerbread", "honeycomb", "icecreamsandwich", "jellybean", "kitkat", "lollipop", "marshmallow");

    public static final List<String> PEOPLE = Arrays.asList("wolverine", "jubilee", "colossus",
            "beast", "rogue", "storm", "cyclops", "iceman", "magma", "emmafrost", "angel");

    private PagerListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PagerListener) context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @OnClick(R.id.prev_button)
    void previousButtonClicked() {
        listener.onPreviousClicked();
    }


    @OnClick(R.id.next_button)
    void nextButtonClicked() {
        listener.onNextClicked();
    }

    public interface PagerListener {
        void onPreviousClicked();

        void onNextClicked();
    }
}
