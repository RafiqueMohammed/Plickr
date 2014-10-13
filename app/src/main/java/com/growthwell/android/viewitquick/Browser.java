package com.growthwell.android.viewitquick;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Browser extends Activity {

    WebView browser;
    String url="";
    int progress_count=0;
    ProgressBar mLoadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_PROGRESS);
       // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminate(true);

        setContentView(R.layout.activity_browser);


        final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20); //Use dp resources


        mLoadingProgressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        mLoadingProgressBar.setIndeterminate(true);
        mLoadingProgressBar.setLayoutParams(lp);
        mLoadingProgressBar.setVisibility(View.INVISIBLE);

        final ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        decor.addView(mLoadingProgressBar);

        ViewTreeObserver observer = mLoadingProgressBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View contentView = decor.findViewById(android.R.id.content);
                mLoadingProgressBar.setY(contentView.getY() - 10);

                ViewTreeObserver observer = mLoadingProgressBar.getViewTreeObserver();
                observer.removeGlobalOnLayoutListener(this);
            }
        });


        browser= (WebView) findViewById(R.id.browser_view);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setSupportZoom(true);
        //browser.getSettings().setBuiltInZoomControls(true);
        browser.setWebViewClient(new BrowserWebClient());
        browser.setWebChromeClient(new BrowserClient());
        Intent extra=getIntent();
        if(extra!=null){
            url=extra.getStringExtra("url");
        }
if(url!=""){

    browser.loadUrl(url);
}else{
    Toast.makeText(this,"No url found",Toast.LENGTH_LONG).show();
}
    }

    class BrowserWebClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast.makeText(Browser.this,url,Toast.LENGTH_LONG).show();
            return super.shouldOverrideUrlLoading(view, url);

        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            setProgressBarIndeterminateVisibility(true);
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setProgressBarIndeterminateVisibility(false);
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(Browser.this,"Error "+errorCode+" : "+failingUrl+" \n Description :"+description,Toast.LENGTH_LONG).show();
        }
    }

    class BrowserClient extends WebChromeClient{


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            mLoadingProgressBar.setProgress(newProgress);
        }
    }


}
