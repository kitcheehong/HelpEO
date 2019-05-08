package com.kitchee.app.helpeo.appCommon;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.kitchee.app.helpeo.utils.FilePathUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kitchee on 2019/5/3.
 * Desc: 自定义异常处理器：保存相关错误信息到SD卡或者内置存储，并且发送到服务器
 */

public class MyCrashHandler implements Thread.UncaughtExceptionHandler {


    private static final String TAG = "MyCrashHandler";
    private static final boolean DEBUG = true;

    private static MyCrashHandler sInstance = new MyCrashHandler();

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;
    private MyCrashHandler(){

    }

    public static MyCrashHandler getInstance(){
        return  sInstance;
    }


    public void init(Context context){
       mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();

       Thread.setDefaultUncaughtExceptionHandler(this);
       mContext = context.getApplicationContext();
    }

    /**
     * 当程序中有未被捕获的异常，系统自动调用uncaughtException
     * @param t 出现未捕获异常的线程，
     * @param e 捕获的异常
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //导出异常信息到SD卡中
        dumpExceptionToExternalStorage(e);
        //上传异常信息到服务器
        uploadExceptionToServer();

//        e.printStackTrace();
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler != null){
            mDefaultCrashHandler.uncaughtException(t,e);
        }else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void uploadExceptionToServer() {
    }

    private void dumpExceptionToExternalStorage(Throwable e) {
        if (!FilePathUtil.isSDCardMounted()){
            if (DEBUG){
                Log.w(TAG, "dumpExceptionToExternalStorage: sdcard unmounted skip dump " );
            }
            return;
        }

        File dir = new File(FilePathUtil.getCRASHPATH());
        if (!dir.exists()){
            dir.mkdirs();
        }
        //记录当前时间
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        File file = new File(FilePathUtil.getCRASHPATH() + FilePathUtil.FILE_NAME+time+FilePathUtil.FILE_NAME_SUFFIX);

        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            printWriter.println(time);
            dumpPhoneInfo(printWriter);
            printWriter.println();
            e.printStackTrace(printWriter);
            printWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void dumpPhoneInfo(PrintWriter printWriter) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
        //APP 版本号
        printWriter.print("APP VersionName: ");
        printWriter.print(packageInfo.versionName);
        printWriter.print("_VersionCode: ");
        printWriter.println(packageInfo.versionCode);

        //Android 版本号
        printWriter.print("OS Version: ");
        printWriter.print(Build.VERSION.RELEASE);
        printWriter.print("_");
        printWriter.println(Build.VERSION.SDK_INT);

        //手机制造商
        printWriter.print("Vendor: ");
        printWriter.println(Build.MANUFACTURER);

        //手机型号
        printWriter.print("Model: ");
        printWriter.println(Build.MODEL);

        //CPU架构
        printWriter.print("CPU ABI: ");
        printWriter.println(Build.CPU_ABI);

    }


}
