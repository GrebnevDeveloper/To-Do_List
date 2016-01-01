package com.developer.grebnev.to_do_list;

import android.app.Application;

/**
 * Created by Grebnev on 01.01.2016.
 */
public class MyApplication extends Application {

    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}
