package com.growthwell.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.zxing.Result;
import com.google.zxing.client.result.VCardResultParser;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.L;
import com.growthwell.android.viewitquick.Browser;
import com.growthwell.android.viewitquick.MainActivity;
import com.growthwell.android.viewitquick.R;

import java.util.List;
import java.util.regex.Pattern;

import ezvcard.Ezvcard;
import ezvcard.VCard;

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
                    Bundle b=new Bundle();
                    b.putString("title","Contact Address Found");
                    b.putString("msg","Do you want to view this contact?");
                    b.putString("positive","Ok, Do it!");
                    b.putString("negative","No, Leave it!");
                    b.putInt("TYPE", Global.QR_SCAN_TYPE.MECARD);
                    b.putString("DATA",result);
                    d=new Global.showDialog(getActivity(),this,b);
                    d.show();
                }else if(vcard_pattern.matcher(result).find()){
                    L.c("VCARD MATCHED");
                 //   VCard v= Ezvcard.parse(result).first();
//                    L.c("VCAR NAME "+v.getFormattedNames().get(0).getValue());
                    ScanInfoFragment s = new ScanInfoFragment();
                    s.setArguments(data.getExtras());
                    getActivity().getFragmentManager()
                            .beginTransaction().replace(R.id.frame_container, s, Global.FRAGMENT_TAG_SCAN).commit();


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

                }else if(result.startsWith("WIFI:")) {

                    Bundle b=new Bundle();
                    b.putString("title","WIFI Configuration");
                    b.putString("msg","Do you want to save this wifi configuration?");
                    b.putString("positive","Yes! Save it!");
                    b.putString("negative","No! Leave it!");
                    b.putInt("TYPE", Global.QR_SCAN_TYPE.WIFI);
                    b.putString("DATA",result);

                    d=new Global.showDialog(getActivity(),this,b);
                    d.show();

              /*  wc.SSID = "\"YOUR_SSID\"";
                wc.preSharedKey = "\"YOUR_PASSWORD\"";
                wc.status = WifiConfiguration.Status.ENABLED;
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
// connect to and enable the connection
                int netId = wifiManager.addNetwork(wc);
                wifiManager.enableNetwork(netId, true);
                wifiManager.setWifiEnabled(true);*/

                }else if(result.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(result));
                    startActivity(intent);
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
        String[] split_data;
switch(data.getInt("TYPE")){
    case Global.QR_SCAN_TYPE.SMS:
        split_data=raw_data.split(":",3);
        String number=split_data[1];
        String msg="";
        if(split_data.length>2){
            msg=split_data[2];
        }


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

    case Global.QR_SCAN_TYPE.MECARD:
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        raw_data=raw_data.replace("MECARD:","");
        split_data=raw_data.split(";");
        for(String sp:split_data){
            if(sp.startsWith("N:")){
                intent.putExtra(ContactsContract.Intents.Insert.NAME, sp.replace("N:",""));
            }else if(sp.startsWith("TEL:")){
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, sp.replace("TEL:",""));
            }else if(sp.startsWith("EMAIL:")){
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, sp.replace("EMAIL:",""));
            }else if(sp.startsWith("EMAIL:")){
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, sp.replace("EMAIL:",""));
            }/*else if(sp.startsWith("URL:")){
                intent.putExtra(ContactsContract.Intents.Insert.DATA, sp.replace("URL:",""));
            }else if(sp.startsWith("ADR:")){
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, sp.replace("ADR:",""));
            }*/
        }


/*
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, "ar.rafiq@live.com");
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY,"Growthwell");*/

        getActivity().startActivity(intent);
        break;
    case Global.QR_SCAN_TYPE.WIFI:
        raw_data=raw_data.replace("WIFI:","");
        split_data=raw_data.split(";");
        String SSID="",PASS="",TYPE="";
        boolean isHidden=false;
        for(String r:split_data){
            L.c("SP "+r);
            if(r.startsWith("S:")){
                SSID=r.replace("S:","");
            }else if(r.startsWith("P:")){
                PASS=r.replace("P:","");
            }else if(r.startsWith("T:")){
                TYPE=r.replace("T:","");
            }else if(r.startsWith("H:")){
                isHidden=true;
            }
        }
        WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        // setup a wifi configuration
        WifiConfiguration conf = new WifiConfiguration();
        if(SSID.equals("")){
            Global.alert(getActivity(),"Bad Encoding","SSID is Missing",true);
        }else {
            conf.SSID = "\"" + SSID + "\"";

            if(!TYPE.equals("")){
                if(TYPE.equals("WPA")) {
                    conf.preSharedKey = "\""+ PASS +"\"";
                }else if(TYPE.equals("WEP")) {
                    conf.wepKeys[0] = "\"" + PASS + "\"";
                    conf.wepTxKeyIndex = 0;
                    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                }
            }else{
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }
            wifiManager.addNetwork(conf);


        }
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + SSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
        break;
}
    }
}
