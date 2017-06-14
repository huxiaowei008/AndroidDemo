package com.hxw.frame.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxw.frame.R;

/**
 * Created by hxw on 2017/6/13.
 */

public class CommonLayoutView extends RelativeLayout {
    private Context mContext;
    //9个位置的textView
    private AppCompatTextView leftTV, centerTV, rightTV;
    private AppCompatTextView leftTopTV, centerTopTV, rightTopTV;
    private AppCompatTextView leftBottomTV, centerBottomTV, rightBottomTV;
    //textView的文字
    private CharSequence leftText, centerText, rightText;
    private CharSequence leftTopText, centerTopText, rightTopText;
    private CharSequence leftBottomText, centerBottomText, rightBottomText;
    //textView的字体大小,为px
    private int leftTextSize, centerTextSize, rightTextSize;
    private int leftTopTextSize, centerTopTextSize, rightTopTextSize;
    private int leftBottomTextSize, centerBottomTextSize, rightBottomTextSize;
    //textView的字体颜色
    private ColorStateList leftTextColor, centerTextColor, rightTextColor;
    private ColorStateList leftTopTextColor, centerTopTextColor, rightTopTextColor;
    private ColorStateList leftBottomTextColor, centerBottomTextColor, rightBottomTextColor;
    //4个位置的imageView
    private AppCompatImageView leftImg, rightImg;
    private AppCompatImageView centerTopImg, centerBottomImg;
    //imageView资源
    private int leftImgRes, rightImgRes, centerTopImgRes, centerBottomImgRes;
    //imageVeiw宽高
    private int leftImgWidth, rightImgWidth, centerTopImgWidth, centerBottomImgWidth;
    private int leftImgHeight, rightImgHeight, centerTopImgHeight, centerBottomImgHeight;

    //1个在右边的checkBox
    private AppCompatCheckBox checkBox;
    private int rightCheckBoxRes;
    private boolean isBoxCheck;
    private boolean isShowCheckBox;
    //1个在右边的开关
    private SwitchCompat switchCompat;
    private int thumbRes;//设置开关的图片
    private int trackRes;//设置开关的轨迹图片
    private boolean isSwitchCheck;
    private boolean isShowSwitch;
    //2个上下线
    private View topLine, bottomLine;
    private int topLineHeight, bottomLineHeight;
    private int topLineMarginLeft, topLineMarginRight;
    private int bottomLineMarginLeft, bottomLineMarginRight;
    private int topLineColor, bottomLineColor;

    //布局属性
    private LayoutParams leftTextParams, centerTextParams, rightTextParams,
            leftTopTextParams, centerTopTextParams, rightTopTextParams,
            leftBottomTextParams, centerBottomTextParams, rightBottomTextParams,
            leftImgParams, rightImgParams, centerTopImgParams, centerBottomImgParams,
            rightCheckBoxParams, rightSwitchParams, topLineParams, bottomLineParams;

    private int defaultTextSize;//默认字体大小
    private int defaultPMValue;//默认padding和margin的大小
    private int defaultWH;//图片默认宽高

    public CommonLayoutView(Context context) {
        this(context, null);
    }

    public CommonLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.defaultPMValue = dp2px(context, 8);
        this.defaultTextSize = sp2px(context, 16);
        this.defaultWH = dp2px(context, 24);
        getAttrs(context, attrs);

