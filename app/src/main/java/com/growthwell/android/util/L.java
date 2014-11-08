package com.growthwell.android.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Rafique on 10/26/2014.
 */
public class L {
    public static void c(String msg) {
        Log.d("ARR", msg);
    }

    public static void t(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
    }
}
