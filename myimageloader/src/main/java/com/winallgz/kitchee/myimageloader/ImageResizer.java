package com.winallgz.kitchee.myimageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Created by kitchee on 2019/4/28.
 * Desc: 图片压缩
 */

public class ImageResizer {

    private static final String TAG = "ImageResizer";

    public ImageResizer() {

    }

    public Bitmap decodeSampledBitmapFromResources(Resources resources, int resId, int reqWidth, int reqHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources,resId,options);

        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources,resId,options);

    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }




    private int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        if (reqHeight == 0 || reqWidth == 0){
            return 1;
        }
        int inSampleSize = 1;
        int oriWidth = options.outWidth ;
        int oriHeight = options.outHeight;

        if (oriWidth > reqWidth || oriHeight > reqHeight){
            int halfWidth = oriWidth / 2;
            int halfHeight = oriHeight / 2;

            while (halfWidth / inSampleSize >= reqWidth && halfHeight / inSampleSize >= reqHeight){
                inSampleSize *= 2;
            }
        }
        Log.d(TAG, "calculateSampleSize: inSampleSize = "+ inSampleSize);
        return inSampleSize;
    }
}
