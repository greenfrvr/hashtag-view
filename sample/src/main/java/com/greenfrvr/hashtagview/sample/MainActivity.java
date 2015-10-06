package com.greenfrvr.hashtagview.sample;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.greenfrvr.hashtagview.sample.fragments.BaseFragment;
import com.greenfrvr.hashtagview.sample.fragments.ContactsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BaseFragment.PagerListener, ContactsFragment.Listener {

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

    public void openGitHubPage() {
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.primary))
                .setShowTitle(true)
                .setStartAnimations(this, R.anim.enter_from_right, R.anim.exit_to_left)
                .setExitAnimations(this, R.anim.enter_from_left, R.anim.exit_to_right)
                .build();
        intent.launchUrl(this, Uri.parse(getString(R.string.github)));
    }

}
