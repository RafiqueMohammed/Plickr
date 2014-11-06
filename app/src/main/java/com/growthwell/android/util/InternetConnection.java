package com.growthwell.android.util;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

//import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InternetConnection {

    public static boolean isConnectingToInternet(Context c){
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
        public final int POST_TYPE=2;
        public final int GET_TYPE=1;
        private int connection_type=1;
        List<NameValuePair> post_data;
        ConnectionProcessListener callback;

        public connect(){

            result=new JSONObject();
            post_data=new ArrayList<NameValuePair>();

        }

        public void setCallbackListener(Fragment c){
            callback= (ConnectionProcessListener) c;
        }

        public void setType(int type){
            this.connection_type=type;
        }

        public void setPostData(ArrayList<NameValuePair> data){
            L.c("setPostData Added");
            post_data=data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.started();
        }

        @Override
        protected void onProgressUpdate(String... val) {
            super.onProgressUpdate(val);
            callback.onProgressUpdated(val[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            callback.finished();
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            url=urls[0];
            int statusCode;
            HttpClient conn=new DefaultHttpClient();


            if(urls.length!=0){
                try {
                    HttpResponse response;
                    if(this.connection_type==POST_TYPE){
                        HttpPost request=new HttpPost(url);
                        if(post_data!=null){
                            request.setEntity(new UrlEncodedFormEntity(post_data));
                        }
                        L.c("REQUESTING POST TYPE");
                        response =conn.execute(request);
                    }else{
                        L.c("REQUESTING GET TYPE");
                        HttpGet request=new HttpGet(url);
                        response =conn.execute(request);
                    }


                    statusCode=response.getStatusLine().getStatusCode();
                    long total_size=response.getEntity().getContentLength();

                    String output="";
                    InputStream is=response.getEntity().getContent();
                    BufferedReader bf=new BufferedReader(new InputStreamReader(is));


                    String word="";
                    long len_count = 0;
                    while((word=bf.readLine())!=null){

                        len_count+=word.length();
                        publishProgress(""+(int)((len_count*100)/total_size));

                        output+=word;
                    }
                    L.c(output);
                    //JSONObject json_result=new JSONObject(output);
                    result=new JSONObject(output);
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

    public interface ConnectionProcessListener{
        public void started();
        public void finished();
        public void onProgressUpdated(String i);
    }

}
