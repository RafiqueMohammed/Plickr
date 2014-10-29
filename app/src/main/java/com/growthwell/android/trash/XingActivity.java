package com.growthwell.android.trash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;

import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.growthwell.android.util.Contents;
import com.growthwell.android.util.QREncoder;
import com.growthwell.android.viewitquick.R;


public class XingActivity extends Activity{

    TextView out;
    Button btn;
    ImageView image_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xing);
        out= (TextView) findViewById(R.id.txt_zxing_iutput);
        btn= (Button) findViewById(R.id.btn_zxing);
        image_view= (ImageView) findViewById(R.id.barcode_img);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
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
                Log.d("ARR","Got exception :"+e);
                }

                }
            });
        }
    }


