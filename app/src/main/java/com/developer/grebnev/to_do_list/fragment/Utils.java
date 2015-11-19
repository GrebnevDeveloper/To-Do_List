package com.developer.grebnev.to_do_list.fragment;

import java.text.SimpleDateFormat;

/**
 * Created by Grebnev on 19.11.2015.
 */
public class Utils {

    public static String getDate(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        return dateFormat.format(date);
    }

    public static String getTime(long time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH.MM");
        return timeFormat.format(time);
    }
}
