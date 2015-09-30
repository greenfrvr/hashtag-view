package com.greenfrvr.hashtagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by greenfrvr
 */
public class HashtagView extends LinearLayout {

    @IntDef({GRAVITY_LEFT, GRAVITY_CENTER, GRAVITY_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityMode {
    }

    public static final int GRAVITY_LEFT = Gravity.LEFT;
    public static final int GRAVITY_RIGHT = Gravity.RIGHT;
    public static final int GRAVITY_CENTER = Gravity.CENTER;

    @IntDef({MODE_WRAP, MODE_STRETCH, MODE_EQUAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StretchMode {
    }

    public static final int MODE_WRAP = 0;
    public static final int MODE_STRETCH = 1;
    public static final int MODE_EQUAL = 2;

    @IntDef({DISTRIBUTION_LEFT, DISTRIBUTION_MIDDLE, DISTRIBUTION_RIGHT, DISTRIBUTION_RANDOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RowDistribution {
    }

    public static final int DISTRIBUTION_LEFT = 0;
    public static final int DISTRIBUTION_MIDDLE = 1;
    public static final int DISTRIBUTION_RIGHT = 2;
    public static final int DISTRIBUTION_RANDOM = 3;

    @IntDef({ELLIPSIZE_START, ELLIPSIZE_MIDDLE, ELLIPSIZE_END, ELLIPSIZE_MARQUEE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Ellipsize {
    }

    public static final int ELLIPSIZE_START = 0;
    public static final int ELLIPSIZE_MIDDLE = 1;
    public static final int ELLIPSIZE_END = 2;
    public static final int ELLIPSIZE_MARQUEE = 3;

    private static final SparseArray<TextUtils.TruncateAt> ellipsizeList = new SparseArray<>(4);

    static {
        ellipsizeList.put(ELLIPSIZE_START, TextUtils.TruncateAt.START);
        ellipsizeList.put(ELLIPSIZE_MIDDLE, TextUtils.TruncateAt.MIDDLE);
        ellipsizeList.put(ELLIPSIZE_END, TextUtils.TruncateAt.END);
        ellipsizeList.put(ELLIPSIZE_MARQUEE, TextUtils.TruncateAt.MARQUEE);
    }

    private final LayoutParams rowLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private final LayoutParams itemLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private final FrameLayout.LayoutParams itemFrameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
    private int maxItemWidth;
    private int itemTextColor;
    private int itemTextGravity;
    private int itemTextEllipsize;
    private float itemTextSize;

    private int rowMargin;
    private int rowGravity;
    private int rowDistribution;
    private int rowMode;
    private int tagRowsCount;
    private int backgroundDrawable;
    private int foregroundDrawable;
    private int leftDrawable;
    private int leftSelectedDrawable;
    private int rightDrawable;
    private int rightSelectedDrawable;

    private Typeface typeface;

    private float totalItemsWidth;

    private boolean isInSelectMode;

    private DataTransform transformer = DefaultTransform.newInstance();
    private DataSelector selector = DefaultSelector.newInstance();

    private final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if (getWidth() > 0) {
                wrap();
                sort();
                draw();
                getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
            }
            return true;
        }
    };

    public HashtagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        extractAttributes(attrs);
        prepareLayoutParams();

        widthList = new ArrayList<>();
        data = new ArrayList<>();
    }

    /**
     * Method defines data as simple {@link java.lang.String} array. Using this method makes not
     * possible to use {@link android.text.Spannable} for representing items label.
     *
     * @param list {@link java.lang.String} array representing data collection.
     */
    public <T> void setData(@NonNull List<T> list) {
        widthList.clear();
        data.clear();
        for (T item : list) {
            data.add(new ItemData<>(item));
        }
        getViewTreeObserver().addOnPreDrawListener(preDrawListener);
    }

    /**
     * Method defines data as an array of custom data model. Using this method allow you
     * to use {@link android.text.Spannable} for representing items label.
     *
     * @param list        Array of user defined objects representing data collection.
     * @param transformer Implementation of {@link com.greenfrvr.hashtagview.HashtagView.DataTransform}
     *                    interface. Can be used for building label from several custom data model
     *                    fields or to prepare {@link android.text.Spannable} label representation.
     * @param <T>         Custom data model
     */
    public <T> void setData(@NonNull List<T> list, @NonNull DataTransform<T> transformer) {
        this.transformer = transformer;
        setData(list);
    }

    /**
     * Method defines data as an array of custom data model. Using this method allow you
     * to use {@link android.text.Spannable} for representing items label and define which items
     * should be preselected items.
     *
     * @param list        Array of user defined objects representing data collection.
     * @param transformer Implementation of {@link com.greenfrvr.hashtagview.HashtagView.DataTransform}
     *                    interface. Can be used for building label from several custom data model
     *                    fields or to prepare {@link android.text.Spannable} label representation.
     * @param selector    Implementation of {@link com.greenfrvr.hashtagview.HashtagView.DataSelector}
     *                    interface. Can be used to preselect some items.
     * @param <T>         Custom data model
     */
    public <T> void setData(@NonNull List<T> list, @NonNull DataTransform<T> transformer, @NonNull DataSelector<T> selector) {
        this.selector = selector;
        setData(list, transformer);
    }

    /**
     * @return List of selected items. Consists of objects corresponding to custom data model defined by setData() method
     */
    public List<Object> getSelectedItems() {
        List<Object> selected = new ArrayList<>();
        for (ItemData item : viewMap.values()) {
            if (item.isSelected)
                selected.add(item.data);
        }
        return selected;
    }

    /**
     * Set up single item click listener
     *
     * @param listener {@link com.greenfrvr.hashtagview.HashtagView.TagsClickListener}
     */
    public void setOnTagClickListener(TagsClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * Set up selection items listener
     *
     * @param listener {@link com.greenfrvr.hashtagview.HashtagView.TagsSelectListener}
     */
    public void setOnTagSelectListener(TagsSelectListener listener) {
        this.selectListener = listener;
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

    public void setMaxItemWidth(int maxWidth) {
        this.maxItemWidth = maxWidth;
    }

    public void setMaxItemWidthRes(@DimenRes int maxWidth) {
        this.maxItemWidth = getResources().getDimensionPixelOffset(maxWidth);
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

    public void setRowGravity(@GravityMode int rowGravity) {
        this.rowGravity = rowGravity;
    }

    public void setRowMode(@StretchMode int rowMode) {
        this.rowMode = rowMode;
    }

    public void setRowDistribution(@RowDistribution int rowDistribution) {
        this.rowDistribution = rowDistribution;
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

    public void setLeftDrawable(@DrawableRes int drawableRes) {
        this.leftDrawable = drawableRes;
    }

    public void setLeftSelectedDrawable(@DrawableRes int drawableRes) {
        this.leftSelectedDrawable = drawableRes;
    }

    public void setRightDrawable(@DrawableRes int drawableRes) {
        this.rightDrawable = drawableRes;
    }

    public void setRightSelectedDrawable(@DrawableRes int drawableRes) {
        this.rightSelectedDrawable = drawableRes;
    }

    public void setInSelectMode(boolean selectMode) {
        isInSelectMode = selectMode;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setEllipsize(@Ellipsize int ellipsizeMode) {
        itemTextEllipsize = ellipsizeMode;
    }

    private void extractAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HashtagView, 0, 0);
        try {
            itemMargin = a.getDimensionPixelOffset(R.styleable.HashtagView_tagMargin, getResources().getDimensionPixelOffset(R.dimen.default_item_margin));
            itemPaddingLeft = a.getDimensionPixelOffset(R.styleable.HashtagView_tagPaddingLeft, getResources().getDimensionPixelOffset(R.dimen.default_item_padding));
            itemPaddingRight = a.getDimensionPixelOffset(R.styleable.HashtagView_tagPaddingRight, getResources().getDimensionPixelOffset(R.dimen.default_item_padding));
            itemPaddingTop = a.getDimensionPixelOffset(R.styleable.HashtagView_tagPaddingTop, getResources().getDimensionPixelOffset(R.dimen.default_item_padding));
            itemPaddingBottom = a.getDimensionPixelOffset(R.styleable.HashtagView_tagPaddingBottom, getResources().getDimensionPixelOffset(R.dimen.default_item_padding));
            itemDrawablePadding = a.getDimensionPixelOffset(R.styleable.HashtagView_tagDrawablePadding, 0);
            minItemWidth = a.getDimensionPixelOffset(R.styleable.HashtagView_tagMinWidth, getResources().getDimensionPixelOffset(R.dimen.min_item_width));
            maxItemWidth = a.getDimensionPixelOffset(R.styleable.HashtagView_tagMaxWidth, getResources().getDimensionPixelOffset(R.dimen.min_item_width));
            rowMargin = a.getDimensionPixelOffset(R.styleable.HashtagView_rowMargin, getResources().getDimensionPixelOffset(R.dimen.default_row_margin));
            itemTextSize = a.getDimension(R.styleable.HashtagView_tagTextSize, getResources().getDimension(R.dimen.default_text_size));

            itemTextGravity = a.getInt(R.styleable.HashtagView_tagTextGravity, Gravity.CENTER);
            itemTextEllipsize = a.getInt(R.styleable.HashtagView_tagEllipsize, ELLIPSIZE_END);
            rowGravity = a.getInt(R.styleable.HashtagView_rowGravity, Gravity.CENTER);
            rowDistribution = a.getInt(R.styleable.HashtagView_rowDistribution, DISTRIBUTION_RANDOM);
            rowMode = a.getInt(R.styleable.HashtagView_rowMode, MODE_WRAP);
            tagRowsCount = a.getInt(R.styleable.HashtagView_tagRowsCount, 0);

            backgroundDrawable = a.getResourceId(R.styleable.HashtagView_tagBackground, 0);
            foregroundDrawable = a.getResourceId(R.styleable.HashtagView_tagForeground, 0);
            leftDrawable = a.getResourceId(R.styleable.HashtagView_tagDrawableLeft, 0);
            leftSelectedDrawable = a.getResourceId(R.styleable.HashtagView_tagSelectedDrawableLeft, 0);
            rightDrawable = a.getResourceId(R.styleable.HashtagView_tagDrawableRight, 0);
            rightSelectedDrawable = a.getResourceId(R.styleable.HashtagView_tagSelectedDrawableRight, 0);

            itemTextColor = a.getColor(R.styleable.HashtagView_tagTextColor, Color.BLACK);

            isInSelectMode = a.getBoolean(R.styleable.HashtagView_selectionMode, false);
        } finally {
            a.recycle();
        }
    }

    private void prepareLayoutParams() {
        itemFrameParams.gravity = itemTextGravity;

        itemLayoutParams.leftMargin = itemMargin;
        itemLayoutParams.rightMargin = itemMargin;
        itemLayoutParams.weight = rowMode > 0 ? 1 : 0;
        if (MODE_EQUAL == rowMode) {
            itemLayoutParams.width = 0;
        }

        rowLayoutParams.topMargin = rowMargin;
        rowLayoutParams.bottomMargin = rowMargin;
    }

    private void wrap() {
        if (data == null || data.isEmpty()) return;
        totalItemsWidth = 0;

        for (ItemData item : data) {
            View view = inflateItemView(item);

            TextView itemView = (TextView) view.findViewById(R.id.text);
            itemView.setText(transformer.prepare(item.data));
            decorateItemTextView(itemView);

            float width = itemView.getMeasuredWidth() + drawableMetrics(itemView) + totalOffset();
            width = Math.max(width, minItemWidth);
            width = Math.min(width, getViewWidth() - 2 * totalOffset());
            item.view = view;
            item.width = width;
            setItemPreselected(item);

            widthList.add(width);
            totalItemsWidth += width;
        }

        Collections.sort(data);
        Collections.sort(widthList, Collections.reverseOrder());
    }

    private void setItemPreselected(ItemData item) {
        if (isInSelectMode) {
            item.isSelected = selector.preselect(item.data);
            item.decorateText(transformer);
            item.displaySelection(leftDrawable, leftSelectedDrawable, rightDrawable, rightSelectedDrawable);
        }
    }

    private void sort() {
        if (data == null || data.isEmpty()) return;

        int rowsQuantity = tagRowsCount == 0 ? evaluateRowsQuantity() : tagRowsCount;
        final int[] rowsWidth = new int[rowsQuantity];

        viewMap = ArrayListMultimap.create(rowsQuantity, data.size());

        while (!data.isEmpty()) {
            rowIteration:
            for (int i = 0; i < rowsQuantity; i++) {
                for (ItemData item : data) {
                    if (tagRowsCount > 0 || rowsWidth[i] + item.width <= getViewWidth()) {
                        rowsWidth[i] += item.width;
                        viewMap.put(i, item);
                        data.remove(item);
                        continue rowIteration;
                    }
                }
            }
        }
    }

    private void draw() {
        if (viewMap == null || viewMap.isEmpty()) return;
        removeAllViews();

        for (Integer key : viewMap.keySet()) {
            ViewGroup rowLayout = getRowLayout(viewMap.get(key).size());
            addView(rowLayout);
            applyDistribution(viewMap.get(key));

            for (ItemData item : viewMap.get(key)) {
                rowLayout.addView(item.view, itemLayoutParams);
            }
        }
    }

    private void applyDistribution(Collection<ItemData> list) {
        switch (rowDistribution) {
            case DISTRIBUTION_LEFT:
                Collections.sort((List) list);
                break;
            case DISTRIBUTION_MIDDLE:
                SortUtil.symmetricSort((List) list);
                break;
            case DISTRIBUTION_RIGHT:
                Collections.sort((List) list, Collections.reverseOrder());
                break;
            case DISTRIBUTION_RANDOM:
                Collections.shuffle((List) list);
                break;
        }
    }

    private ViewGroup getRowLayout(int weightSum) {
        LinearLayout rowLayout = new LinearLayout(getContext());
        rowLayout.setLayoutParams(rowLayoutParams);
        rowLayout.setOrientation(HORIZONTAL);
        rowLayout.setGravity(rowGravity);
        rowLayout.setWeightSum(weightSum);
        return rowLayout;
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

    private int evaluateRowsQuantity() {
        if (widthList == null || widthList.isEmpty()) return 0;

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
                    if (rowsWidth[i] + item <= getWidth()) {
                        rowsWidth[i] += item;
                        widthList.remove(item);
                        continue rowIteration;
                    }
                }
            }
        }
        return rows;
    }

    private View inflateItemView(final ItemData item) {
        ViewGroup itemLayout = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_item, this, false);
        itemLayout.setBackgroundResource(backgroundDrawable);
        itemLayout.setPadding(itemPaddingLeft, itemPaddingTop, itemPaddingRight, itemPaddingBottom);
        itemLayout.setMinimumWidth(minItemWidth);
        try {
            if (foregroundDrawable != 0)
                ((FrameLayout) itemLayout).setForeground(ContextCompat.getDrawable(getContext(), foregroundDrawable));
        } catch (Exception e) {
            e.printStackTrace();
        }

        itemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInSelectMode) {
                    handleSelection(item);
                } else {
                    handleClick(item);
                }
            }
        });
        return itemLayout;
    }

    private void decorateItemTextView(TextView textView) {
        textView.setTextColor(itemTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemTextSize);
        textView.setCompoundDrawablePadding(itemDrawablePadding);
        textView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, rightDrawable, 0);
        textView.setEllipsize(ellipsizeList.get(itemTextEllipsize));
        if (maxItemWidth > 0) textView.setMaxWidth(maxItemWidth);
        if (typeface != null) textView.setTypeface(typeface);

        textView.setLayoutParams(itemFrameParams);
        textView.measure(0, 0);
    }

