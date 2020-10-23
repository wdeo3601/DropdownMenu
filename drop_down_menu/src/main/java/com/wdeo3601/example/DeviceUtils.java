package com.wdeo3601.example;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

class DeviceUtils {

    static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}