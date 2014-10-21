package com.growthwell.android.viewitquick;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


public class MainActivity extends Activity {


    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;


    ImageScanner scanner;

    private static boolean barcodeScanned = false;
    private static boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camera_view);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

       /* scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    MainActivity.barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                MainActivity.previewing = true;
                    mCamera.autoFocus(autoFocusCB);

            }
        });*/
    }

    public void onPause() {
        super.onPause();

        releaseCamera();
        Log.i("ARR","OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mCamera!=null){
            mCamera.release();
            mCamera.stopPreview();

        }
        setResult(RESULT_CANCELED);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera ==null){

            setContentView(R.layout.camera_view);
            autoFocusHandler = new Handler();
            mCamera = getCameraInstance();

            mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
            FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
            preview.addView(mPreview);

        }

    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                String output="";
                for (Symbol sym : syms) {
                   output+=sym.getData();
                    barcodeScanned = true;
                }
               // ResultAction.fireIntent(MainActivity.this,output);
                Intent i = new Intent();
                i.putExtra("SCAN_RESULT",output);
                setResult(RESULT_OK,i);
                finish();
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m=getMenuInflater();
        m.inflate(R.menu.camera_preview,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.about_plickr:
                startActivity(new Intent(MainActivity.this,ABout.class));

                return true;
            case R.id.report_bug:
                startActivity(new Intent(MainActivity.this,ABout.class));

                return true;

            default: return true;
        }

    }
}