    private void handleSelection(ItemData item) {
        item.select(leftDrawable, leftSelectedDrawable, rightDrawable, rightSelectedDrawable);
        item.decorateText(transformer);

        if (selectListener != null) {
            selectListener.onItemSelected(item.data, item.isSelected);
        }
    }

    private void handleClick(ItemData item) {
        if (clickListener != null) {
            clickListener.onItemClicked(item.data);
        }
    }

    /**
     * Listener used to handle item click events.
     */
    public interface TagsClickListener {
        void onItemClicked(Object item);
    }

    /**
     * Listener used to handle item selection events.
     */
    public interface TagsSelectListener {
        void onItemSelected(Object item, boolean selected);
    }

    /**
     * Prepare the formatting and appearance of data to be displayed on each item.
     * As it returns {@link CharSequence}, item text can be represented as a {@link android.text.SpannableString}.
     * Avoid using spans which may produce item width change (such as {@link android.text.style.BulletSpan} or {@link android.text.style.RelativeSizeSpan})
     */
    public interface DataTransform<T> {
        CharSequence prepare(T item);
    }

    /**
     * Prepare the formatting and appearance of data to be displayed on each item, for both selected and
     * non-selected state.
     * As it returns {@link CharSequence}, item text can be represented as a {@link android.text.SpannableString}.
     * Avoid using spans which may produce item width change (such as {@link android.text.style.BulletSpan} or {@link android.text.style.RelativeSizeSpan})
     */
    public interface DataStateTransform<T> extends DataTransform<T> {
        CharSequence prepareSelected(T item);
    }

    /**
     * Allows to define whether item should be preselected or not. Returning true (or false) for exact
     * item will cause initial state of this item to be set to selected (or unselected).
     */
    public interface DataSelector<T> {
        boolean preselect(T item);
    }
}
