package com.growthwell.android.fragment;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.result.ResultParser;
import com.growthwell.android.util.Contents;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.QREncoder;
import com.growthwell.android.viewitquick.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class GeneratedQRFragment extends Fragment {

ImageView image_view;
    public GeneratedQRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_create_qr, container, false);
        image_view= (ImageView) v.findViewById(R.id.GeneratedQR);
        if(getArguments().isEmpty()){
            String[] contact={"This is plain text",
                    "WIFI:S:NEWAGE;T:WEP;P:0123456789;",
                    "http://youtube.com",
                    "BEGIN:VCARD\n"+"VERSION:3.0\n"+"N:Rafique\n"+"ORG:WhiteSmoke\n"+"TITLE:Contact\n"+"TEL:8976288706\n"+"EMAIL:ar.rafiq@live.com\n"+"ADR:k/b/6\n"+"END:VCARD",
                    "MECARD:N:Rafique;ORG:WhiteSmoke;TEL:8976288706;URL:www.youtube.com;EMAIL:ar.rafiq@live.com;ADR:k/b/6;NOTE:Contact;"};
            Log.d("ARR","Bundle is empty");
        }else{

            switch(getArguments().getInt(Global.QR_TYPE_ID)){
                case Global.QR_CREATE_TYPE.PLAIN:
                    createQR(getArguments().getString(Global.QR_RAW_DATA),Contents.Type.TEXT);
                    break;

                case Global.QR_CREATE_TYPE.URL:
                    createQR(getArguments().getString(Global.QR_RAW_DATA),Contents.Type.TEXT);
                    break;
                case Global.QR_CREATE_TYPE.CONTACT:
                    createQR(getArguments().getString(Global.QR_RAW_DATA),Contents.Type.CONTACT);
                    break;

            }
        }



        return v;
    }
    public static int getScreenDimension(Context c){
        WindowManager manager = (WindowManager)c.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        return width < height ? width : height;
    }

    public void createQR(String data,String ContentType){

        //OPTIMISE THIS DUPLICATE CREATION
        if(GenerateQRBitmap(getActivity(),data,ContentType)!=null){
            Bitmap bm = GenerateQRBitmap(getActivity(),data,ContentType);
            if (bm != null) {
                image_view.setImageBitmap(bm);
            }
        }

           }

    public static Bitmap GenerateQRBitmap(Context c,String data,String ContentType){
        try {

            QREncoder qc = new QREncoder(data, null, Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), GeneratedQRFragment.getScreenDimension(c));

                return qc.encodeAsBitmap();

        } catch (WriterException e) {

            Log.d("ARR", "Got exception :" + e);
            return null;
        }

    }


}
