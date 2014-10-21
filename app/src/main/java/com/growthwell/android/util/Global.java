package com.growthwell.android.util;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.growthwell.android.viewitquick.Browser;
import com.growthwell.android.viewitquick.Home;
import com.growthwell.android.viewitquick.R;
import com.growthwell.android.viewitquick.ScanInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rafique on 18/10/2014.
 */
public class Global {
    public static final int REQUEST_SCAN_RESULT=92;
    public static final String FRAGMENT_TAG_ABOUT="FRAG_ABOUT";
    public static final String FRAGMENT_TAG_HOME="FRAG_HOME";
    public static final String FRAGMENT_TAG_SCAN="FRAG_SCAN";
    public static final String FRAGMENT_TAG_LOGIN="FRAG_LOGIN";
    public static final String FRAGMENT_TAG_CREATE="FRAG_CREATE";

    public static final String URL_REGEX = "^((http?|https)://|(www)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    public static final String VIDEO_REGEX = "^((http?|https)://|(www)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/].*)+(\\.mp4|\\.3gp|\\.avi|\\.mp3)?$";

private static String res="";
    public static void fireIntent(Context c, String result) {
        String output = "";

        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(result);//replace with string to compare
        if (m.find()) {
            Intent i = new Intent(c,Browser.class);
            i.putExtra("url", result);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Pattern mp = Pattern.compile(VIDEO_REGEX);
            Matcher mm = mp.matcher(result);

            output = result + "\n The String Contains URL";
            if (mm.find()) {
                output = result + "\n Its a Video URL";
            } else {
                output += "\n and Its not Video URL";
            }
            Log.d("ARR", output);
            c.startActivity(i);
        }else{

Global.start((Activity)c);

/*
            Intent i = new Intent(c,ScanInfo.class);
            i.putExtra("ScanResult", result);
            Log.d("ARR", "not a url :"+result);
            c.startActivity(i);*/
        }
    }


    public static void start(Activity a) {
        ScanInfo s=new ScanInfo();
        Bundle b=new Bundle();
        b.putString("SCAN_RESULT",Global.res);
        s.setArguments(b);
        FragmentManager frag=a.getFragmentManager();
        frag.beginTransaction().replace(R.id.frame_container, s, Global.FRAGMENT_TAG_SCAN).commit();
    }
}
