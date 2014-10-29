package com.growthwell.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

//import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InternetConnection {

    public boolean isConnectingToInternet(Context c){
        ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for(int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
    public static class connect extends AsyncTask<String,String,JSONObject>{
        String url;
        JSONObject result;
        public connect(){
            result=new JSONObject();
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            url=urls[0];
            int statusCode;
            HttpClient conn=new DefaultHttpClient();
            L.c("URL "+url);
            HttpPost post=new HttpPost(url);
            if(urls.length!=0){
                try {
                    HttpResponse response=conn.execute(post);
                    statusCode=response.getStatusLine().getStatusCode();
                    String output="";
                    InputStream is=response.getEntity().getContent();
                    BufferedReader bf=new BufferedReader(new InputStreamReader(is));
                    String word="";
                    while((word=bf.readLine())!=null){
                        output+=word;
                    }
                    L.c(output);
                    JSONObject json_result=new JSONObject(output);
                    result.put("status",json_result.getString("status"));
                    result.put("status_code",statusCode);
                    result.put("result","URL Successful");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{

                try {

                    result.put("status","no");
                    result.put("result","Url is empty.Please specify URL to connect");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return result;
        }
    }

}
