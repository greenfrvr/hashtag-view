package com.greenfrvr.hashtagview;

import android.support.annotation.NonNull;
import android.view.View;


/**
 * Object representing HashtagView item model.
 *
 * @param <T> custom data model
 */
class ItemData<T> implements Comparable<ItemData> {
    protected T data;

    protected View view;
    protected CharSequence title;
    protected float width;
    protected boolean isSelected;

    public ItemData(CharSequence title) {
        this.title = title;
    }

    public ItemData(T data, CharSequence title) {
        this.data = data;
        this.title = title;
    }

    public void select() {
        isSelected = !isSelected;
        view.setSelected(isSelected);
        view.invalidate();
    }

    @Override
    public String toString() {
        return String.format("Item data: title - %s, width - %f", title, width);
    }

    @Override
    public int compareTo(@NonNull ItemData another) {
        if (width == another.width) return 0;
        return width < another.width ? 1 : -1;
    }
}