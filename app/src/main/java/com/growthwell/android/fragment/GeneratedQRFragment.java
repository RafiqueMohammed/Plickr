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
import com.growthwell.android.util.Contents;
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
        return v;
    }
    public void createQR(){
        try {
            WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 9/10;
            QREncoder qc = new QREncoder("Hello Rafique", null, Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), smallerDimension);
            Bitmap bm = qc.encodeAsBitmap();

            if (bm != null) {
                image_view.setImageBitmap(bm);
            }
        } catch (WriterException e) {
            Log.d("ARR", "Got exception :" + e);
        }
    }


}
