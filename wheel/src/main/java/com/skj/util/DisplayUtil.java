package com.skj.util;

import android.content.Context;
import android.util.DisplayMetrics;


/**
 * Created by 孙科技 on 2017/4/26.
 */

public class DisplayUtil {
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static float dip2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (float) (((double) displayMetrics.density + 0.5D) * (double) dp);
    }

    public static float px2dip(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (float) ((double) px / ((double) displayMetrics.density + 0.5D));
    }
}
