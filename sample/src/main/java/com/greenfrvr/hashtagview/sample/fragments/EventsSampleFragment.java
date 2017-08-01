package com.greenfrvr.hashtagview.sample.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
public class EventsSampleFragment extends BaseFragment implements HashtagView.TagsClickListener, HashtagView.TagsSelectListener {

    protected @Bind(R.id.coordinator) CoordinatorLayout coordinator;
    protected @Bind(R.id.hashtags1) HashtagView hashtagView1;
    protected @Bind(R.id.hashtags2) HashtagView hashtagView2;
    protected @Bind(R.id.hashtags3) HashtagView hashtagView3;

    private int addedCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hashtagView1.setData(DATA, Transformers.HASH);
        hashtagView1.addOnTagClickListener(this);

        hashtagView2.setData(DATA1, new HashtagView.DataTransform<Obj>() {
            @Override
            public CharSequence prepare(Obj item) {
                SpannableString spannableString = new SpannableString("#" + item.name);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#85F5F5F5")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableString;
            }
        }, new HashtagView.DataSelector<Obj>() {
            int i = 0;

            @Override
            public boolean preselect(Obj item) {
                return i++ < 6;
            }
        });
        hashtagView2.addOnTagSelectListener(this);

        hashtagView3.setData(DATA, Transformers.HASH);
        hashtagView3.setDynamicMode(true) ;
    }

    public static final String TAG = "TAGG";

    @OnClick(R.id.add_tag) @SuppressWarnings("unused")
    void addTag() {
        String str = "new_tag_" + addedCount;
        if (hashtagView3.addItem(str)) addedCount++;

        Log.i(TAG, "addTag: " + hashtagView2.getData());
        Log.i(TAG, "addTag: " + hashtagView2.getSelectedItems());
    }

    @OnClick(R.id.remove_tag) @SuppressWarnings("unused")
    void removeTag() {
        String str = "new_tag_" + --addedCount;
        if (!hashtagView3.removeItem(str)) ++addedCount;
    }

    @Override
    public void onItemClicked(Object item) {
        Snackbar.make(coordinator, String.format("Item %s clicked", item.toString()), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(Object item, boolean isSelected) {
        Snackbar.make(coordinator, String.format("Selected items: %s", Arrays.toString(hashtagView2.getSelectedItems().toArray())), Snackbar.LENGTH_SHORT).show();
    }

}
