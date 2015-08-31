package com.greenfrvr.hashtagview.sample.utils;

import com.greenfrvr.hashtagview.HashtagView;

/**
 * Created by greenfrvr
 */
public class Transformers{

    public static final HashtagView.DataTransform<String> HASH = new HashtagView.DataTransform<String>() {
        @Override
        public String prepare(String item) {
            return "#" + item;
        }
    };

    public static final HashtagView.DataTransform<String> PERSON = new HashtagView.DataTransform<String>() {
        @Override
        public String prepare(String item) {
            return "@" + item;
        }
    };

    public static final HashtagView.DataTransform<String> CAPS = new HashtagView.DataTransform<String>() {
        @Override
        public String prepare(String item) {
            return item.toUpperCase();
        }
    };

}
