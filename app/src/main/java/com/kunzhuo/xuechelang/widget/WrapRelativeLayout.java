package com.kunzhuo.xuechelang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kunzhuo.xuechelang.R;

/**
 * @author zhangQ
 * @date 2016-1-20
 * @description <p>
 * com.kunzhuo.android.weitanr.widget.WrapRelativeLayout
 * </p>
 */
public class WrapRelativeLayout extends RelativeLayout {
    private Context mContext;
    private float widthPerHeight = 2.5f;

    public WrapRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setCustomAttributes(attrs);
    }

    public WrapRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setCustomAttributes(attrs);
    }

    public WrapRelativeLayout(Context context) {
        super(context);
        this.mContext = context;
    }

    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs,
                R.styleable.WrapImageView);
        widthPerHeight = array.getFloat(
                R.styleable.WrapImageView_widthPerHeight, 2.5f);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();

        // 设置高度
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                (int) (childWidthSize / widthPerHeight), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
