package com.greenfrvr.hashtagview.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import timber.log.Timber;

/**
 * Created by greenfrvr
 */
public class EventsSampleFragment extends BaseFragment implements HashtagView.TagsClickListener, HashtagView.TagsSelectListener {

    private static final String data = "Android library collection hashtags min14SDK UI view github opensource project";

    @Bind(R.id.next_button) Button nextButton;
    protected @Bind(R.id.coordinator) CoordinatorLayout coordinator;
    protected @Bind(R.id.hashtags1) HashtagView hashtagView1;
    protected @Bind(R.id.hashtags2) HashtagView hashtagView2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton.setVisibility(View.GONE);
        hashtagView1.setData(Arrays.asList(data.split("\\s")), Transformers.HASH);
        hashtagView1.setOnTagClickListener(this);
        hashtagView2.setData(Arrays.asList(data.split("\\s")), Transformers.HASH);
        hashtagView2.setOnTagSelectListener(this);
    }

    @Override
    public void onItemClicked(Object item) {
        Timber.i("Click");
        Snackbar.make(coordinator, String.format("Item %s clicked", item.toString()), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(Object item) {
        Timber.i("Select");
        Snackbar.make(coordinator, String.format("Selected items: %s", Arrays.toString(hashtagView2.getSelectedItems().toArray())), Snackbar.LENGTH_SHORT).show();
    }

}
