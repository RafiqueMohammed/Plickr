package com.growthwell.android.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.growthwell.android.util.Global;
import com.growthwell.android.util.InternetConnection;
import com.growthwell.android.util.L;
import com.growthwell.android.viewitquick.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FeedbackFragment extends Fragment implements InternetConnection.ConnectionProcessListener,View.OnClickListener {

    Button btn;

    EditText in_fname,in_email,in_msg;

    LinearLayout click_feedback_cont,click_bug_cont;


    public FeedbackFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_feedback, container, false);
        in_fname= (EditText) v.findViewById(R.id.in_fname);
        in_email= (EditText) v.findViewById(R.id.in_email);
        in_msg= (EditText) v.findViewById(R.id.in_msg);

        btn= (Button) v.findViewById(R.id.btn_send_feedback);
        click_bug_cont= (LinearLayout) v.findViewById(R.id.click_bug_container);
        click_feedback_cont= (LinearLayout) v.findViewById(R.id.click_feedback_container);
        click_bug_cont.setOnClickListener(this);
        click_feedback_cont.setOnClickListener(this);
        btn.setOnClickListener(this);
        return v;
    }

     /** ******** STARTS ConnectionProcessListener **************/
    ProgressDialog pd;
    @Override
    public void started() {
        L.c("ConnectionProcessListener STARTED");
        pd=new ProgressDialog(getActivity());
        pd.setTitle("Sending..");
        pd.setMessage("Please wait..");

        pd.setIndeterminate(true);
        pd.show();
    }

    @Override
    public void finished() {
        if(pd!=null){
            pd.dismiss();
        }

        L.c("ConnectionProcessListener finished");
    }

    @Override
    public void onProgressUpdated(String i) {
        L.c("PROGRESS "+i);
        if(pd!=null){
            pd.setProgress(Integer.parseInt(i));
        }
    }


    /** ******** ENDS ConnectionProcessListener **************/

    @Override
    public void onClick(View view) {
switch(view.getId()){
    case R.id.click_feedback_container:
        LinearLayout s1= (LinearLayout) getActivity().findViewById(R.id.screen_1);
        ScrollView s2= (ScrollView) getActivity().findViewById(R.id.screen_2);
        s1.setVisibility(View.GONE);
        s2.setVisibility(View.VISIBLE);
        break;
    case R.id.click_bug_container:
        Intent i =new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_SUBJECT,"Reporting a bug in Plickr");
        i.putExtra(Intent.EXTRA_EMAIL,"rafique@growthwell.com");
        i.putExtra(Intent.EXTRA_TEXT,"Hello,");
        i.putExtra(Intent.EXTRA_MIME_TYPES,"message/rfc822");
        i.setType("text/html");
        startActivity(Intent.createChooser(i,"Report bug using.."));
        break;
    case R.id.btn_send_feedback:

                if(in_fname.getText().toString().equals("")||in_email.getText().toString().equals("")||in_msg.getText().toString().equals("")){
                    Global.alert(getActivity(),"Required","All fields are required!",true);
                }else{
                    if(InternetConnection.isConnectingToInternet(getActivity())){

                        InternetConnection.connect conn=new InternetConnection.connect();
                        conn.setCallbackListener(FeedbackFragment.this);
                        conn.setType(conn.POST_TYPE);

                        ArrayList<NameValuePair> data_post=new ArrayList<NameValuePair>();
                        data_post.add(new BasicNameValuePair("fullname",in_fname.getText().toString()));
                        data_post.add(new BasicNameValuePair("email",in_email.getText().toString()));
                        data_post.add(new BasicNameValuePair("message",in_msg.getText().toString()));


                        conn.setPostData(data_post);



                        try {
                            JSONObject res=conn.execute(Global.LINK_FEEDBACK).get();
                            if(res.has("status")){
                                if(res.getString("status").equals("ok")){
                                    Global.alert(getActivity(),"Sent","Thank you for your feedback.",false);
                                }else if(res.getString("status").equals("no")){
                                    Global.alert(getActivity(),"Sorry",res.getString("result"),true);
                                }
                            }else{
                                Global.alert(getActivity(),"Sorry","No Response from the server",true);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Global.alert(getActivity(),"No Internet Connected","Kindly connect to internet to proceed",true);
                    }
                }

        break;
}
    }
}
