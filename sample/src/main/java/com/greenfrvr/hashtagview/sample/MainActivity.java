package com.greenfrvr.hashtagview.sample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.greenfrvr.hashtagview.HashtagView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private String data = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    protected @Bind(R.id.pager) ViewPager viewPager;

    protected HashtagView hashtags1;
    protected HashtagView hashtags2;
    protected HashtagView hashtags3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());

        List<String> list = Arrays.asList(data.replaceAll("[^\\w\\s]", "").split("\\s"));
        hashtags1 = (HashtagView) findViewById(R.id.hashtag_view_1);
        hashtags1.setData(list.subList(0, 23)); //Looping
        hashtags1.setOnTagClickListener(new HashtagView.TagsClickListener() {
            @Override
            public void onItemClicked(Object item) {
                Timber.i("Item [%s] clicked", item.toString());
            }
        });

        hashtags2 = (HashtagView) findViewById(R.id.hashtag_view_2);
        hashtags2.setRowGravity(HashtagView.GRAVITY_LEFT);
        hashtags2.setBackgroundDrawable(R.drawable.rounded_rect_2);
        hashtags2.setForegroundDrawable(R.drawable.flat_button_light);
        hashtags2.setItemPaddingRes(R.dimen.padding_horizontal, R.dimen.padding_horizontal, R.dimen.padding_vertical, R.dimen.padding_vertical);
        hashtags2.setItemMargin(getResources().getDimensionPixelOffset(R.dimen.item_margin));
        hashtags2.setRowMarginRes(R.dimen.row_margin);
        hashtags2.setMinItemWidthRes(R.dimen.min_width);
        hashtags2.setItemTextColorRes(android.R.color.white);
        hashtags2.setData(Arrays.asList("tst", "dataa", "couldd", "be", "very", "boringg", "!"));
        hashtags2.setOnTagClickListener(new HashtagView.TagsClickListener() {
            @Override
            public void onItemClicked(Object item) {
                Timber.i("Item [%s] clicked", item.toString());
                for (Object obj : hashtags3.getSelectedItems()) {
                    Timber.i("Selected item: %s", obj.toString());
                }
            }
        });

        hashtags3 = (HashtagView) findViewById(R.id.hashtag_view_3);
        hashtags3.setData(Arrays.asList("test", "data", "can", "be", "very", "boring", "!"));
        hashtags3.setOnTagSelectListener(new HashtagView.TagsSelectListener() {
            @Override
            public void onItemSelected(Object item) {
                Timber.i("Item [%s] selected", item.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
