package com.growthwell.android.QRLayouts;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.util.Global;
import com.growthwell.android.viewitquick.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class SMSLayout extends Fragment {

    EditText msg,number;
    Button btn;

    public SMSLayout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.qr_layouts_sms, container, false);
        msg= (EditText) v.findViewById(R.id.in_msg);
        number= (EditText) v.findViewById(R.id.in_phone);

        btn= (Button) v.findViewById(R.id.btn_create_qr);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number.getText().toString().equals("")||msg.getText().toString().equals("")){
                    Global.alert(getActivity(),"All Fields are Required",true);
                }else{
                    Bundle b=new Bundle();
                    b.putInt(Global.QR_TYPE_ID, Global.QR_CREATE_TYPE.PLAIN);
                    String Convert="SMSTO:"+number.getText()+":"+msg.getText();

                    b.putString(Global.QR_RAW_DATA,Convert);

                   Global.swapFragment(getActivity(),new GeneratedQRFragment(),b,Global.FRAGMENT_TAG_CREATE_QR_SMS);
                }
            }
        });
        return v;
    }


}
