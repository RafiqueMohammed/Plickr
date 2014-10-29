package com.growthwell.android.QRLayouts;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.util.Global;
import com.growthwell.android.viewitquick.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContactAddress extends Fragment {

    Button btn;
    EditText in_fname,in_lname,in_mobile,in_landline,in_email,in_website,in_company;

    public ContactAddress() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.qr_layouts_contact_address, container, false);
        in_fname= (EditText) v.findViewById(R.id.in_fname);
        in_lname= (EditText) v.findViewById(R.id.in_lname);
        in_mobile= (EditText) v.findViewById(R.id.in_mobile);
        in_landline= (EditText) v.findViewById(R.id.in_landline);
        in_email= (EditText) v.findViewById(R.id.in_email);
        in_website= (EditText) v.findViewById(R.id.in_website);
        in_company= (EditText) v.findViewById(R.id.in_company);
        btn= (Button) v.findViewById(R.id.btn_create_qr);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname="",lname="",email="",mobile="",landline="",company="",website="",address="";
                fname=in_fname.getText().toString();
                lname=in_lname.getText().toString();
                email=in_email.getText().toString();
                mobile=in_mobile.getText().toString();
                landline=in_landline.getText().toString();
                website=in_website.getText().toString();
                company=in_company.getText().toString();

                String business_card_raw="MECARD:N:"+fname+" "+lname+";ORG:"+company+";TEL:"+mobile+";URL:"+website+";EMAIL:"+email+";ADR:"+address+";NOTE:Contact;";

                Bundle b=new Bundle();
                    b.putInt(Global.QR_TYPE_ID,Global.QR_CREATE_TYPE.CONTACT);
                    b.putString(Global.QR_RAW_DATA,business_card_raw);

                    Global.swapFragment(getActivity(),new GeneratedQRFragment(),b,Global.FRAGMENT_TAG_CREATE_QR_PLAIN);

            }
        });
        return v;
    }


}
