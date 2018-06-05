/**
 *
 */
package com.kitchee.app.helpeo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.kitchee.app.helpeo.R;


/*
 *
 * File Name： ClearWriteEditText.java
 * Company： Amy information technology
 * Author：lwq
 * Date： 2016年11月2日
 * Description：输入清除文本框内容
 */
@SuppressLint("AppCompatCustomView")
public class ClearWriteEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;

    private TextIsClearFinishListener mTextIsClearFinishListener;

    private boolean isKeepAliveVisible = false;

    public ClearWriteEditText(Context context) {
        this(context, null);
    }

    public ClearWriteEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearWriteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 晃动动画
     *
     * @param counts 半秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    private void init() {
        mClearDrawable = getResources().getDrawable(R.mipmap.btn_delete);
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setCompoundDrawablePadding(10);
        setClearIconVisible(false);
        this.setOnFocusChangeListener(this);
        this.addTextChangedListener(this);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (!isKeepAliveVisible) {
            setClearIconVisible(s.length() > 0);
        }
        if (mTextIsClearFinishListener != null) {
            mTextIsClearFinishListener.onTextIsClearFinish(!(s.length() > 0));
        }

    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public void setKeepAliveDrawable(boolean isKeepAliveVisible) {
        this.isKeepAliveVisible = isKeepAliveVisible;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth()
                        - getPaddingRight() - mClearDrawable.getIntrinsicWidth())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (isKeepAliveVisible) { //常驻显示x图标
            setClearIconVisible(true);
            return;
        }

        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.startAnimation(shakeAnimation(3));
    }

    public Drawable getClearDrawable() {
        return mClearDrawable;
    }

    public void setClearDrawable(Drawable mClearDrawable) {
        this.mClearDrawable = mClearDrawable;
    }

    public void setTextIsClearFinishListener(TextIsClearFinishListener listener) {
        mTextIsClearFinishListener = listener;
    }

    public interface TextIsClearFinishListener {
        void onTextIsClearFinish(Boolean b);
    }
}
