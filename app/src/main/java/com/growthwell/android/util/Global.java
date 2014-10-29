package com.growthwell.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.growthwell.android.viewitquick.Browser;
import com.growthwell.android.viewitquick.R;
import com.growthwell.android.fragment.ScanInfoFragment;

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
    public static final String FRAGMENT_TAG_CREATE_QR_PLAIN="FRAG_CREATE_QR_PLAIN";
    public static final String FRAGMENT_TAG_CREATE_QR_SMS="FRAG_CREATE_QR_SMS";
    public static final String FRAGMENT_TAG_CREATE_QR_CONTACT="FRAG_CREATE_QR_CONTACT";

    public static final String FRAGMENT_TAG_CREATE_QR_URL="FRAG_CREATE_QR_URL";

    public static final String QR_TYPE_ID="QR_TYPE_ID";
    public static final String QR_RAW_DATA="QR_RAW_DATA";

    public static final class QR_CREATE_TYPE{

       public static final int PLAIN=1350,URL=1351,CONTACT=1352,SMS=1353,VCARD=1354,MECARD=1355;
    }

    public static final class QR_SCAN_TYPE{

        public static final int PLAIN=1450,URL=1451,CONTACT=1452,SMS=1453,VCARD=1454,MECARD=1455;
    }


    public static final String URL_REGEX = "^((http?|https)://|(www)\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+([/?].*)?$";

    public static final String VIDEO_REGEX = "^((http?|https)://|(www)\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+([/].*)+(\\.mp4|\\.3gp|\\.avi|\\.mp3)?$";
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
        ScanInfoFragment s=new ScanInfoFragment();
        Bundle b=new Bundle();
        b.putString("SCAN_RESULT",Global.res);
        s.setArguments(b);
        FragmentManager frag=a.getFragmentManager();
        frag.beginTransaction().replace(R.id.frame_container, s, Global.FRAGMENT_TAG_SCAN).commit();
    }

    public static void swapFragment(Context c,Fragment frag,Bundle data,String tag){
        Activity a=(Activity)c;
        FragmentManager fM=a.getFragmentManager();
        frag.setArguments(data);
        fM.beginTransaction().replace(R.id.frame_container,frag,tag).addToBackStack(null).commit();
    }

    public static void alert(Context c,String msg,Boolean isWarning){

        int icon;
        String title;

        if(isWarning){
            icon=android.R.drawable.stat_sys_warning;
            title="Alert";
        }else{
            icon=android.R.drawable.stat_sys_download_done;
            title="Success";
        }

        AlertDialog alertDialog = new AlertDialog.Builder(c).create();

        alertDialog.setTitle(title);

        alertDialog.setMessage(msg);

        alertDialog.setIcon(icon);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public static class showDialog{
        AlertDialog d;
        DialogListener listen;
        public showDialog(Context c,DialogListener l,Bundle data){
            if(data==null){
                return;
            }
            String title=data.getString("title");
            String msg=data.getString("msg");
            String pos=data.getString("positive");
            String neg=data.getString("negative");
           final int type=((data.getInt("TYPE"))>0)?data.getInt("TYPE"):0;
            final String dataPackage=(!data.getString("DATA").equals(""))?data.getString("DATA"):"";
            listen=l;
            Fragment c2=(Fragment)l;
            c=((Fragment) l).getActivity();
            d=new AlertDialog.Builder(c).create();
            d.setTitle(title);
            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
            d.setMessage(msg);
            d.setButton(AlertDialog.BUTTON_NEGATIVE,neg,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            d.setButton(AlertDialog.BUTTON_POSITIVE,pos,new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                   Bundle res= new Bundle();
                    res.putInt("TYPE",type);
                    res.putString("DATA",dataPackage);
                listen.response(true,res);

                }
            });
        }
        public void show(){
            d.show();
        }
        public void dismiss(){
            d.dismiss();
        }

        public static interface DialogListener{
            public void response(boolean res,Bundle data);

        }

    }

}
