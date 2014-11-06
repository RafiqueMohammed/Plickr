package com.growthwell.android.QRLayouts;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.L;
import com.growthwell.android.viewitquick.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class SMSLayout extends Fragment {

    EditText msg,number;
    TextView why;
    Button btn;
    ImageButton pick;
    String phoneNumber="";
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
        why= (TextView) v.findViewById(R.id.recommend_why);
        why.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.alert(getActivity(),getResources().getString(R.string.why),true);
            }
        });
        pick= (ImageButton) v.findViewById(R.id.btn_pick_contact);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickFromContact();
            }
        });
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
    public void PickFromContact(){
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i,165);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver cr=getActivity().getContentResolver();
        if(requestCode==165){
            if(resultCode==getActivity().RESULT_OK){

                Uri uri=data.getData();
                String[] proj={ContactsContract.Contacts._ID,ContactsContract.Contacts.HAS_PHONE_NUMBER};
                Cursor c=getActivity().getContentResolver().query(uri,proj,null,null,null);
                c.moveToFirst();
                String contactId=c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                String hasNumber=c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));


                if (hasNumber.equals("1"))
                {

                    Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                    while (phones.moveToNext())
                    {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                    number.setText(phoneNumber);
                }else{
                    Global.alert(getActivity(),"This Contact has No Phone Number.",true);
                }

            }else{
                L.t(getActivity(),"Result no");
            }
        }
    }

}
