package com.kitchee.app.helpeo.display;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import org.jetbrains.annotations.NonNls;

/**
 * Created by kitchee on 2018/6/20.
 * 在相关Activity中的onCreate方法中调用一下来进行屏幕的适配
 */

public class ScreenAdaption {

    private static float sNonCompatDensity;
    private static float sNonCompatScaledDensity;
    private static float defaultWidthDP = 360.0f;
    private static float defaultHeightDP = 640.0f;


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


}
