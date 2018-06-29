package com.kitchee.app.helpeo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.appCommon.Config;
import com.kitchee.app.helpeo.bean.CircleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kitchee on 2018/6/28.
 * desc : 手势解锁控件
 */

public class GustureLockView extends View {

    // 控件宽，高
    private int mWidth;
    private int mHeight;

    // 绘制相关
    private Paint circlePaint;
    private Paint circleNorPaint;
    private Paint circleSelPaint;
    private Paint circleCorPaint;
    private Paint circleWorPaint;
    private Paint pathPaint;
    private Paint outerCirclePaint;

    private Path mPath;// 用户绘制的线条
    private Path temPath;// 记录用户以前绘制过的线条


    private int radius = 50;

    // 存储的圆圈列表数据
    private ArrayList<CircleBean> circleList;
    // 存储划线后的圆圈列表数据
    private List<CircleBean> hitCircleList;
    // 第一次存储后的圆圈列表数据
    private List<CircleBean> firstCircleList;

    // 自定义属性
    private int normalColor;
    private int selectColor;
    private int correctColor;
    private int wrongColor;
    private int lineWidth;

    // 触摸相关
    private float startX; // 上一个节点的X坐标
    private float startY; // 上一个节点的Y坐标
    private boolean isUnlocking; // 是否正在解锁（手指落下时是否刚好在一个节点上）

    private setLockListener lockListener;
    private boolean enableAutoClear = true;
    private boolean isFirstSetting = true;

    private Canvas mCanvas;

    public GustureLockView(Context context) {
        this(context, null);
    }

    public GustureLockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GustureLockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 根据属性初始化
        initAttrs(attrs);
        // 初始化画笔
        initPaint();

    }

    private void initData() {
        int width;
        circleList = new ArrayList();
        if (mWidth > mHeight) {
            width = mHeight;
        } else {
            width = mWidth;
        }
        for (int i = 0; i < 9; i++) {
            float x = width / 3 / 2 + (width / 3) * (i % 3);
            float y = width / 3 / 2 + (width / 3) * (i / 3);
            CircleBean circleBean = new CircleBean(i + 1, x, y, radius);
            circleList.add(circleBean);
        }
        hitCircleList = new ArrayList();
    }

    private void initPaint() {

        circlePaint = new Paint();
        circlePaint.setColor(normalColor);
        circlePaint.setAntiAlias(true);

        circleNorPaint = new Paint();
        circleNorPaint.setColor(normalColor);
        circleNorPaint.setAntiAlias(true);

        circleSelPaint = new Paint();
        circleSelPaint.setColor(selectColor);
        circleSelPaint.setAntiAlias(true);

        circleCorPaint = new Paint();
        circleCorPaint.setColor(correctColor);
        circleCorPaint.setAntiAlias(true);

        circleWorPaint = new Paint();
        circleWorPaint.setColor(wrongColor);
        circleWorPaint.setAntiAlias(true);

        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.TRANSPARENT);//刚开始是透明
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setStrokeWidth(lineWidth);
        outerCirclePaint.setStyle(Paint.Style.STROKE);//空心效果

        pathPaint = new Paint();
        pathPaint.setColor(selectColor);
        pathPaint.setAntiAlias(true);//设置防锯齿
        pathPaint.setDither(true);//设置防抖动
        pathPaint.setStyle(Paint.Style.STROKE);//
        pathPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔笔触风格
        pathPaint.setStrokeJoin(Paint.Join.ROUND);//设置接合处的形态
        pathPaint.setStrokeWidth(lineWidth);
        mPath = new Path();
        temPath = new Path();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.GustureLockView);
            normalColor = array.getColor(R.styleable.GustureLockView_normalColor, Config.defaultNormalColor);
            selectColor = array.getColor(R.styleable.GustureLockView_selectColor, Config.defaultSelectColor);
            correctColor = array.getColor(R.styleable.GustureLockView_correctColor, Config.defaultCorrectColor);
            wrongColor = array.getColor(R.styleable.GustureLockView_wrongColor, Config.defaultWrongColor);
            lineWidth = (int) array.getDimension(R.styleable.GustureLockView_lineWidth, Config.defaultLineWidth);
