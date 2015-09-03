package com.greenfrvr.hashtagview.sample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.greenfrvr.hashtagview.sample.fragments.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BaseFragment.PagerListener {

    protected @Bind(R.id.pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTitle(R.string.toolbar_title);

        SamplesAdapter adapter = new SamplesAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPreviousClicked() {
        int current = viewPager.getCurrentItem();
        viewPager.setCurrentItem(current - 1);
    }

    @Override
    public void onNextClicked() {
        int current = viewPager.getCurrentItem();
        viewPager.setCurrentItem(current + 1);
    }

}
