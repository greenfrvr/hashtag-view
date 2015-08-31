package com.greenfrvr.hashtagview.sample.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.greenfrvr.hashtagview.sample.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by greenfrvr
 */
public abstract class BaseFragment extends Fragment {

    private PagerListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (PagerListener) activity;
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

    protected PagerListener getPagerListener() {
        return listener;
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
