package com.hxw.frame.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hxw.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示多个图片的bar,例如显示星级
 * Created by hxw on 2017/4/9.
 */

public class RatingBarView extends LinearLayout {
    private List<ImageView> mStars = new ArrayList<ImageView>();//存放星星图片的容器
    private boolean mClickable = false;//是否允许点击
    private OnRatingListener onRatingListener;
    private Object bindObject;
    private float starImageSize;//图片的大小
    private int starCount;//图片个数
    private int starEmptyRes;//没有star的样子
    private int starFillRes;//star后的样子

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView);
        starImageSize = a.getDimension(R.styleable.RatingBarView_starImageSize, 24);
        starCount = a.getInteger(R.styleable.RatingBarView_starCount, 5);
        starEmptyRes = a.getResourceId(R.styleable.RatingBarView_starEmpty, 0);
        starFillRes = a.getResourceId(R.styleable.RatingBarView_starFill, 0);
        a.recycle();

        for (int i = 0; i < starCount; i++) {
            ImageView imageView = getStarImageView(context, attrs);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickable) {
                        setStar(indexOfChild(v) + 1);
                        if (onRatingListener != null) {
                            onRatingListener.onRating(bindObject, indexOfChild(v) + 1);
                        }
                    }

                }
            });
            addView(imageView);
        }
    }

    private ImageView getStarImageView(Context context, AttributeSet attrs) {

        ImageView imageView = new ImageView(context);
        //设置宽高
        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(Math.round(starImageSize), Math.round(starImageSize));
        imageView.setLayoutParams(para);
        imageView.setPadding(0, 0, 4, 0);
        imageView.setImageResource(starEmptyRes);
        return imageView;

    }

    public RatingBarView setStarFillRes(int starFillRes) {
        this.starFillRes = starFillRes;
        return this;
    }

    public RatingBarView setStarEmptyRes(int starEmptyRes) {
        this.starEmptyRes = starEmptyRes;
        return this;
    }

    public RatingBarView setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
        return this;
    }

    public RatingBarView setBindObject(Object bindObject) {
        this.bindObject = bindObject;
        return this;
    }

    public RatingBarView setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
        return this;
    }

    public RatingBarView setmClickable(boolean clickable) {
        this.mClickable = clickable;
        return this;
    }

    public RatingBarView setStar(int starCount) {
        setStar(starCount, true);
        return this;
    }

    public void setStar(int starCount, boolean animation) {
        starCount = starCount > this.starCount ? this.starCount : starCount;
        starCount = starCount < 0 ? 0 : starCount;
        //TODO
        for (int i = 0; i < starCount; i++) {
            ((ImageView) getChildAt(i)).setImageResource(starFillRes);
            if (animation) {
                YoYo.with(Techniques.BounceIn).duration(400).playOn(getChildAt(i));
            }
        }

        for (int i = this.starCount - 1; i >= starCount; i--) {
            ((ImageView) getChildAt(i)).setImageResource(starEmptyRes);
        }

    }

    /**
     * 该监听器用于监听选中Tab时View的变化
     */
    public interface OnRatingListener {

        void onRating(Object bindObject, int ratingScore);

    }
}
