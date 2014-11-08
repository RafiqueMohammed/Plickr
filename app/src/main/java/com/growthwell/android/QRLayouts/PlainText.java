package com.growthwell.android.QRLayouts;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.plickr.Home;
import com.growthwell.android.util.Global;
import com.growthwell.android.plickr.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlainText extends Fragment {

    EditText text;
    Button btn;
    TextView why;

    public PlainText() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.qr_layouts_plain_text, container, false);
        text = (EditText) v.findViewById(R.id.qr_input_plain_text);
        btn = (Button) v.findViewById(R.id.btn_create_qr);
        why = (TextView) v.findViewById(R.id.recommend_why);
        Home parent= (Home) getActivity();
        parent.setPageTitle("Create QR for Plain Text");
        why.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.alert(getActivity(), getResources().getString(R.string.why), true);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.getText().toString().equals("")) {
                    Global.alert(getActivity(), "Required Field Missing", true);
                } else {
                    Bundle b = new Bundle();
                    b.putInt(Global.QR_TYPE_ID, Global.QR_CREATE_TYPE.PLAIN);
                    b.putString(Global.QR_RAW_DATA, text.getText().toString());

                    Global.swapFragment(getActivity(), new GeneratedQRFragment(), b, Global.FRAGMENT_TAG_CREATE_QR_PLAIN);
                }
            }
        });
        return v;
    }


}
