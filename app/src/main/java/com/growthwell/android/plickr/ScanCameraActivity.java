package com.growthwell.android.plickr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

import com.growthwell.android.util.CameraSurfaceHolder;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.L;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


public class ScanCameraActivity extends Activity implements Global.showDialog.DialogListener {


    private static boolean barcodeScanned = false;
    private static boolean previewing = true;
    static {
        System.loadLibrary("iconv");
    }
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
                String output = "";
                for (Symbol sym : syms) {
                    output += sym.getData();
                    barcodeScanned = true;
                }
                // ResultAction.fireIntent(MainActivity.this,output);
                Intent i = new Intent();
                i.putExtra("SCAN_RESULT", output);
                setResult(RESULT_OK, i);
                finish();
            }
        }
    };
    ImageScanner scanner;
    private Camera mCamera;
    private CameraSurfaceHolder mPreview;
    private Handler autoFocusHandler;
    private boolean isBackCameraExist = false;

    static int getFrontCameraId() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1; // No front-facing camera found
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {

            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {
            L.c("CAMERA_FACING_BACK exception");
        }
        return c;
    }

    public static Camera getFrontCameraInstance() {
        Camera c = null;
        try {
            if (getFrontCameraId() != -1) {
                c = Camera.open(getFrontCameraId());
            }

        } catch (Exception e) {
            L.c("CAMERA_FACING_FRONT exception");
        }
        return c;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camera_view);
        init();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        autoFocusHandler = new Handler();
        mCamera = getCamera();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraSurfaceHolder(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
if(savedInstanceState!=null){
    L.c("YOU ARE BACK FROM PAUSE");
}

    }

    private void init() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                Global.alert(this, "No Camera", "Your device doesn't have any camera to scan", true);
                isBackCameraExist = false;
                finish();
            } else {
                isBackCameraExist = false;
            }

        } else {
            isBackCameraExist = true;
            final SharedPreferences sp=getPreferences(MODE_PRIVATE);
            if(sp.getBoolean(Global.SHOW_AUTO_FOCUS_WARNING,true)){
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

                    alertDialog.setTitle(getResources().getString(R.string.no_auto_focus));

                    alertDialog.setMessage(getResources().getString(R.string.no_auto_focus_desc));

                    alertDialog.setIcon(R.drawable.ic_grey_warning);


                    alertDialog.setPositiveButton( "OK! Got it!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setNegativeButton("Don't Show it Again",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor edit=sp.edit();

                                edit.putBoolean(Global.SHOW_AUTO_FOCUS_WARNING,false);
                                L.c("You have checked! its is disabled now");

                            edit.commit();
                        }
                    });
                    alertDialog.show();
                }
            }


        }
    }

    public void onPause() {
        super.onPause();

        releaseCamera();
        Log.i("ARR", "OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCamera != null) {
            mCamera.release();
            mCamera.stopPreview();

        }
        setResult(RESULT_CANCELED);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {

            setContentView(R.layout.camera_view);
            autoFocusHandler = new Handler();
            mCamera = getCamera();

            mPreview = new CameraSurfaceHolder(this, mCamera, previewCb, autoFocusCB);
            FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
            preview.addView(mPreview);

        }

    }

    private Camera getCamera() {
        if (isBackCameraExist) {
            return getCameraInstance();
        } else {
            return getFrontCameraInstance();
        }

    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };



    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    @Override
    public void response(boolean res, Bundle data) {

    }
}
