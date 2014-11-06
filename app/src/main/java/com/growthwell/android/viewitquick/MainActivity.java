package com.growthwell.android.viewitquick;

import android.app.Activity;
import android.content.Intent;
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


public class MainActivity extends Activity {


    private Camera mCamera;
    private CameraSurfaceHolder mPreview;
    private Handler autoFocusHandler;
    private boolean isBackCameraExist=false;

    ImageScanner scanner;

    private static boolean barcodeScanned = false;
    private static boolean previewing = true;

    static {
        System.loadLibrary("iconv");
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
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);


    }

    private void init(){
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){

            if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
                Global.alert(this,"No Camera","Your device doesn't have any camera to scan",true);
                isBackCameraExist=false;
                finish();
            }else{
                isBackCameraExist=false;
            }

        }else{
            isBackCameraExist=true;
            if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)){
                Global.alert(this,"No Auto Focus Feature","Your device doesn't have the camera auto-focus feature. You can scan QR codes but small sized QR codes may fail to recognize by the app",true);
            }

        }
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
            mCamera = getCamera();

            mPreview = new CameraSurfaceHolder(this, mCamera, previewCb, autoFocusCB);
            FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
            preview.addView(mPreview);

        }

    }

    private Camera getCamera(){
if(isBackCameraExist){
    return getCameraInstance();
}else{
    return getFrontCameraInstance();
}

    }
   static int getFrontCameraId() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0 ; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1; // No front-facing camera found
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {

            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e){
            L.c("CAMERA_FACING_BACK exception");
        }
        return c;
    }

    public static Camera getFrontCameraInstance(){
        Camera c = null;
        try {
if(getFrontCameraId()!=-1){
    c = Camera.open(getFrontCameraId());
}

        } catch (Exception e){
            L.c("CAMERA_FACING_FRONT exception");
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

}