        initLayout();
    }

    /**
     * 获取自定义属性值
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonLayoutView);
        try {
            //文字获取
            if (a.hasValue(R.styleable.CommonLayoutView_leftText)) {
                leftText = a.getString(R.styleable.CommonLayoutView_leftText);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_centerText)) {
                centerText = a.getString(R.styleable.CommonLayoutView_centerText);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_rightText)) {
                rightText = a.getString(R.styleable.CommonLayoutView_rightText);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_leftTopText)) {
                leftTopText = a.getString(R.styleable.CommonLayoutView_leftTopText);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_centerTopText)) {
                centerTopText = a.getString(R.styleable.CommonLayoutView_centerTopText);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_rightTopText)) {
                rightTopText = a.getString(R.styleable.CommonLayoutView_rightTopText);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_leftBottomText)) {
                leftBottomText = a.getString(R.styleable.CommonLayoutView_leftBottomText);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_centerBottomText)) {
                centerBottomText = a.getString(R.styleable.CommonLayoutView_centerBottomText);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_rightBottomText)) {
                rightBottomText = a.getString(R.styleable.CommonLayoutView_rightBottomText);
            }
            //字体大小
            leftTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_leftTextSize, defaultTextSize);
            centerTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_centerTextSize, defaultTextSize);
            rightTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_rightTextSize, defaultTextSize);
            leftTopTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_leftTopTextSize, defaultTextSize);
            centerTopTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_centerTopTextSize, defaultTextSize);
            rightTopTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_rightTopTextSize, defaultTextSize);
            leftBottomTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_leftBottomTextSize, defaultTextSize);
            centerBottomTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_centerBottomTextSize, defaultTextSize);
            rightBottomTextSize = a.getDimensionPixelSize(R.styleable.CommonLayoutView_rightBottomTextSize, defaultTextSize);
            //字体颜色
            if (a.hasValue(R.styleable.CommonLayoutView_leftTextColor)) {
                leftTextColor = a.getColorStateList(R.styleable.CommonLayoutView_leftTextColor);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_centerTextColor)) {
                centerTextColor = a.getColorStateList(R.styleable.CommonLayoutView_centerTextColor);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_leftTextColor)) {
                rightTextColor = a.getColorStateList(R.styleable.CommonLayoutView_leftTextColor);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_leftTopTextColor)) {
                leftTopTextColor = a.getColorStateList(R.styleable.CommonLayoutView_leftTopTextColor);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_centerTopTextColor)) {
                centerTopTextColor = a.getColorStateList(R.styleable.CommonLayoutView_centerTopTextColor);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_rightTopTextColor)) {
                rightTopTextColor = a.getColorStateList(R.styleable.CommonLayoutView_rightTopTextColor);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_leftBottomTextColor)) {
                leftBottomTextColor = a.getColorStateList(R.styleable.CommonLayoutView_leftBottomTextColor);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_centerBottomTextColor)) {
                centerBottomTextColor = a.getColorStateList(R.styleable.CommonLayoutView_centerBottomTextColor);
            }
            if (a.hasValue(R.styleable.CommonLayoutView_rightBottomTextColor)) {
                rightBottomTextColor = a.getColorStateList(R.styleable.CommonLayoutView_rightBottomTextColor);
            }
            //图片资源
            leftImgRes = a.getResourceId(R.styleable.CommonLayoutView_leftImgRes, -1);
            rightImgRes = a.getResourceId(R.styleable.CommonLayoutView_rightImgRes, -1);
            centerTopImgRes = a.getResourceId(R.styleable.CommonLayoutView_centerTopImgRes, -1);
            centerBottomImgRes = a.getResourceId(R.styleable.CommonLayoutView_centerBottomImgRes, -1);
            //图片宽高
            leftImgWidth = a.getDimensionPixelSize(R.styleable.CommonLayoutView_leftImgWidth, defaultWH);
            leftImgHeight = a.getDimensionPixelSize(R.styleable.CommonLayoutView_leftImgHeight, defaultWH);
            rightImgWidth = a.getDimensionPixelSize(R.styleable.CommonLayoutView_rightImgWidth, defaultWH);
            rightImgHeight = a.getDimensionPixelSize(R.styleable.CommonLayoutView_rightImgHeight, defaultWH);
            centerTopImgWidth = a.getDimensionPixelSize(R.styleable.CommonLayoutView_centerTopImgWidth, defaultWH);
            centerTopImgHeight = a.getDimensionPixelSize(R.styleable.CommonLayoutView_centerTopImgHeight, defaultWH);
            centerBottomImgWidth = a.getDimensionPixelSize(R.styleable.CommonLayoutView_centerBottomImgWidth, defaultWH);
            centerBottomImgHeight = a.getDimensionPixelSize(R.styleable.CommonLayoutView_centerBottomImgHeight, defaultWH);
            //checkBox属性
            isShowCheckBox = a.getBoolean(R.styleable.CommonLayoutView_isShowCheckBox, false);
            rightCheckBoxRes = a.getResourceId(R.styleable.CommonLayoutView_rightCheckBoxRes, -1);
            isBoxCheck = a.getBoolean(R.styleable.CommonLayoutView_isBoxCheck, false);
            //switch属性
            isShowSwitch = a.getBoolean(R.styleable.CommonLayoutView_isShowSwitch, false);
            thumbRes = a.getResourceId(R.styleable.CommonLayoutView_thumbRes, -1);
            trackRes = a.getResourceId(R.styleable.CommonLayoutView_trackRes, -1);
            isSwitchCheck = a.getBoolean(R.styleable.CommonLayoutView_isSwitchCheck, false);
            //上下线属性
            topLineHeight = a.getDimensionPixelSize(R.styleable.CommonLayoutView_topLineHeight, 0);
            bottomLineHeight = a.getDimensionPixelSize(R.styleable.CommonLayoutView_bottomLineHeight, 0);
            topLineMarginLeft = a.getDimensionPixelSize(R.styleable.CommonLayoutView_topLineMarginLeft, 0);
            bottomLineMarginLeft = a.getDimensionPixelSize(R.styleable.CommonLayoutView_bottomLineMarginLeft, 0);
            topLineMarginRight = a.getDimensionPixelSize(R.styleable.CommonLayoutView_topLineMarginRight, 0);
            bottomLineMarginRight = a.getDimensionPixelSize(R.styleable.CommonLayoutView_bottomLineMarginRight, 0);
            topLineColor = a.getColor(R.styleable.CommonLayoutView_topLineColor, Color.parseColor("#ebebeb"));
            bottomLineColor = a.getColor(R.styleable.CommonLayoutView_bottomLineColor, Color.parseColor("#ebebeb"));
        } finally {
            a.recycle();
        }
    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        if (leftImgRes != -1) {
            initLeftImage();
        }
        if (leftText != null) {
            initLeftText();
        }
        if (leftTopText != null) {
            initLeftTopText();
        }
        if (leftBottomText != null) {
            initLeftBottomText();
        }

        if (centerText != null) {
            initCenterText();
        }
        if (centerTopImgRes != -1) {
            initCenterTopImage();
        }
        if (centerTopText != null) {
            initCenterTopText();
        }
        if (centerBottomImgRes != -1) {
            initCenterBottomImage();
        }
        if (centerBottomText != null) {
            initCenterBottomText();
        }


        if (isShowSwitch) {
            initSwitch();
        }
        if (isShowCheckBox) {
            initCheckBox();
        }

        if (rightImgRes != -1) {
            initRightImage();
        }
        if (rightText != null) {
            initRightText();
        }
        if (rightTopText != null) {
            initRightTopText();
        }
        if (rightBottomText != null) {
            initRightBottomText();
        }

        if (topLineHeight != 0) {
            initTopLine();
        }
        if (bottomLineHeight != 0) {
            initBottomLine();
        }
    }

    /**
     * 初始化左边图片
     */
    private void initLeftImage() {
        leftImg = new AppCompatImageView(mContext);
        leftImgParams = new LayoutParams(leftImgWidth, leftImgHeight);
        leftImgParams.addRule(CENTER_VERTICAL);
        leftImgParams.addRule(ALIGN_PARENT_LEFT);
        leftImgParams.setMargins(defaultPMValue, defaultPMValue, 0, defaultPMValue);
        leftImg.setLayoutParams(leftImgParams);
        leftImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        leftImg.setId(R.id.img_left);
        if (leftImgRes != -1) {
            leftImg.setImageResource(leftImgRes);
        }

        addView(leftImg);
    }

    /**
     * 初始化左边文字
     */
    private void initLeftText() {
        leftTV = new AppCompatTextView(mContext);
        leftTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftTextParams.addRule(CENTER_VERTICAL);
        if (leftImg != null) {
            leftTextParams.addRule(RIGHT_OF, R.id.img_left);
        } else {
            leftTextParams.addRule(ALIGN_PARENT_LEFT);
        }
        leftTextParams.setMarginStart(defaultPMValue);
        leftTV.setLayoutParams(leftTextParams);
        leftTV.setId(R.id.tv_left);

        leftTV.setText(leftText);
        leftTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
        if (leftTextColor != null) {
            leftTV.setTextColor(leftTextColor);
        }

        addView(leftTV);
    }

    /**
     * 初始化左边顶部文字
     */
    private void initLeftTopText() {
        leftTopTV = new AppCompatTextView(mContext);
        leftTopTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (leftImg != null) {
            leftTopTextParams.addRule(RIGHT_OF, R.id.img_left);
        } else {
            leftTopTextParams.addRule(ALIGN_PARENT_LEFT);
        }
        if (leftTV != null) {
            leftTopTextParams.addRule(ABOVE, R.id.tv_left);
            leftTopTextParams.bottomMargin = defaultPMValue;
        } else {
            leftTopTextParams.addRule(ALIGN_PARENT_TOP);
            leftTopTextParams.topMargin = defaultPMValue;
        }
        leftTopTextParams.setMarginStart(defaultPMValue);
        leftTopTV.setLayoutParams(leftTopTextParams);
        leftTopTV.setId(R.id.tv_left_top);
        leftTopTV.setText(leftTopText);
        leftTopTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTopTextSize);
        if (leftTopTextColor != null) {
            leftTopTV.setTextColor(leftTopTextColor);
        }

        addView(leftTopTV);
    }

    /**
     * 初始化左边底部文字
     */
    private void initLeftBottomText() {
        leftBottomTV = new AppCompatTextView(mContext);
        leftBottomTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (leftImg != null) {
            leftBottomTextParams.addRule(RIGHT_OF, R.id.img_left);
        } else {
            leftBottomTextParams.addRule(ALIGN_PARENT_LEFT);
        }
        if (leftTopTV != null || leftTV != null) {
            if (leftTV != null) {
                leftBottomTextParams.addRule(BELOW, R.id.tv_left);
            } else {
                leftBottomTextParams.addRule(BELOW, R.id.tv_left_top);
            }
            leftBottomTextParams.topMargin = defaultPMValue;
        } else {
            leftBottomTextParams.addRule(ALIGN_PARENT_BOTTOM);
            leftBottomTextParams.bottomMargin = defaultPMValue;
        }
        leftBottomTextParams.setMarginStart(defaultPMValue);
        leftBottomTV.setLayoutParams(leftBottomTextParams);
        leftBottomTV.setId(R.id.tv_left_bottom);
        leftBottomTV.setText(leftBottomText);
        leftBottomTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftBottomTextSize);
        if (leftBottomTextColor != null) {
            leftBottomTV.setTextColor(leftBottomTextColor);
        }

        addView(leftBottomTV);
    }

    /**
     * 初始化中间文字
     */
    private void initCenterText() {
        centerTV = new AppCompatTextView(mContext);
        centerTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        centerTextParams.addRule(CENTER_IN_PARENT);

        centerTV.setLayoutParams(centerTextParams);
        centerTV.setId(R.id.tv_center);
        centerTV.setText(centerText);
        centerTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize);
        if (centerTextColor != null) {
            centerTV.setTextColor(centerTextColor);
        }

        addView(centerTV);
    }

    /**
     * 初始化中间顶部图片
     */
    private void initCenterTopImage() {
        centerTopImg = new AppCompatImageView(mContext);
        centerTopImgParams = new LayoutParams(centerTopImgWidth, centerTopImgHeight);
        centerTopImgParams.addRule(CENTER_HORIZONTAL);
        if (centerTV != null) {
            centerTopImgParams.addRule(ABOVE, R.id.tv_center);
            centerTopImgParams.bottomMargin = defaultPMValue;
        } else {
            centerTopImgParams.addRule(ALIGN_PARENT_TOP);
            centerTopImgParams.topMargin = defaultPMValue;
        }
        centerTopImg.setLayoutParams(centerTopImgParams);
        centerTopImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        centerTopImg.setId(R.id.img_center_top);
        if (centerTopImgRes != -1) {
            centerTopImg.setImageResource(centerTopImgRes);
        }

        addView(centerTopImg);
    }

    /**
     * 初始化中间顶部文字
     */
    private void initCenterTopText() {
        centerTopTV = new AppCompatTextView(mContext);
        centerTopTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        centerTopTextParams.addRule(CENTER_HORIZONTAL);
        if (centerTV != null) {
            centerTopTextParams.addRule(ABOVE, R.id.tv_center);
            centerTopTextParams.bottomMargin = defaultPMValue;
        } else {
            centerTopTextParams.addRule(ALIGN_PARENT_TOP);
            centerTopTextParams.topMargin = defaultPMValue;
        }
        centerTopTV.setLayoutParams(centerTopTextParams);
        centerTopTV.setId(R.id.tv_center_top);
        centerTopTV.setText(centerTopText);
        centerTopTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTopTextSize);
        if (centerTopTextColor != null) {
            centerTopTV.setTextColor(centerTopTextColor);
        }

        addView(centerTopTV);
    }

    /**
     * 初始化中间底部图片
     */
    private void initCenterBottomImage() {
        centerBottomImg = new AppCompatImageView(mContext);
        centerBottomImgParams = new LayoutParams(centerBottomImgWidth, centerBottomImgHeight);
        centerBottomImgParams.addRule(CENTER_HORIZONTAL);
        if (centerTopTV != null || centerTV != null) {
            if (centerTV != null) {
                centerBottomImgParams.addRule(BELOW, R.id.tv_center);
            } else {
                centerBottomImgParams.addRule(BELOW, R.id.tv_center_top);
            }
            centerBottomImgParams.topMargin = defaultPMValue;
        } else {
            centerBottomImgParams.addRule(ALIGN_PARENT_BOTTOM);
            centerBottomImgParams.bottomMargin = defaultPMValue;
        }
        centerBottomImg.setLayoutParams(centerBottomImgParams);
        centerBottomImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        centerBottomImg.setId(R.id.img_center_bottom);
        if (centerBottomImgRes != -1) {
            centerBottomImg.setImageResource(centerBottomImgRes);
        }

        addView(centerBottomImg);
    }

    /**
     * 初始化中间底部文字
     */
    private void initCenterBottomText() {
        centerBottomTV = new AppCompatTextView(mContext);
        centerBottomTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        centerBottomTextParams.addRule(CENTER_HORIZONTAL);
        if (centerTopImg != null || centerTV != null) {
            if (centerTV != null) {
                centerBottomTextParams.addRule(BELOW, R.id.tv_center);
            } else {
                centerBottomTextParams.addRule(BELOW, R.id.img_center_top);
            }
            centerBottomTextParams.topMargin = defaultPMValue;
        } else {
            centerBottomTextParams.addRule(ALIGN_PARENT_BOTTOM);
            centerBottomTextParams.bottomMargin = defaultPMValue;
        }
        centerBottomTV.setLayoutParams(centerBottomTextParams);
        centerBottomTV.setId(R.id.tv_center_bottom);
        centerBottomTV.setText(centerBottomText);
        centerBottomTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerBottomTextSize);
        if (centerBottomTextColor != null) {
            centerBottomTV.setTextColor(centerBottomTextColor);
        }

        addView(centerBottomTV);
    }

    /**
     * 初始化右边switch开关
     */
    private void initSwitch() {
        switchCompat = new SwitchCompat(mContext);
        rightSwitchParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightSwitchParams.addRule(CENTER_VERTICAL);
        rightSwitchParams.addRule(ALIGN_PARENT_RIGHT);
        rightSwitchParams.setMargins(0, defaultPMValue, defaultPMValue, defaultPMValue);
        switchCompat.setLayoutParams(rightSwitchParams);
        switchCompat.setId(R.id.switch_right);
        if (thumbRes != -1) {
            switchCompat.setThumbResource(thumbRes);
        }
        if (trackRes != -1) {
            switchCompat.setTrackResource(trackRes);
        }
        switchCompat.setChecked(isSwitchCheck);

        addView(switchCompat);
    }

    /**
     * 初始化右边checkBox
     */
    private void initCheckBox() {
        checkBox = new AppCompatCheckBox(mContext);
        rightCheckBoxParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightCheckBoxParams.addRule(CENTER_VERTICAL);
        rightCheckBoxParams.addRule(ALIGN_PARENT_RIGHT);
        rightCheckBoxParams.setMargins(0, defaultPMValue, defaultPMValue, defaultPMValue);
        checkBox.setLayoutParams(rightCheckBoxParams);
        checkBox.setId(R.id.check_box_right);
        if (rightCheckBoxRes != -1) {
            checkBox.setButtonDrawable(rightCheckBoxRes);
        }
        checkBox.setChecked(isBoxCheck);

        addView(checkBox);
    }

    /**
     * 初始化右边图片
     */
    private void initRightImage() {
        rightImg = new AppCompatImageView(mContext);
        rightImgParams = new LayoutParams(rightImgWidth, rightImgHeight);
        rightImgParams.addRule(CENTER_VERTICAL);
        rightImgParams.addRule(ALIGN_PARENT_RIGHT);
        rightImgParams.setMargins(0, defaultPMValue, defaultPMValue, defaultPMValue);
        rightImg.setLayoutParams(rightImgParams);
        rightImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        rightImg.setId(R.id.img_right);
        if (rightImgRes != -1) {
            rightImg.setImageResource(rightImgRes);
        }

        addView(rightImg);
    }

    /**
     * 初始化右边文字
     */
    private void initRightText() {
        rightTV = new AppCompatTextView(mContext);
        rightTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightTextParams.addRule(CENTER_VERTICAL);
        if (checkBox != null || switchCompat != null || rightImg != null) {
            if (checkBox != null) {
                rightTextParams.addRule(LEFT_OF, R.id.check_box_right);
            }
            if (switchCompat != null) {
                rightTextParams.addRule(LEFT_OF, R.id.switch_right);
            }
            if (rightImg != null) {
                rightTextParams.addRule(LEFT_OF, R.id.img_right);
            }
        } else {
            rightTextParams.addRule(ALIGN_PARENT_RIGHT);
        }
        rightTextParams.setMarginEnd(defaultPMValue);
        rightTV.setLayoutParams(rightTextParams);
        rightTV.setId(R.id.tv_right);

        rightTV.setText(rightText);
        rightTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        if (rightTextColor != null) {
            rightTV.setTextColor(rightTextColor);
        }

        addView(rightTV);
    }

    /**
     * 初始化右边顶部文字
     */
    private void initRightTopText() {
        rightTopTV = new AppCompatTextView(mContext);
        rightTopTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (checkBox != null || switchCompat != null || rightImg != null) {
            if (checkBox != null) {
                rightTopTextParams.addRule(LEFT_OF, R.id.check_box_right);
            }
            if (switchCompat != null) {
                rightTopTextParams.addRule(LEFT_OF, R.id.switch_right);
            }
            if (rightImg != null) {
                rightTopTextParams.addRule(LEFT_OF, R.id.img_right);
            }
        } else {
            rightTopTextParams.addRule(ALIGN_PARENT_RIGHT);
        }
        if (rightTV != null) {
            rightTopTextParams.addRule(ABOVE, R.id.tv_right);
            rightTopTextParams.bottomMargin = defaultPMValue;
        } else {
            rightTopTextParams.addRule(ALIGN_PARENT_TOP);
            rightTopTextParams.topMargin = defaultPMValue;
        }
        rightTopTextParams.setMarginEnd(defaultPMValue);
        rightTopTV.setLayoutParams(rightTopTextParams);
        rightTopTV.setId(R.id.tv_right_top);

        rightTopTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTopTextSize);
        if (rightTopTextColor != null) {
            rightTopTV.setTextColor(rightTopTextColor);
        }

        addView(rightTopTV);
    }

    /**
     * 初始化右边底部文字
     */
    private void initRightBottomText() {
        rightBottomTV = new AppCompatTextView(mContext);
        rightBottomTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (checkBox != null || switchCompat != null || rightImg != null) {
            if (checkBox != null) {
                rightBottomTextParams.addRule(LEFT_OF, R.id.check_box_right);
            }
            if (switchCompat != null) {
                rightBottomTextParams.addRule(LEFT_OF, R.id.switch_right);
            }
            if (rightImg != null) {
                rightBottomTextParams.addRule(LEFT_OF, R.id.img_right);
            }

        } else {
            rightBottomTextParams.addRule(ALIGN_PARENT_RIGHT);
        }
        if (rightTopTV != null || rightTV != null) {
            if (rightTV != null) {
                rightBottomTextParams.addRule(BELOW, R.id.tv_right);
            } else {
                rightBottomTextParams.addRule(BELOW, R.id.tv_right_top);
            }
            rightBottomTextParams.topMargin = defaultPMValue;
        } else {
            rightBottomTextParams.addRule(ALIGN_PARENT_BOTTOM);
            rightBottomTextParams.bottomMargin = defaultPMValue;
        }
        rightBottomTextParams.setMarginEnd(defaultPMValue);
        rightBottomTV.setLayoutParams(rightBottomTextParams);
        rightBottomTV.setId(R.id.tv_right_bottom);
        rightBottomTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightBottomTextSize);
        if (rightBottomTextColor != null) {
            rightBottomTV.setTextColor(rightBottomTextColor);
        }

        addView(rightBottomTV);
    }

    /**
     * 初始化顶部线
     */
    private void initTopLine() {
        topLine = new View(mContext);
        topLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, topLineHeight);
        topLineParams.addRule(ALIGN_PARENT_TOP);
        topLineParams.setMargins(topLineMarginLeft, 0, topLineMarginRight, 0);
        topLine.setLayoutParams(topLineParams);
        topLine.setBackgroundColor(topLineColor);

        addView(topLine);
    }

    /**
     * 初始化底部线
     */
    private void initBottomLine() {
        bottomLine = new View(mContext);
        bottomLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, bottomLineHeight);
        bottomLineParams.addRule(ALIGN_PARENT_BOTTOM);
        bottomLineParams.setMargins(bottomLineMarginLeft, 0, bottomLineMarginRight, 0);
        bottomLine.setLayoutParams(bottomLineParams);
        bottomLine.setBackgroundColor(bottomLineColor);

        addView(bottomLine);
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    private static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //给外面获取方法

    public AppCompatTextView getLeftTV() {
        if (leftTV == null) {
            initLeftText();
        }
        return leftTV;
    }

    public AppCompatTextView getLeftTopTV() {
        if (leftTopTV == null) {
            initLeftTopText();
        }
        return leftTopTV;
    }

    public AppCompatTextView getLeftBottomTV() {
        if (leftBottomTV == null) {
            initLeftBottomText();
        }
        return leftBottomTV;
    }

    public AppCompatTextView getCenterTV() {
        if (centerTV == null) {
            initCenterText();
        }
        return centerTV;
    }

    public AppCompatTextView getCenterTopTV() {
        if (centerTopTV == null) {
            initCenterTopText();
        }
        return centerTopTV;
    }

    public AppCompatTextView getCenterBottomTV() {
        if (centerBottomTV == null) {
            initCenterBottomText();
        }
        return centerBottomTV;
    }

    public AppCompatTextView getRightTV() {
        if (rightTV == null) {
            initRightText();
        }
        return rightTV;
    }

    public AppCompatTextView getRightTopTV() {
        if (rightTopTV == null) {
            initRightTopText();
        }
        return rightTopTV;
    }

    public AppCompatTextView getRightBottomTV() {
        if (rightBottomTV == null) {
            initRightBottomText();
        }
        return rightBottomTV;
    }

    public AppCompatImageView getLeftImg() {
        if (leftImg == null) {
            initLeftImage();
        }
        return leftImg;
    }

    public AppCompatImageView getRightImg() {
        if (rightImg == null) {
            initRightImage();
        }
        return rightImg;
    }

    public AppCompatImageView getCenterTopImg() {
        if (centerTopImg == null) {
            initCenterTopImage();
        }
        return centerTopImg;
    }

    public AppCompatImageView getCenterBottomImg() {
        if (centerBottomImg == null) {
            initCenterBottomImage();
        }
        return centerBottomImg;
    }

    public AppCompatCheckBox getCheckBox() {
        if (checkBox == null) {
            initCheckBox();
        }
        return checkBox;
    }

    public SwitchCompat getSwitchCompat() {
        if (switchCompat == null) {
            initSwitch();
        }
        return switchCompat;
    }
}
