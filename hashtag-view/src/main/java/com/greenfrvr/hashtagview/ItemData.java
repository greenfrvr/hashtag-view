package com.greenfrvr.hashtagview;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;


/**
 * Object representing HashtagView item model.
 *
 * @param <T> custom data model
 */
class ItemData<T> implements Comparable<ItemData> {
    protected T data;

    protected View view;
    protected float width;
    protected boolean isSelected;

    public ItemData(T data) {
        this.data = data;
    }

    void setText(CharSequence charSequence) {
        ((TextView) view.findViewById(R.id.text)).setText(charSequence);
    }

    void displaySelection(int left, int leftSelected, int right, int rightSelected) {
        ((TextView) view.findViewById(R.id.text)).setCompoundDrawablesWithIntrinsicBounds(isSelected ? leftSelected : left, 0, isSelected ? rightSelected : right, 0);
        view.setSelected(isSelected);
        view.invalidate();
    }

    void select(int left, int leftSelected, int right, int rightSelected) {
        isSelected = !isSelected;
        displaySelection(left, leftSelected, right, rightSelected);
    }

    void decorateText(HashtagView.DataTransform<T> transformer) {
        if (transformer instanceof HashtagView.DataStateTransform) {
            if (isSelected) {
                setText(((HashtagView.DataStateTransform<T>) transformer).prepareSelected(data));
            } else {
                setText(transformer.prepare(data));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ItemData && this.data.equals(((ItemData) o).data);
    }

    @Override
    public String toString() {
        return String.format("Item data: title - %s, width - %f", data.toString(), width);
    }

    @Override
    public int compareTo(@NonNull ItemData another) {
        if (width == another.width) return 0;
        return width < another.width ? 1 : -1;
    }
}