package com.growthwell.android.viewitquick;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rafique on 11/10/2014.
 */
public class ResultAction {

    public static final String URL_REGEX = "^((http?|https)://|(www)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    public static final String VIDEO_REGEX = "^((http?|https)://|(www)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/].*)+(\\.mp4|\\.3gp|\\.avi|\\.mp3)?$";

    public static void fireIntent(Context c, String result) {
        String output = "";



        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(result);//replace with string to compare
        if (m.find()) {
            Intent i = new Intent(c,Browser.class);
            i.putExtra("url", result);

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
            Intent i = new Intent(c,ScanInfo.class);
            i.putExtra("ScanResult", result);
            Log.d("ARR", "not a url :"+result);
            c.startActivity(i);
        }
    }
}
