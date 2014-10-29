package com.growthwell.android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.growthwell.android.util.QREncoder;
import com.growthwell.android.viewitquick.Browser;
import com.growthwell.android.trash.MainActivity;
import com.growthwell.android.viewitquick.R;

import java.text.Format;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanFragment extends Fragment {

    ImageButton btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_scan,container,false);
        btn= (ImageButton) root.findViewById(R.id.btn_scan_camera);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanning();
            }
        });
        startScanning();
        return root;


    }


    public void startScanning(){


        Intent i=new Intent(getActivity().getApplicationContext(),MainActivity.class);
        startActivityForResult(i, Global.REQUEST_SCAN_RESULT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Global.REQUEST_SCAN_RESULT){
            if(resultCode==getActivity().RESULT_OK){
                String result = data.getStringExtra("SCAN_RESULT");

                Pattern p = Pattern.compile(Global.URL_REGEX);
                Matcher m = p.matcher(result);//replace with string to compare
                if (m.find()) {
                    Intent i = new Intent(getActivity(), Browser.class);
                    i.putExtra("url", result);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                  // Pattern mp = Pattern.compile(Global.VIDEO_REGEX);
                  //  Matcher mm = mp.matcher(result);

                    getActivity().startActivity(i);
                }else{

                    ScanInfoFragment s=new ScanInfoFragment();
                    s.setArguments(data.getExtras());
                    getActivity().getFragmentManager()
                            .beginTransaction().replace(R.id.frame_container, s, Global.FRAGMENT_TAG_SCAN).commit();
                }

            }
        }
    }
}
