package com.greenfrvr.hashtagview.sample.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;

import com.greenfrvr.hashtagview.HashtagView;

/**
 * Created by greenfrvr
 */
public class Transformers {

    public static final HashtagView.DataTransform<String> HASH = new HashtagView.DataTransform<String>() {
        @Override
        public CharSequence prepare(String item) {
            SpannableString spannableString = new SpannableString("#" + item);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#85F5F5F5")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    };

    public static final HashtagView.DataTransform<String> HASH_SELECTED = new HashtagView.DataStateTransform<String>() {
        @Override
        public CharSequence prepare(String item) {
            SpannableString spannableString = new SpannableString("#" + item);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#85F5F5F5")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public CharSequence prepareSelected(String item) {
            SpannableString spannableString = new SpannableString("#" + item);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#85F5F5F5")), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    };

    public static final HashtagView.DataTransform<String> PERSON = new HashtagView.DataTransform<String>() {
        @Override
        public CharSequence prepare(String item) {
            SpannableString spannableString = new SpannableString("@" + item);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#85F5F5F5")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    };

    public static final HashtagView.DataTransform<String> CAPS = new HashtagView.DataTransform<String>() {
        @Override
        public CharSequence prepare(String item) {
            return item.toUpperCase();
        }
    };

    public static final HashtagView.DataTransform<String> SPAN1 = new HashtagView.DataTransform<String>() {
        @Override
        public CharSequence prepare(String item) {
            SpannableString spannableString = new SpannableString("#" + item);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#85F5F5F5")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new SuperscriptSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    };

    public static final HashtagView.DataTransform<String> SPAN2 = new HashtagView.DataTransform<String>() {
        @Override
        public CharSequence prepare(String item) {
            SpannableString spannableString = new SpannableString("@" + item);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#33691E")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    };

}
