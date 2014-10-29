package com.growthwell.android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.encoder.QRCode;
import com.growthwell.android.util.Contents;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.L;
import com.growthwell.android.util.QREncoder;
import com.growthwell.android.viewitquick.Browser;
import com.growthwell.android.trash.MainActivity;
import com.growthwell.android.viewitquick.R;

import java.text.Format;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanFragment extends Fragment implements Global.showDialog.DialogListener{

    ImageButton btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        btn = (ImageButton) root.findViewById(R.id.btn_scan_camera);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanning();
            }
        });
        startScanning();
        return root;


    }


    public void startScanning() {


        Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivityForResult(i, Global.REQUEST_SCAN_RESULT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Global.REQUEST_SCAN_RESULT) {
            Global.showDialog d;
            if (resultCode == getActivity().RESULT_OK) {
                String result = data.getStringExtra("SCAN_RESULT");

                Pattern url_pattern = Pattern.compile(Global.URL_REGEX);
                Pattern mecard_pattern = Pattern.compile("(MECARD:.*?)", Pattern.DOTALL);
                Pattern vcard_pattern = Pattern.compile("(BEGIN:VCARD.*?END:VCARD)", Pattern.DOTALL);
                Pattern sms_pattern = Pattern.compile("(SMSTO:.*?)", Pattern.DOTALL);
                if (url_pattern.matcher(result).find()) {
                    Intent i = new Intent(getActivity(), Browser.class);
                    i.putExtra("url", result);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Pattern mp = Pattern.compile(Global.VIDEO_REGEX);
                    //  Matcher mm = mp.matcher(result);

                    getActivity().startActivity(i);
                } else if (mecard_pattern.matcher(result).find()) {
                    L.c("MECARD MATCHED");
                }else if(vcard_pattern.matcher(result).find()){
                    L.c("VCARD MATCHED");
                }else if(sms_pattern.matcher(result).find()){
                    L.c("SMS MATCHED");
                    Bundle b=new Bundle();
                    b.putString("title","SMS Detected");
                    b.putString("msg","Do you want to open it in SMS window?");
                    b.putString("positive","Ok, Do it!");
                    b.putString("negative","No, Leave it!");
                    b.putInt("TYPE", Global.QR_SCAN_TYPE.SMS);
                    b.putString("DATA",result);
                    d=new Global.showDialog(getActivity(),this,b);
               d.show();

                }else {
                    L.c("PLAIN TEXT");
                    ScanInfoFragment s = new ScanInfoFragment();
                    s.setArguments(data.getExtras());
                    getActivity().getFragmentManager()
                            .beginTransaction().replace(R.id.frame_container, s, Global.FRAGMENT_TAG_SCAN).commit();
                }


            }
        }
    }

    @Override
    public void response(boolean res,Bundle data) {
String raw_data=data.getString("DATA");
switch(data.getInt("TYPE")){
    case Global.QR_SCAN_TYPE.SMS:
        String[] split_data=raw_data.split(":",3);
        for(String r:split_data){
            L.c("SP "+r);

    }
        String number=split_data[1];
        String msg="";
        if(split_data.length>2){
            msg=split_data[2];
        }
        L.c("SPLIT N:"+number+" M:"+msg);

        Intent smsIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); //Need to change the build to API 19

            smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(number)));
            //smsIntent.putExtra(Intent.EXTRA_TEXT, msg);
            smsIntent.putExtra("sms_body", msg);

            if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
            {
                smsIntent.setPackage(defaultSmsPackageName);
            }


        }
        else //For early versions, do what worked for you before.
        {
            /*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", msg);
            getActivity().startActivity(sendIntent);*/
            smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", number);
            smsIntent.putExtra("sms_body",msg);


        }
        getActivity().startActivity(smsIntent);

        break;
}
    }
}
