package com.kitchee.app.helpeo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
    private Paint pathPaint;
    private Paint outerCirclePaint;
    private Path mPath;// 用户绘制的线条
    private Path temPath;// 记录用户以前绘制过的线条



    private int radius = 50;

    // 存储的圆圈列表数据
    private List circleList;
    // 存储划线后的圆圈列表数据
    private List hitCircleList;

    // 自定义属性
    private int normalColor;
    private int selectColor;
    private int currentColor;
    private int wrongColor;
    private int lineWidth;

    // 触摸相关
    private float startX; // 上一个节点的X坐标
    private float startY; // 上一个节点的Y坐标
    private boolean isUnlocking; // 是否正在解锁（手指落下时是否刚好在一个节点上）

    public GustureLockView(Context context) {
        this(context,null);
    }

    public GustureLockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
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
        if(mWidth > mHeight){
            width = mHeight;
        }else{
            width = mWidth;
        }
        for (int i = 0 ; i < 9; i++){
            float x = width / 3 / 2 + (width / 3) * (i % 3);
            float y = width / 3 / 2 + (width / 3) * (i / 3);
            CircleBean circleBean = new CircleBean(i+1,x,y,radius);
            circleList.add(circleBean);
        }
        hitCircleList = new ArrayList();
    }

    private void initPaint() {

        circlePaint = new Paint();
        circlePaint.setColor(normalColor);
        circlePaint.setAntiAlias(true);

        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(selectColor);
        outerCirclePaint.setAntiAlias(true);

        pathPaint = new Paint();
        pathPaint.setColor(selectColor);
        pathPaint.setAntiAlias(true);//设置防锯齿
        pathPaint.setDither(true);//设置防抖动
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔笔触风格
        pathPaint.setStrokeJoin(Paint.Join.ROUND);//设置接合处的形态
        pathPaint.setStrokeWidth(lineWidth);
        mPath = new Path();
        temPath = new Path();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null){
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.GustureLockView);
            normalColor = array.getColor(R.styleable.GustureLockView_normalColor, Config.defaultNormalColor);
            selectColor = array.getColor(R.styleable.GustureLockView_selectColor, Config.defaultSelectColor);
            currentColor = array.getColor(R.styleable.GustureLockView_correctColor,Config.defaultCurrectColor);
            wrongColor = array.getColor(R.styleable.GustureLockView_wrongColor,Config.defaultWrongColor);
            lineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, R.styleable.GustureLockView_lineWidth, getResources().getDisplayMetrics());
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int a = Math.min(widthMeasureSpec,heightMeasureSpec);
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
        for (int i = 0 ; i < circleList.size(); i++){
            CircleBean circleBean = (CircleBean) circleList.get(i);
            canvas.drawCircle(circleBean.x,circleBean.y,circleBean.radius,circlePaint);
        }

        canvas.drawPath(mPath,pathPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currX = event.getX();
        float currY = event.getY();
        CircleBean circleBean = null;
        for (int i = 0 ; i < circleList.size();i++){
            circleBean = (CircleBean) circleList.get(i);
            if(circleBean.legal(currX,currY)){
                break;
            }
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(circleBean != null){
                    startY = circleBean.y;
                    startX = circleBean.x;
                    circleBean.isSelect = true;
                    hitCircleList.add(circleBean);
                    mPath.moveTo(startX,startY);
                    temPath.moveTo(startX,startY);
                    isUnlocking = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(isUnlocking){
                    //
                    mPath.reset();//为什么要重置？
                    mPath.addPath(temPath);
                    mPath.moveTo(startX,startY);
                    mPath.lineTo(event.getX(),event.getY());
                    if(circleBean != null){
                        startX = circleBean.x;
                        startY = circleBean.y;
                        circleBean.isSelect = true;
                        temPath.lineTo(startX,startY);
                        hitCircleList.add(circleBean);

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isUnlocking = false;
                if (hitCircleList.size() > 0){
                    mPath.reset();
                    mPath.addPath(temPath);

                }
                break;


        }
        invalidate();
        return true;

    }
}
