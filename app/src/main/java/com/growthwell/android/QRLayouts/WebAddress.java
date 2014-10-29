package com.growthwell.android.QRLayouts;



import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.util.Global;
import com.growthwell.android.viewitquick.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class WebAddress extends Fragment {


    public WebAddress() {
        // Required empty public constructor
    }


    Button btn;
    ImageView img;
    EditText url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.qr_layouts_web_address, container, false);
        btn= (Button) v.findViewById(R.id.btn_create_qr);
        img= (ImageView) v.findViewById(R.id.img_qr_view);
        url= (EditText) v.findViewById(R.id.url_txt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(url.getText().equals("")){
                    Global.alert(getActivity(),"Required Field Missing",true);
                }else{
                    Bundle b=new Bundle();
                    b.putInt(Global.QR_TYPE_ID,Global.QR_CREATE_TYPE.PLAIN);
                    b.putString(Global.QR_RAW_DATA,url.getText().toString());

                    Global.swapFragment(getActivity(),new GeneratedQRFragment(),b,Global.FRAGMENT_TAG_CREATE_QR_URL);
/*
                    Bitmap b= GeneratedQRFragment.GenerateQRBitmap(getActivity(),url.getText().toString());
                    img.setImageBitmap(b);*/
                }

            }
        });
        return v;
    }


}
