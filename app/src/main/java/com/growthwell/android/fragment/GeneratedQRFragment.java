package com.growthwell.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.growthwell.android.util.Contents;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.L;
import com.growthwell.android.util.QREncoder;
import com.growthwell.android.plickr.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneratedQRFragment extends Fragment {

    ImageView image_view;
    ImageButton share,save_qr;
    final String filename = Math.random()+System.currentTimeMillis()+".jpg";
    public GeneratedQRFragment() {
        // Required empty public constructor
    }

    public static int getScreenDimension(Context c) {
        WindowManager manager = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        return width < height ? width : height;
    }

    public static Bitmap GenerateQRBitmap(Context c, String data, String ContentType) {
        try {

            QREncoder qc = new QREncoder(data, null, Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), GeneratedQRFragment.getScreenDimension(c));

            return qc.encodeAsBitmap();

        } catch (WriterException e) {

            Log.d("ARR", "Got exception :" + e);
            return null;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_qr, container, false);
        image_view = (ImageView) v.findViewById(R.id.GeneratedQR);
        save_qr= (ImageButton) v.findViewById(R.id.btn_save_qr);
        save_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    Bitmap b = image_view.getDrawingCache();

                    FileOutputStream ostream = getActivity().openFileOutput(filename, Context.MODE_WORLD_READABLE);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                    Global.addImageToGallery(getActivity().getFilesDir() + "/" + filename, getActivity());
                    L.t(getActivity(),"Saved at"+getActivity().getFilesDir());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        image_view.setDrawingCacheEnabled(true);
        if (getArguments().isEmpty()) {

          Log.d("ARR", "Bundle is empty");
        } else {

            switch (getArguments().getInt(Global.QR_TYPE_ID)) {
                case Global.QR_CREATE_TYPE.PLAIN:
                    createQR(getArguments().getString(Global.QR_RAW_DATA), Contents.Type.TEXT);
                    break;

                case Global.QR_CREATE_TYPE.URL:
                    createQR(getArguments().getString(Global.QR_RAW_DATA), Contents.Type.TEXT);
                    break;
                case Global.QR_CREATE_TYPE.CONTACT:
                    createQR(getArguments().getString(Global.QR_RAW_DATA), Contents.Type.CONTACT);
                    break;

            }
        }


        share = (ImageButton) v.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

               try {
                   Bitmap b = image_view.getDrawingCache();

                    FileOutputStream ostream = getActivity().openFileOutput(filename, Context.MODE_WORLD_READABLE);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(getActivity().getFileStreamPath(filename).getAbsolutePath())));
                sharingIntent.setType("image/*");
                // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });


        return v;
    }

    public void createQR(String data, String ContentType) {

        //OPTIMISE THIS DUPLICATE CREATION
        if (GenerateQRBitmap(getActivity(), data, ContentType) != null) {
            Bitmap bm = GenerateQRBitmap(getActivity(), data, ContentType);
            if (bm != null) {
                image_view.setImageBitmap(bm);
            }
        }

    }


}
