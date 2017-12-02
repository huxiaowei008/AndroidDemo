package com.hxw.frame.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.hxw.frame.R;

import timber.log.Timber;

/**
 * 类似密码框输入,适用于字数少的,行数为1,字体大小适配控件大小
 *
 * @author hxw
 * @date 2017/11/7
 */

public class InputView extends AppCompatEditText {

    private Context mContext;
    private ColorStateList textColor;
    private ColorStateList lineColor;

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
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint;
    private RectF rectF;
    private int strokeWidth;
    private int radius;//圆角矩形的圆角度


    private Drawable backgroundDrawable;
    private Drawable backgroundSelectedDrawable;
    private boolean isConnect;
    private boolean isFocused;

    private int cursorPosition;//光标的位置
    private String[] textArray;
    private int boxWidth;

    private InputMethodManager imm;//软键盘

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
            if (a.hasValue(R.styleable.InputView_lineColor)) {
                lineColor = a.getColorStateList(R.styleable.InputView_lineColor);
            }
            backgroundDrawable = a.getDrawable(R.styleable.InputView_textBackground);
            backgroundSelectedDrawable = a.getDrawable(R.styleable.InputView_textBackgroundSelected);
            maxLength = a.getInt(R.styleable.InputView_maxLength, 4);
            boxMargin = a.getDimensionPixelOffset(R.styleable.InputView_boxMargin, dp2px(mContext, 4));
            isConnect = a.getBoolean(R.styleable.InputView_isConnect, false);
            strokeWidth = a.getDimensionPixelOffset(R.styleable.InputView_strokeWidth, dp2px(mContext, 1));
            radius = a.getDimensionPixelOffset(R.styleable.InputView_radius, dp2px(mContext, 4));
        } finally {
            a.recycle();
        }

        //设置光标不可见
        setCursorVisible(false);
        setMaxLines(1);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        textArray = new String[maxLength];
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
        textPaint.setStrokeWidth(dp2px(mContext, 2));
        if (textColor != null) {
            textPaint.setColor(textColor.getColorForState(getDrawableState(), 0));
        }

        textPaint.setTextAlign(Paint.Align.CENTER);

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Timber.tag("InputView").d("onDraw select" + getSelectionStart());
        int width = getWidth();
        int height = getHeight();

        //大框的宽度,高度和控件一样
        boxWidth = width / maxLength;
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

        if (isConnect) {
            if (paint == null) {
                paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth);
                if (lineColor != null) {
                    paint.setColor(lineColor.getDefaultColor());
                }
                paint.setStrokeJoin(Paint.Join.ROUND);
            }
            if (rectF == null) {
                rectF = new RectF(strokeWidth / 2, strokeWidth / 2,
                        width - strokeWidth / 2, height - strokeWidth / 2);
            }

            canvas.drawRoundRect(rectF, radius, radius, paint);
            for (int i = 0; i < maxLength; i++) {
                int bright = (i + 1) * boxWidth;
                if (i < maxLength - 1) {//最后一条线不用画
                    canvas.drawLine(bright, 0, bright, height, paint);
                }
                if (i < currentTextLength) {
                    canvas.drawText(getText().toString().substring(i, i + 1), bright - boxWidth / 2, baseline, textPaint);
                }
                if (i == currentTextLength && isFocused) {
                    canvas.drawLine(bright - boxWidth / 2, height / 2 - height / 4,
                            bright - boxWidth / 2, height / 2 + height / 4, textPaint);
                }
            }

        } else {
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
                    canvas.drawText(getText().toString().substring(i, i + 1), box.centerX(), baseline, textPaint);
                }
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
        Timber.d("onTextChanged:text=" + text + "  start=" + start + "  lengthBefore=" + lengthBefore + "  lengthAfter=" + lengthAfter);
        this.currentTextLength = text.toString().length();
        if (textArray == null) {
            return;
        }
        cursorPosition = start + 1;
        Timber.d("onTextChanged:select=" + getSelectionStart());
        if (cursorPosition <= text.toString().length()) {
            String s = text.toString().substring(start, start + 1);
            Timber.d("s=" + s);
            textArray[start] = s;
            for (int i = 0; i < textArray.length; i++) {
                Timber.d("textArray" + i + ":" + textArray[i]);
            }
        }
        invalidate();
    }

    @Override
    protected boolean getDefaultEditable() {
        return true;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        textArray = new String[maxLength];
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        invalidate();
    }

    public static int dp2px(Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        isFocused = focused;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            int c = (int) x / boxWidth;
            Timber.d("c=" + c);
            for (int i = c; c < textArray.length; c++) {
                if (!TextUtils.isEmpty(textArray[i])) {
                    cursorPosition = c;
                    invalidate();
                    setSelection(cursorPosition);
                    Timber.d("重绘");
                    break;
                }
            }
            if (imm != null) {
                imm.viewClicked(this);
                imm.showSoftInput(this, 0);
            }
        }
        //这里不用返回super的方法,因为super方法里会按textView的逻辑重新设置光标位置,textView中内容的位置和现在展示的是不一样的,
        //虽然没有显示出来,但textView中内容的位置依旧保留了
        return true;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        Timber.d("selectStart:" + selStart + "  selectEnd:" + selEnd);
    }
}
