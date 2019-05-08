package com.kitchee.app.helpeo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kitchee on 2019/5/3.
 * Desc:
 */

public class FilePathUtil {



    private static String CRASHPATH = "/CRASHLOG/";

    private static String SDCARD = Environment.getExternalStorageDirectory().getPath();

    public static String FILE_NAME = "crash";

    public static String FILE_NAME_SUFFIX = ".trace";

    public static void init(Context context){

    }

    public static String getCRASHPATH(){
        return SDCARD + CRASHPATH;
    }

    public static String getSDCARD(){
        return SDCARD;
    }

    /**
     * 判断SD卡是否被挂载
     * @return
     */
    public static boolean isSDCardMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡的根目录
     * @return
     */
    public static String getSDCardBaseDir(){
        if (isSDCardMounted()){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取SD卡的完整空间大小，返回MB
     * @return
     */
    public static long getSDCardSize(){
        if (isSDCardMounted()){
            StatFs statFs = new StatFs(getSDCardBaseDir());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                long count = statFs.getBlockCountLong();
                long size = statFs.getBlockSizeLong();
                return count * size / 1024 / 1024;
            }
            long count = statFs.getBlockCount();
            long size = statFs.getBlockSize();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 获取SD卡剩余的空间大小
     * @return
     */
    public static long getSDCardFreeSize(){
        if (isSDCardMounted()){
            StatFs sf = new StatFs(getSDCardBaseDir());
            long count;
            long size;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                count = sf.getFreeBlocksLong();
                size = sf.getBlockSizeLong();
                return count * size / 1024 / 1024;
            }
            count = sf.getFreeBlocks();
            size = sf.getBlockSize();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 获取SD卡可用空间大小
     * @return
     */
    public static long getSDCardAvailableSize(){
        if (isSDCardMounted()){
            StatFs statFs = new StatFs(getSDCardBaseDir());
            long count;
            long size;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                count = statFs.getAvailableBlocksLong();
                size = statFs.getBlockSizeLong();
                return count * size / 1024/ 1024;
            }
            count = statFs.getAvailableBlocks();
            size = statFs.getBlockSize();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 往SD卡的公有目录下保存文件
     * @param data
     * @param type
     * @param fileName
     * @return
     */
    public static boolean saveFileToSDCardPublicDir(byte[] data, String type, String fileName){
        BufferedOutputStream bos = null;
        if (isSDCardMounted()){
            File file = Environment.getExternalStoragePublicDirectory(type);
            if (file != null){
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(new File(file,fileName)));
                    bos.write(data);
                    bos.flush();
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    CloseUtils.closeQuietly(bos);
                }
            }
        }
        return false;
    }

    /**
     * 往SD卡自定义目录下保存文件
     * @param data
     * @param dir
     * @param fileName
     * @return
     */
    public static boolean saveFileToSDCardCustomDir(byte[] data,String dir,String fileName){
        BufferedOutputStream bos = null;
         if (isSDCardMounted()){
             File file = new File(getSDCardBaseDir() + File.separator + dir);
             if (!file.exists()){
                 file.mkdirs();//递归创建自定义目录
             }

             try {
                 bos = new BufferedOutputStream(new FileOutputStream(new File(file,fileName)));
                 bos.write(data);
                 bos.flush();
                 return true;
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             } finally {
                 CloseUtils.closeQuietly(bos);
             }
         }
         return false;
    }

    public static boolean saveFileToSDCardPrivateFilesDir(byte[] data, String type, String fileName,Context context){
        BufferedOutputStream bos = null;
        if (isSDCardMounted()){
            File file = context.getExternalFilesDir(type);
            if (file != null){
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(new File(file,fileName)));
                    bos.write(data);
                    bos.flush();
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeQuietly(bos);
                }
            }
        }
        return false;
    }

    // 往SD卡的私有Cache目录下保存文件
    public static boolean saveFileToSDCardPrivateCacheDir(byte[] data, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CloseUtils.closeQuietly(bos);
            }
        }
        return false;
    }

    // 保存bitmap图片到SDCard的私有Cache目录
    public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap, String fileName, Context context) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            // 获取私有的Cache缓存目录
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                if (fileName != null
                        && (fileName.contains(".png") || fileName.contains(".PNG"))) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                }
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // 从SD卡获取文件
    public static byte[] loadFileFromSDCard(String fileDir) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bis = new BufferedInputStream(new FileInputStream(new File(fileDir)));
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            while ((c = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 从SDCard中寻找指定目录下的文件，返回Bitmap
    public Bitmap loadBitmapFromSDCard(String filePath) {
        byte[] data = loadFileFromSDCard(filePath);
        if (data != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bm != null) {
                return bm;
            }
        }
        return null;
    }

    public static String getSDCardPublicDir(String type){
        return Environment.getExternalStoragePublicDirectory(type).getPath();
    }

    public static String getSDCardPrivateCacheDir(Context context){
        return context.getExternalCacheDir().getAbsolutePath();
    }

    public static String getSDCardPrivateFilesDir(Context context, String type){
        return context.getExternalFilesDir(type).getAbsolutePath();
    }
    //判断文件是否存在
    public static boolean isFileExist(String filePath){
        File file = new File(filePath);
        return file.isFile();
    }

    public static boolean removeFileFromSDCard(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            try {
                file.delete();
                return true;
            }catch (Exception e){
                return false;
            }
        }else {
            return false;
        }
    }



}
