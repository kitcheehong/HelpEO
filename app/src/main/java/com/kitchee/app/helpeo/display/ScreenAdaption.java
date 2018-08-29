package com.kitchee.app.helpeo.display;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Created by kitchee on 2018/6/20.
 * 在相关Activity中的onCreate方法中setContentView()后调用一下来进行屏幕的适配
 */

public class ScreenAdaption {

    private static float sNonCompatDensity;
    private static float sNonCompatScaledDensity;


    public static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application,float designWidthDP){
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if(sNonCompatDensity == 0.0f){
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaledDensity = appDisplayMetrics.scaledDensity;
            // 监听字体切换
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0){
                        sNonCompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        float defaultWidthDP = 360.0f;
        final float targetDensity = appDisplayMetrics.widthPixels / (designWidthDP <= 0 ? defaultWidthDP : designWidthDP);
        final float targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;

    }

    /**
     * 根据设计图的高维度进行适配
     * @param activity 页面
     * @param application 程序
     * @param designHeightDP 设计稿的高宽度单位：dp
     */
    public static void setCustomDensityByHeight(@NonNull Activity activity, @NonNull final Application application,float designHeightDP){
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if(sNonCompatDensity == 0.0f){
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaledDensity = appDisplayMetrics.scaledDensity;
            // 监听字体切换
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0){
                        sNonCompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        float defaultHeightDP = 640.0f;
        final float targetDensity = appDisplayMetrics.heightPixels / (designHeightDP <= 0 ? defaultHeightDP : designHeightDP);
        final float targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;

    }

    /**
     * 如果Ui设计师给定px单位设计图，那么我们需要的是它的屏幕分辨率比如720 * 1280；还有dpi,比如等于320，那么density =2;因为不给出dpi
     * 我们就无法知道它的设计稿是否按照标准设计规格来设置，先转化为dp,比如上面宽度就是360dp,高度640dp
     */
    public static void setDensityByWidthForPx(@NonNull Activity activity, @NonNull Application application, float widthPx, float dpi){
        float widthDp = widthPx / dpi;
        setCustomDensity(activity,application,widthDp);
    }




}
