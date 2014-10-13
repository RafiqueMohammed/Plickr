package com.growthwell.android.viewitquick;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Rafique on 11/10/2014.
 */
public class ScanInfo extends Activity {

    TextView scan_output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_info);
        scan_output= (TextView) findViewById(R.id.qr_txt_result_output);
        Intent i=getIntent();
        String out="";
        if(i.getStringExtra("ScanResult")!=null){
            out=i.getStringExtra("ScanResult");
        }else{
            out="Scan result is empty";
        }

        scan_output.setText(out);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m=getMenuInflater();
        m.inflate(R.menu.main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.copy_clipboard:

                               //place your TextView's text in clipboard
                final ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if(!scan_output.getText().equals("")){
                    clipboard.setText(scan_output.getText().toString());
                    Toast.makeText(ScanInfo.this,"Copied Successful",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ScanInfo.this,"Blank cannot be copied",Toast.LENGTH_LONG).show();
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
