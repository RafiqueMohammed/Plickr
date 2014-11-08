package com.growthwell.android.QRLayouts;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.plickr.Home;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.L;
import com.growthwell.android.plickr.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class WIFIConfigureFragment extends Fragment {

    Button btn;

    EditText in_ssid, in_pass;
    RadioGroup enc, hidden;

    public WIFIConfigureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.qr_layouts_wifi_configure, container, false);
        btn = (Button) v.findViewById(R.id.btn_create_qr);
        in_ssid = (EditText) v.findViewById(R.id.in_ssid);
        in_pass = (EditText) v.findViewById(R.id.in_pass);
        Home parent= (Home) getActivity();
        parent.setPageTitle("Create QR for WIFI Configuration");
        enc = (RadioGroup) v.findViewById(R.id.radio_group_encryption);
        hidden = (RadioGroup) v.findViewById(R.id.radio_group_hidden);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ssid = "", pass = "", encryption = "", hide = "";
                ssid = in_ssid.getText().toString();
                pass = in_pass.getText().toString();

                if (enc.getCheckedRadioButtonId() != -1) {
                    switch (enc.getCheckedRadioButtonId()) {
                        case R.id.radio_wep:
                            encryption = "T:WEP;";
                            break;
                        case R.id.radio_wpa:
                            encryption = "T:WPA;";
                            break;
                    }
                }
                if (hidden.getCheckedRadioButtonId() != -1) {
                    switch (hidden.getCheckedRadioButtonId()) {
                        case R.id.radio_yes:
                            hide = "H:true;";
                            break;
                    }
                }

                if (ssid.equals("")) {
                    Global.alert(getActivity(), "Required", "SSID is Required", true);
                } else {
                    ssid = "S:" + ssid + ";";
                    pass = "P:" + pass + ";";
                    String wifi_conf = "WIFI:" + ssid + pass + encryption + hide + ";";
                    L.c("WIFI CONF " + wifi_conf);
                    Bundle b = new Bundle();
                    b.putInt(Global.QR_TYPE_ID, Global.QR_CREATE_TYPE.CONTACT);
                    b.putString(Global.QR_RAW_DATA, wifi_conf);

                    Global.swapFragment(getActivity(), new GeneratedQRFragment(), b, Global.FRAGMENT_TAG_CREATE_QR_WIFI);

                }


            }
        });
        return v;
    }
}
