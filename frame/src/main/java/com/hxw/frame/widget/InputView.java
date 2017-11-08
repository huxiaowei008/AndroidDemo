package com.hxw.frame.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.hxw.frame.R;

import timber.log.Timber;

/**
 * 类似密码框输入,适用于字数少的,行数为1,字体大小适配控件大小
 *
 * @author hxw
 * @date 2017/11/7
 */

public class InputView extends AppCompatTextView {

    private Context mContext;
    private ColorStateList textColor;

    /**
     * 字体大小
     */
    private float textSize;
//    /**
//     * 每个字的常态背景资源id
//     */
//    private int textBackgroundId;
//    /**
//     * 每个字的选中背景资源id
//     */
//    private int textBackgroundSelectedId;

    private int maxLength;//最大字符数
    private int boxMargin; //字符方块的margin

    private int currentTextLength;
    private String text;
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private Drawable backgroundDrawable;
    private Drawable backgroundSelectedDrawable;


    public InputView(Context context) {
        this(context, null);
    }

    public InputView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputView);

        try {
            if (a.hasValue(R.styleable.InputView_textColor)) {
                textColor = a.getColorStateList(R.styleable.InputView_textColor);
            }
            backgroundDrawable = a.getDrawable(R.styleable.InputView_textBackground);
            backgroundSelectedDrawable = a.getDrawable(R.styleable.InputView_textBackgroundSelected);
            maxLength = a.getInt(R.styleable.InputView_maxLength, -1);
            boxMargin = a.getDimensionPixelOffset(R.styleable.InputView_boxMargin, dp2px(mContext, 4));

        } finally {
            a.recycle();
        }

        //设置光标不可见
        setCursorVisible(false);
        setMaxLines(1);
        if (maxLength > 0) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        if (backgroundDrawable == null) {
            backgroundDrawable = new ColorDrawable(Color.parseColor("#00000000"));
        }
        if (backgroundSelectedDrawable == null) {
            backgroundSelectedDrawable = new ColorDrawable(Color.parseColor("#00000000"));
        }
//        if (textBackgroundId != -1) {
//            backgroundDrawable = AppCompatResources.getDrawable(mContext, textBackgroundId);
//        }
//        if (textBackgroundSelectedId != -1) {
//            backgroundSelectedDrawable = AppCompatResources.getDrawable(mContext, textBackgroundSelectedId);
//        }

        textPaint.density = getResources().getDisplayMetrics().density;
        textPaint.setFakeBoldText(false);
        textPaint.setTextSkewX(0);
        if (textColor != null) {
            textPaint.setColor(textColor.getColorForState(getDrawableState(), 0));
        }

        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Timber.tag("InputView").d("onDraw");
        int width = getWidth();
        int height = getHeight();

        //大框的宽度,高度和控件一样
        int boxWidth = width / maxLength;
        //获取宽高最小的一边调整,使宽度小于等于高度
        int min = Math.min(boxWidth, height);
        int dw = (boxWidth - min) / 2;
        int bgSize = min - boxMargin * 2;//背景最小一边的大小
        if (bgSize <= 0) {
            return;
        }
        textSize = bgSize * 0.8f;
        textPaint.setTextSize(textSize);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();

        int baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;

        for (int i = 0; i < maxLength; i++) {
            Rect box = getBoxRect(boxWidth, height, dw, i);
            if (i == currentTextLength) {
                backgroundSelectedDrawable.setBounds(box);
                backgroundSelectedDrawable.draw(canvas);
            } else {
                backgroundDrawable.setBounds(box);
                backgroundDrawable.draw(canvas);
            }
            if (i < currentTextLength) {
                canvas.drawText(text.substring(i, i + 1), box.centerX(), baseline, textPaint);
            }
        }
    }

    /**
     * 获取背景的框大小
     *
     * @param boxWidth  大框的宽度
     * @param boxHeight 大框的高度
     * @param dw        调整数值,使宽度小于等于高度
     * @param i         框的编号
     * @return
     */
    private Rect getBoxRect(int boxWidth, int boxHeight, int dw, int i) {
        //具体的背景框上下左右
        int left = boxWidth * i + boxMargin;
        int right = boxWidth * (i + 1) - boxMargin;
        int top = boxMargin;
        int bottom = boxHeight - boxMargin;
        left += dw;
        right -= dw;
        return new Rect(left, top, right, bottom);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.currentTextLength = text.toString().length();
        this.text = text.toString();
        invalidate();
    }

    @Override
    protected boolean getDefaultEditable() {
        return true;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        invalidate();
    }

    public static int dp2px(Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }
}
