package com.skj.wheel.album.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 孙科技 on 2017/4/26.
 */

public class CacheActivityUtil {
    /**
     * 建一个容器储存每一个activity页面
     */
    public static List<Activity> activityList = new LinkedList<Activity>();

    public CacheActivityUtil() {

    }

    /**
     * 添加到Activity容器中
     */
    public static void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 遍历所有Activity并finish
     */
    public static void finishActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    /**
     * 遍历所有Activity并finish除指定activity
     */
    public static void finishOtherActivity(Class<?> cls) {
        for (Activity activity : activityList) {
            if (!activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public static void finishSingleActivityByClass(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }
        finishSingleActivity(tempActivity);
    }
}