//            lineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, R.styleable.GustureLockView_lineWidth, getResources().getDisplayMetrics());
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int a = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(a, a);
        // 获取到控件的宽高属性值
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化圆圈数据
        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        for (int i = 0; i < circleList.size(); i++) {
            CircleBean circleBean = (CircleBean) circleList.get(i);
            drawCircle(circleBean, canvas);
        }

        canvas.drawPath(mPath, pathPaint);
    }

    private void drawCircle(CircleBean circleBean, Canvas canvas) {
        switch (circleBean.state) {
            case Config.POINT_STATE_NORMAL:
                circlePaint.setColor(Config.defaultNormalColor);
                outerCirclePaint.setColor(Color.TRANSPARENT);
                canvas.drawCircle(circleBean.x, circleBean.y, circleBean.radius, circlePaint);
                canvas.drawCircle(circleBean.x, circleBean.y, circleBean.radius * 2, outerCirclePaint);
                break;
            case Config.POINT_STATE_SELECTED:
//                circlePaint.setColor(Config.defaultSelectColor);
                outerCirclePaint.setColor(Config.defaultSelectColor);
                canvas.drawCircle(circleBean.x, circleBean.y, circleBean.radius, circleSelPaint);
                canvas.drawCircle(circleBean.x, circleBean.y, circleBean.radius * 2, outerCirclePaint);
                break;
            case Config.POINT_STATE_SUCCESS:
//                circlePaint.setColor(Config.defaultCorrectColor);
                outerCirclePaint.setColor(Config.defaultCorrectColor);
                canvas.drawCircle(circleBean.x, circleBean.y, circleBean.radius, circleCorPaint);
                canvas.drawCircle(circleBean.x, circleBean.y, circleBean.radius * 2, outerCirclePaint);
                break;
            case Config.POINT_STATE_WRONG:
//                circlePaint.setColor(Config.defaultWrongColor);
                outerCirclePaint.setColor(Config.defaultWrongColor);
                canvas.drawCircle(circleBean.x, circleBean.y, circleBean.radius, circleWorPaint);
                canvas.drawCircle(circleBean.x, circleBean.y, circleBean.radius * 2, outerCirclePaint);
                break;

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currX = event.getX();
        float currY = event.getY();
        CircleBean circleBean = null;
        for (int i = 0; i < circleList.size(); i++) {
            circleBean = (CircleBean) circleList.get(i);
            if (circleBean.legal(currX, currY)) {
                break;
            } else {
                circleBean = null;
            }
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (circleBean != null) {
                    startY = circleBean.y;
                    startX = circleBean.x;
                    circleBean.state = Config.POINT_STATE_SELECTED;
                    hitCircleList.add(circleBean);
                    mPath.moveTo(startX, startY);
                    temPath.moveTo(startX, startY);

                    isUnlocking = true;
                    setWholePathState(circleBean.state);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isUnlocking) {
                    //
                    mPath.reset();//
                    mPath.addPath(temPath);
                    mPath.lineTo(event.getX(), event.getY());
                    if (circleBean != null) {
                        startX = circleBean.x;
                        startY = circleBean.y;
                        circleBean.state = Config.POINT_STATE_SELECTED;
                        if (!hitCircleList.contains(circleBean)) {
                            mPath.moveTo(startX, startY);
                            temPath.lineTo(startX, startY);
                            hitCircleList.add(circleBean);

                        }

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isUnlocking = false;
                if (hitCircleList.size() > 0) {
                    mPath.reset();
                    mPath.addPath(temPath);
                    checkPathLegal();
                    if (this.enableAutoClear) {
                        startTimer();
                    }
                }

                break;


        }
        invalidate();
        return true;

    }

    /**
     * 根据状态（解锁成功/失败）改变整条路径上所有元素的颜色
     *
     * @param state 状态（解锁成功/失败）
     */
    private void setWholePathState(int state) {
//        circlePaint.setColor(getColorByState(state));
        for (CircleBean rect : hitCircleList) {
            rect.state = (state);
        }
        pathPaint.setColor(getColorByState(state));

        if (state == Config.POINT_STATE_NORMAL) {
            outerCirclePaint.setColor(Color.TRANSPARENT);

        } else {
            outerCirclePaint.setColor(getColorByState(state));
        }
    }

    /**
     * 通过状态得到应显示的颜色
     *
     * @param state 状态
     * @return 给定状态下应该显示的颜色
     */
    private int getColorByState(int state) {
        int color = normalColor;
        switch (state) {
            case Config.POINT_STATE_NORMAL:
                color = normalColor;
                break;
            case Config.POINT_STATE_SELECTED:
                color = selectColor;
                break;
            case Config.POINT_STATE_SUCCESS:
                color = correctColor;
                break;
            case Config.POINT_STATE_WRONG:
                color = wrongColor;
                break;
        }
        return color;
    }

    /**
     * 检测划线是否合法
     */
    private void checkPathLegal() {
        if (hitCircleList.size() > 3) {
            if (isFirstSetting) {

                lockListener.onSetLockSuccess(1);
                if (firstCircleList == null) {
                    firstCircleList = new ArrayList<>();
                } else {
                    firstCircleList.clear();
                }
                firstCircleList.addAll(hitCircleList);
                isFirstSetting = false;
            } else {
                // 校验两次设置图案是否一致
                checkUpPathSame();
            }
        } else {
            setWholePathState(Config.POINT_STATE_WRONG);
            lockListener.onSetLockFail("至少连接4个点，请重新绘制");
        }

    }

    private void checkUpPathSame() {

        if (hitCircleList.size() == firstCircleList.size()) {
            boolean isSuccess = false;
            for (int i = 0; i < hitCircleList.size(); i++) {
                if (!hitCircleList.get(i).equals(firstCircleList.get(i))) {
                    setWholePathState(Config.POINT_STATE_WRONG);
                    lockListener.onSetLockFail("与上次绘制不一致，请重新绘制");
                    isSuccess = false;
                    break;
                } else {
                    isSuccess = true;
                }
            }
            if (isSuccess) {
                setWholePathState(Config.POINT_STATE_SUCCESS);
                lockListener.onSetLockSuccess(2);
            }
        } else {
            setWholePathState(Config.POINT_STATE_WRONG);
            lockListener.onSetLockFail("与上次绘制不一致，请重新绘制");
        }
    }

    /**
     * 重置所有元素回到初始状态
     */
    private void reset() {

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mCanvas.drawPaint(p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    public interface setLockListener {
        void onSetLockSuccess(int type);

        void onSetLockFail(String msg);
    }

    public void setOnLockListener(setLockListener listener) {
        this.lockListener = listener;
    }

    //设置自动清除划线
    private final Runnable clearAction = new Runnable() {
        @Override
        public void run() {
            setEnabled(true);
            clearHitData();
        }
    };

    private void clearHitData() {
        mPath.reset();
        temPath.reset();
        hitCircleList.clear();
        setWholePathState(Config.POINT_STATE_NORMAL);
        invalidate();
        postInvalidate();
//        reset();
    }

    private void startTimer() {
        setEnabled(false);
        this.postDelayed(this.clearAction, Config.defaultDelayTime);
    }
}
