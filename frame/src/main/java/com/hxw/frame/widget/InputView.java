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
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.hxw.frame.R;

import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 类似密码框输入,适用于字数少的,行数为1,字体大小适配控件大小
 *
 * @author hxw
 * @date 2017/11/7
 */

public class InputView extends View {

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

    private int cursorPosition;//画图光标的位置,不是text内部的光标
    private String[] textArray;
    private int boxWidth;

    private InputMethodManager imm;//软键盘
    private int inputType;

    private InputFilter mFilters ;//第一个控制长度,第二个控制内容

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
            inputType = a.getInt(R.styleable.InputView_inputType, EditorInfo.TYPE_CLASS_TEXT);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (getLayoutParams().height == WRAP_CONTENT) {
            height = Math.min(getMeasuredHeight(), dp2px(mContext, 24));
        }
        if (getLayoutParams().width == WRAP_CONTENT) {
            width = Math.min(getMeasuredWidth(), dp2px(mContext, 192));
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
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
                if (!TextUtils.isEmpty(textArray[i])) {
                    canvas.drawText(textArray[i], bright - boxWidth / 2, baseline, textPaint);
                }

                if (i == cursorPosition && showcursor()) {
                    if (TextUtils.isEmpty(textArray[i])) {
                        canvas.drawLine(bright - boxWidth / 2, height / 2 - height / 4,
                                bright - boxWidth / 2, height / 2 + height / 4, textPaint);
                    } else {
                        canvas.drawLine(bright - boxWidth / 2 + textSize / 2, height / 2 - height / 4,
                                bright - boxWidth / 2 + textSize / 2, height / 2 + height / 4, textPaint);
                    }
                }
            }
        } else {
            for (int i = 0; i < maxLength; i++) {
                Rect box = getBoxRect(boxWidth, height, dw, i);
                if (i == cursorPosition && showcursor()) {
                    backgroundSelectedDrawable.setBounds(box);
                    backgroundSelectedDrawable.draw(canvas);
                } else {
                    backgroundDrawable.setBounds(box);
                    backgroundDrawable.draw(canvas);
                }
                if (!TextUtils.isEmpty(textArray[i])) {
                    canvas.drawText(textArray[i], box.centerX(), baseline, textPaint);
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


    /**
     * 发送字符变换
     *
     * @param text  变换的字符
     * @param isAdd true时增加一个字符,false时减少一个字符
     */
    public void sendOnTextChanged(CharSequence text, boolean isAdd) {
        if (textArray == null) {
            return;
        }
        Timber.d("onTextChanged:positon=" + cursorPosition);

        if (isAdd) {
            if (cursorPosition == maxLength) {
                return;
            }
            //增加了一个字符
            textArray[cursorPosition] = text.toString().substring(0, 1);
            cursorPosition++;
        } else {
            if (cursorPosition == 0) {
                return;
            }
            //减少了一个字符
            if (cursorPosition == maxLength) {
                textArray[cursorPosition - 1] = null;
                cursorPosition--;
            } else if (TextUtils.isEmpty(textArray[cursorPosition])) {
                textArray[cursorPosition - 1] = null;
                cursorPosition--;
            } else {
                textArray[cursorPosition] = null;
            }

        }
        invalidate();
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        textArray = new String[maxLength];
        cursorPosition = 0;
        invalidate();

    }

    public void setInputType(int type) {
        this.inputType = type;
        restartInput();

    }

    public void setText(String text) {
        for (int i = 0; i < maxLength; i++) {
            if (i < text.length()) {
                textArray[i] = text.substring(i, i + 1);
            }
        }
        cursorPosition = text.length();
        invalidate();
    }

    public String getText() {
        StringBuilder builder = new StringBuilder();
        for (String str : textArray) {
            if (str != null) {
                builder.append(str);
            }
        }
        return builder.toString();
    }

    public static int dp2px(Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onCheckIsTextEditor()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float x = event.getX();
                cursorPosition = (int) x / boxWidth;
                invalidate();
                showSoftInput();
            }
        }

        return super.onTouchEvent(event);
    }

    //让这个View变成文本可编辑的状态
    @Override
    public boolean onCheckIsTextEditor() {
        return inputType != EditorInfo.TYPE_NULL;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

        outAttrs.inputType = inputType;

        if (onCheckIsTextEditor() && isEnabled()) {
            if (focusSearch(FOCUS_DOWN) != null) {
                outAttrs.imeOptions |= EditorInfo.IME_FLAG_NAVIGATE_NEXT;
            }
            if (focusSearch(FOCUS_UP) != null) {
                outAttrs.imeOptions |= EditorInfo.IME_FLAG_NAVIGATE_PREVIOUS;
            }
            InputConnection ic = new EasyInputConnection(this);
            outAttrs.initialCapsMode = ic.getCursorCapsMode(inputType);
            return ic;
        }
        return null;
    }

    private void showSoftInput() {
        if (imm != null) {
            imm.viewClicked(this);
            imm.showSoftInput(this, 0);
        }
    }

    private void hideSoftInput() {
        if (imm != null && imm.isActive(this)) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    private void restartInput() {
        if (imm != null) {
            imm.restartInput(this);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        if (!enabled) {
            hideSoftInput();
        }
        super.setEnabled(enabled);

        if (enabled) {
            restartInput();
        }
    }

    /**
     * @return true 需要画 false 不需要画
     */
    private boolean showcursor() {
        return isFocused() && onCheckIsTextEditor() && isEnabled();
    }

    // TODO: 2017/12/6 inputType 完善

}
