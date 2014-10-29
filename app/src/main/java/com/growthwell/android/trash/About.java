package com.growthwell.android.trash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.growthwell.android.viewitquick.R;


public class About extends Activity {

    TextView visit_growthwell;
    TextView visit_plickr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        visit_growthwell= (TextView) findViewById(R.id.visit_growthwell);
        visit_growthwell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go =new Intent();

            }
        });

        visit_plickr= (TextView) findViewById(R.id.visit_plickr);
        visit_plickr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go =new Intent();

            }
        });
    }

}
