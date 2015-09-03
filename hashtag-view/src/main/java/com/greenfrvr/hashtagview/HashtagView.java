package com.greenfrvr.hashtagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

/**
 * Created by greenfrvr
 */
public class HashtagView extends LinearLayout {

    public static final int GRAVITY_LEFT = Gravity.LEFT;
    public static final int GRAVITY_RIGHT = Gravity.RIGHT;
    public static final int GRAVITY_CENTER = Gravity.CENTER;

    public static final int MODE_STRETCH = 1;
    public static final int MODE_WRAP = 0;

    private final LayoutParams rowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private final ItemComparator comparator = new ItemComparator();

    private TagsClickListener clickListener;
    private TagsSelectListener selectListener;

    private List<Float> widthList;
    private List<ItemData> data;
    private Multimap<Integer, ItemData> viewMap;

    private int itemMargin;
    private int itemPaddingLeft;
    private int itemPaddingRight;
    private int itemPaddingTop;
    private int itemPaddingBottom;
    private int itemDrawablePadding;
    private int minItemWidth;
    private int itemTextColor;
    private int itemTextGravity;
    private float itemTextSize;

    private int rowMargin;
    private int rowGravity;
    private int rowMode;
    private int backgroundDrawable;
    private int foregroundDrawable;
    private int leftDrawable;
    private int rightDrawable;

    private Typeface typeface;

    private float totalItemsWidth = 0;

    private boolean isInSelectMode;

