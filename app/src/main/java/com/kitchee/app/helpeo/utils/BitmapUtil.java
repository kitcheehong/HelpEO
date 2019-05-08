package com.kitchee.app.helpeo.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by kitchee on 2019/1/10.
 * Desc: bitmap
 */

public class BitmapUtil {

    private static final String TAG = "BitmapUtil";

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        // First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 解析图片的原始的宽、高信息
        BitmapFactory.decodeResource(res,resId,options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        //
        options.inJustDecodeBounds = false;
        // Decode Bitmap with inSampleSize Set
        return BitmapFactory.decodeResource(res,resId,options);

    }

    /**
     * 计算出采样率
     * @param options
     * @param reqWidth imageView的宽度，请求的宽度
     * @param reqHeight imageView的高度，请求的高度
     * @return 合适的采样率
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int originWidth = options.outWidth;
        int originHeight = options.outHeight;
        int inSampleSize = 1;
//        // 方法一,不能保证这个采样率一定是2的指数
//        if (originHeight > reqHeight || originWidth > reqWidth){
//            int heightRate = originHeight / reqHeight;
//            int widthRate = originWidth / reqWidth;
//            if (heightRate > widthRate){
//                if (widthRate > 1){
//                    if (widthRate%2 != 0){
//                        widthRate--;
//                    }
//                    if (widthRate >= 1){
//                        inSampleSize = widthRate;
//                    }
//                }
//            }else {
//                if (heightRate > 1){
//                    if (heightRate % 2 != 0){
//                        heightRate--;
//                    }
//                    if (heightRate >= 1){
//                        inSampleSize = heightRate;
//                    }
//                }
//            }
//        }
//        Log.d(TAG, "calculateInSampleSize: 方法一 计算所得的采样率 = " + inSampleSize);
        // 方法二
        if (originHeight > reqHeight || originWidth > reqWidth){
            int halfHeight = originHeight / 2;
            int halfWidth = originWidth / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth){
                inSampleSize *= 2;
            }
        }
        Log.d(TAG, "calculateInSampleSize: 方法二 计算所得的采样率 = "+ inSampleSize);
        return inSampleSize;
    }
}
