package com.greenfrvr.hashtagview.sample.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by greenfrvr
 */
public class IntentFactory {

    public static void email(Context context, String email) {
        context.startActivity(emailIntent(email));
    }

    public static Intent emailIntent(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email.trim()));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hashtag view library");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static void explore(Context context, String url) {
        context.startActivity(exploreIntent(url));
    }

    public static Intent exploreIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

}
