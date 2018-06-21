package com.kitchee.app.helpeo.base;

import android.app.Activity;
import android.os.Process;

import java.util.Stack;

/**
 * Created by kitchee on 2018/6/21.
 * desc : activity管理器
 */

public class AppManager {
    private static Stack<Activity> activityStack = new Stack<>();

    /**
     * 添加activity到堆栈
     * @param activity 待压栈的activity
     */
    public static void addActivity(Activity activity){
        if(activity != null){
            activityStack.push(activity);
        }
    }

    /**
     * 从堆栈中移除指定的activity
     * @param activity 待出栈的activity
     */
    public static void removeActivity(Activity activity){
        if(activityStack.contains(activity)){
            activityStack.remove(activity);
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    /**
     * 获取当前正在显示的activity
     * @return 最后一个压入栈的activity
     */
    public static Activity getLastActivity(){
        return activityStack.lastElement();
    }

    /**
     * 结束当前的activity
     */
    public static void finishCurrentActivity(){
        Activity activity = activityStack.lastElement();
        activity.finish();
    }

    /**
     * 结束所有的activity
     */
    public static void finishAllActivity(){
        for (Activity activity: activityStack){
            activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public static void APPExit(){
        try{
            finishAllActivity();
            android.os.Process.killProcess(Process.myPid());
            System.exit(0);
        }catch (Exception e){

        }
    }




}
