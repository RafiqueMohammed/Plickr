package com.growthwell.android.plickr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.growthwell.android.util.Global;

public class Splash extends Activity {


    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sp=getPreferences(MODE_PRIVATE);

        if(sp.getBoolean(Global.IS_FRESH_INSTALL,true)){
            SharedPreferences.Editor edit=sp.edit();
            edit.putBoolean(Global.IS_FRESH_INSTALL,false);
            edit.putBoolean(Global.SHOW_AUTO_FOCUS_WARNING,true);
            edit.commit();
        }



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(Splash.this, Home.class));
                finish();
            }
        }, AUTO_HIDE_DELAY_MILLIS);

    }
}
