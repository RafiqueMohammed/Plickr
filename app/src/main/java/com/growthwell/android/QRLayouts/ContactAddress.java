package com.growthwell.android.QRLayouts;


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

import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.plickr.Home;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.L;
import com.growthwell.android.plickr.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactAddress extends Fragment {

    Button btn;
    ImageButton pick;
    EditText in_fname, in_phone, in_email, in_website, in_company;

    public ContactAddress() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.qr_layouts_contact_address, container, false);
        in_fname = (EditText) v.findViewById(R.id.in_fname);
        in_phone = (EditText) v.findViewById(R.id.in_phone);
        in_email = (EditText) v.findViewById(R.id.in_email);
        in_website = (EditText) v.findViewById(R.id.in_website);
        in_company = (EditText) v.findViewById(R.id.in_company);
        Home parent= (Home) getActivity();
        parent.setPageTitle("Create QR for Contact Address");
        btn = (Button) v.findViewById(R.id.btn_create_qr);
        pick = (ImageButton) v.findViewById(R.id.btn_pick_contact);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickFromContact();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = "", email = "", phone = "", company = "", website = "";
                fname = in_fname.getText().toString();
                email = in_email.getText().toString();
                phone = in_phone.getText().toString();
                website = in_website.getText().toString();
                company = in_company.getText().toString();

                String business_card_raw = "MECARD:N:" + fname + ";ORG:" + company + ";TEL:" + phone + ";URL:" + website + ";EMAIL:" + email + ";NOTE:Contact;";

                Bundle b = new Bundle();
                b.putInt(Global.QR_TYPE_ID, Global.QR_CREATE_TYPE.CONTACT);
                b.putString(Global.QR_RAW_DATA, business_card_raw);

                Global.swapFragment(getActivity(), new GeneratedQRFragment(), b, Global.FRAGMENT_TAG_CREATE_QR_PLAIN);

            }
        });
        return v;
    }

    public void PickFromContact() {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, 175);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver cr = getActivity().getContentResolver();
        if (requestCode == 175) {
            if (resultCode == getActivity().RESULT_OK) {

                Uri uri = data.getData();
                String[] proj = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};
                Cursor c = getActivity().getContentResolver().query(uri, proj, null, null, null);
                c.moveToFirst();
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String display_name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String phoneNumber = "", emailAddress = "", companyName = "";

                if (hasNumber.equals("1")) {

                    Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }

                // Find Email Addresses
                Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
                while (emails.moveToNext()) {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emails.close();


/*
                Cursor address = cr.query(
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                        null, null);
                while (address.moveToNext())
                {
                    // These are all private class variables, don't forget to create them.
                    poBox      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                    street     = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                    city       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                    state      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                    postalCode = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                    country    = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                    type       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                }  //address.moveToNext()
*/
                L.c("contact id" + contactId + " Name " + display_name + " companyName " + companyName + " phoneNumber " + phoneNumber + " emailAddress" + emailAddress);
                in_fname.setText(display_name);
                in_email.setText(emailAddress);
                in_phone.setText(phoneNumber);


            } else {
                L.t(getActivity(), "Result no");
            }
        }
    }
}