    private final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            Timber.i("Pre draw listener shoots");
            wrap();
            sort();
            draw();
            getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
            return true;
        }
    };

    public HashtagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        extractAttributes(attrs);

        getViewTreeObserver().addOnPreDrawListener(preDrawListener);
    }

    private void extractAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HashtagView, 0, 0);
        try {
            itemMargin = a.getDimensionPixelOffset(R.styleable.HashtagView_tagMargin, getResources().getDimensionPixelOffset(R.dimen.default_item_margin));
            itemPaddingLeft = a.getDimensionPixelOffset(R.styleable.HashtagView_tagPaddingLeft, getResources().getDimensionPixelOffset(R.dimen.default_item_padding));
            itemPaddingRight = a.getDimensionPixelOffset(R.styleable.HashtagView_tagPaddingLeft, getResources().getDimensionPixelOffset(R.dimen.default_item_padding));
            itemPaddingTop = a.getDimensionPixelOffset(R.styleable.HashtagView_tagPaddingLeft, getResources().getDimensionPixelOffset(R.dimen.default_item_padding));
            itemPaddingBottom = a.getDimensionPixelOffset(R.styleable.HashtagView_tagPaddingLeft, getResources().getDimensionPixelOffset(R.dimen.default_item_padding));
            itemDrawablePadding = a.getDimensionPixelOffset(R.styleable.HashtagView_tagDrawablePadding, 0);
            minItemWidth = a.getDimensionPixelOffset(R.styleable.HashtagView_tagMinWidth, getResources().getDimensionPixelOffset(R.dimen.min_item_width));
            rowMargin = a.getDimensionPixelOffset(R.styleable.HashtagView_rowMargin, getResources().getDimensionPixelOffset(R.dimen.default_row_margin));
            itemTextSize = a.getDimension(R.styleable.HashtagView_tagTextSize, getResources().getDimension(R.dimen.default_text_size));

            itemTextGravity = a.getInt(R.styleable.HashtagView_tagTextGravity, Gravity.CENTER);
            rowGravity = a.getInt(R.styleable.HashtagView_rowGravity, Gravity.CENTER);
            rowMode = a.getInt(R.styleable.HashtagView_rowMode, MODE_WRAP);

            backgroundDrawable = a.getResourceId(R.styleable.HashtagView_tagBackground, 0);
            foregroundDrawable = a.getResourceId(R.styleable.HashtagView_tagForeground, 0);
            leftDrawable = a.getResourceId(R.styleable.HashtagView_tagDrawableLeft, 0);
            rightDrawable = a.getResourceId(R.styleable.HashtagView_tagDrawableRight, 0);

            itemTextColor = a.getColor(R.styleable.HashtagView_tagTextColor, Color.BLACK);

            isInSelectMode = a.getBoolean(R.styleable.HashtagView_selectionMode, false);
        } finally {
            a.recycle();
        }
    }

    public void setData(List<String> list) {
        widthList = new ArrayList<>(list.size());
        data = new ArrayList<>(list.size());
        for (String item : list) {
            data.add(new ItemData<>(item));
        }
    }

    public <T> void setData(List<T> list, DataTransform<T> transformer) {
        widthList = new ArrayList<>(list.size());
        data = new ArrayList<>(list.size());
        for (T item : list) {
            data.add(new ItemData<>(item, transformer.prepare(item)));
        }
    }

    public List<Object> getSelectedItems() {
        List<Object> selected = new ArrayList<>();
        for (ItemData item : viewMap.values()) {
            if (item.isSelected)
                selected.add(item.data != null ? item.data : item.title.toString());
        }
        return selected;
    }

    public void setOnTagClickListener(TagsClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnTagSelectListener(TagsSelectListener listener) {
        this.selectListener = listener;
    }

    public void wrap() {
        if (data == null || data.isEmpty()) return;

        for (ItemData item : data) {
            View view = inflateItemView(item);

            TextView itemView = (TextView) view.findViewById(R.id.text);
            itemView.setText(item.title);
            decorate(itemView);

            float width = itemView.getMeasuredWidth() + drawableMetrics(itemView) + totalOffset();
            width = Math.max(width, minItemWidth);
            item.view = view;
            item.width = width;

            widthList.add(width);
            totalItemsWidth += width;
        }

        Collections.sort(data, comparator);
        Collections.sort(widthList, Collections.reverseOrder());
        Timber.i(Arrays.toString(data.toArray()));
    }

    private void decorate(TextView textView) {
        textView.setTextColor(itemTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemTextSize);
        textView.setCompoundDrawablePadding(itemDrawablePadding);
        textView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, rightDrawable, 0);
        if (typeface != null) textView.setTypeface(typeface);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = itemTextGravity;
        textView.setLayoutParams(params);
        textView.measure(0, 0);
    }

    private int evaluateRowsQuantity() {
        if (widthList == null || widthList.isEmpty()) return 0;

        Timber.i("Total items width is %f, and width of widget is %d", totalItemsWidth, getViewWidth());
        int rows = (int) Math.ceil(totalItemsWidth / getViewWidth());
        int[] rowsWidth = new int[rows];
        int iterationLimit = rows + widthList.size();

        int counter = 0;
        while (!widthList.isEmpty()) {
            rowIteration:
            for (int i = 0; i < rows; i++) {
                if (counter > iterationLimit)
                    return rows + 1;

                counter++;
                for (Float item : widthList) {
                    if (rowsWidth[i] + item < getWidth()) {
                        rowsWidth[i] += item;
                        widthList.remove(item);
                        continue rowIteration;
                    }
                }
            }
        }
        return rows;
    }

    public void sort() {
        if (data == null || data.isEmpty()) return;

        int rowsQuantity = evaluateRowsQuantity();
        final int[] rowsWidth = new int[rowsQuantity];
        Timber.i("Rows count - %d", rowsQuantity);

        viewMap = ArrayListMultimap.create(rowsQuantity, data.size());

        while (!data.isEmpty()) {
            rowIteration:
            for (int i = 0; i < rowsQuantity; i++) {
                for (ItemData item : data) {
                    if (rowsWidth[i] + item.width < getViewWidth()) {
                        rowsWidth[i] += item.width;
                        viewMap.put(i, item);
                        data.remove(item);
                        continue rowIteration;
                    }
                }
            }
        }
    }

    public void draw() {
        if (viewMap == null || viewMap.isEmpty()) return;
        removeAllViews();

        for (Integer key : viewMap.keySet()) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setGravity(Gravity.CENTER);
            rowLayout.setOrientation(HORIZONTAL);
            rowLayout.setLayoutParams(rowParams);
            rowLayout.setGravity(rowGravity);
            rowLayout.setWeightSum(viewMap.get(key).size());
            addView(rowLayout);

            List<ItemData> itemsList = new ArrayList<>(viewMap.get(key));
            Collections.shuffle(itemsList);
            for (ItemData item : itemsList) {
                rowLayout.addView(item.view, getItemLayoutParams());
            }
        }
    }

    private int getViewWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int totalOffset() {
        return itemPaddingLeft + itemPaddingRight + 2 * itemMargin;
    }

    private int drawableMetrics(TextView textView) {
        int drawablesWidth = 0;
        Drawable[] drawables = textView.getCompoundDrawables();
        drawablesWidth += drawables[0] != null ? drawables[0].getIntrinsicWidth() + itemDrawablePadding : 0;
        drawablesWidth += drawables[2] != null ? drawables[2].getIntrinsicWidth() + itemDrawablePadding : 0;
        return drawablesWidth;
    }

    private View inflateItemView(final ItemData item) {
        ViewGroup itemLayout = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_item, this, false);
        itemLayout.setBackgroundResource(backgroundDrawable);
        itemLayout.setPadding(itemPaddingLeft, itemPaddingTop, itemPaddingRight, itemPaddingBottom);
        itemLayout.setMinimumWidth(minItemWidth);
        try {
            if (foregroundDrawable != 0)
                ((FrameLayout) itemLayout).setForeground(ContextCompat.getDrawable(getContext(), foregroundDrawable));
        } catch (Exception e){
            Timber.i(e, "");
        }

        itemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("Item clicked");
                if (isInSelectMode) {
                    handleSelection(item);
                } else {
                    handleClick(item);
                }
            }
        });
        return itemLayout;
    }

    private LayoutParams getItemLayoutParams() {
        LayoutParams itemParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemParams.bottomMargin = rowMargin;
        itemParams.topMargin = rowMargin;
        itemParams.leftMargin = itemMargin;
        itemParams.rightMargin = itemMargin;
        itemParams.weight = rowMode;
        return itemParams;
    }

    private void handleSelection(ItemData item) {
        item.select();
        if (selectListener != null) {
            if (item.data == null) {
                selectListener.onItemSelected(item.title.toString());
            } else {
                selectListener.onItemSelected(item.data);
            }
        }
    }

    private void handleClick(ItemData item) {
        if (clickListener != null) {
            if (item.data == null) {
                clickListener.onItemClicked(item.title.toString());
            } else {
                clickListener.onItemClicked(item.data);
            }
        }
    }

    public void setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
    }

    public void setItemMarginRes(@DimenRes int marginRes) {
        this.itemMargin = getResources().getDimensionPixelOffset(marginRes);
    }

    public void setItemPadding(int left, int right, int top, int bottom) {
        this.itemPaddingLeft = left;
        this.itemPaddingRight = right;
        this.itemPaddingTop = top;
        this.itemPaddingBottom = bottom;
    }

    public void setItemPaddingRes(@DimenRes int left, @DimenRes int right, @DimenRes int top, @DimenRes int bottom) {
        this.itemPaddingLeft = getResources().getDimensionPixelOffset(left);
        this.itemPaddingRight = getResources().getDimensionPixelOffset(right);
        this.itemPaddingTop = getResources().getDimensionPixelOffset(top);
        this.itemPaddingBottom = getResources().getDimensionPixelOffset(bottom);
    }

    public void setMinItemWidth(int minWidth) {
        this.minItemWidth = minWidth;
    }

    public void setMinItemWidthRes(@DimenRes int minWidth) {
        this.minItemWidth = getResources().getDimensionPixelOffset(minWidth);
    }

    public void setItemTextColor(int textColor) {
        this.itemTextColor = textColor;
    }

    public void setItemTextColorRes(@ColorRes int textColor) {
        this.itemTextColor = getResources().getColor(textColor);
    }

    public void setItemTextGravity(int itemTextGravity) {
        this.itemTextGravity = itemTextGravity;
    }

    public void setItemTextSize(float textSize) {
        this.itemTextSize = textSize;
    }

    public void setItemTextSizeRes(@DimenRes int textSize) {
        this.itemTextSize = getResources().getDimension(textSize);
    }

    public void setRowMargin(int rowMargin) {
        this.rowMargin = rowMargin;
    }

    public void setRowMarginRes(@DimenRes int rowMargin) {
        this.rowMargin = getResources().getDimensionPixelOffset(rowMargin);
    }

    public void setRowGravity(int rowGravity) {
        this.rowGravity = rowGravity;
    }

    public void setRowMode(int rowMode) {
        this.rowMode = rowMode;
    }

    public void setBackgroundDrawable(@DrawableRes int backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
    }

    public void setBackgroundColor(@ColorRes int backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
    }

    public void setForegroundDrawable(@DrawableRes int foregroundDrawable) {
        this.foregroundDrawable = foregroundDrawable;
    }

    public void setInSelectMode(boolean selectMode) {
        isInSelectMode = selectMode;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    private class ItemData<T> {
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
    }

    private class ItemComparator implements Comparator<ItemData> {
        @Override
        public int compare(ItemData lhs, ItemData rhs) {
            if (lhs == null || rhs == null) return 0;

            return lhs.width > rhs.width ? -1 : (lhs.width == rhs.width ? 0 : 1);
        }
    }

    public interface TagsClickListener {
        void onItemClicked(Object item);
    }

    public interface TagsSelectListener {
        void onItemSelected(Object item);
    }

    public interface DataTransform<T> {
        CharSequence prepare(T item);
    }
}
