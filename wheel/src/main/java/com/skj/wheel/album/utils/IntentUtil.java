package com.skj.wheel.album.utils;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 孙科技 on 2017/4/26.
 */

public class IntentUtil {
    /**
     * 页面跳转
     *
     * @param activity
     * @param activityClazz
     * @param param
     */
    public static void startActivity(Activity activity,
                                     Class activityClazz, Map<String, Object> param) {
        Intent intent = new Intent(activity, activityClazz);
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                Object obj = entry.getValue();
                if (obj != null) {
                    if (obj instanceof String) {
                        intent.putExtra(entry.getKey(),
                                (String) entry.getValue());
                    } else if (obj instanceof Integer) {
                        intent.putExtra(entry.getKey(),
                                (Integer) entry.getValue());
                    } else {
                        try {
                            intent.putExtra(entry.getKey(),
                                    (Serializable) entry.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        activity.startActivity(intent);
    }

    /**
     * 页面跳转且带返回结果的
     *
     * @param activity
     * @param activityClazz
     * @param param
     */
    public static void startActivity(Activity activity,
                                     Class activityClazz, Map<String, Object> param, int reqCode) {
        Intent intent = new Intent(activity, activityClazz);
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                Object obj = entry.getValue();
                if (obj != null) {
                    if (obj instanceof String) {
                        intent.putExtra(entry.getKey(),
                                (String) entry.getValue());
                    } else if (obj instanceof Integer) {
                        intent.putExtra(entry.getKey(),
                                (Integer) entry.getValue());
                    } else {
                        try {
                            intent.putExtra(entry.getKey(),
                                    (Serializable) entry.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        activity.startActivityForResult(intent, reqCode);
    }
}
