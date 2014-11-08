package com.growthwell.android.QRLayouts;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.plickr.Home;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.InternetConnection;
import com.growthwell.android.util.L;
import com.growthwell.android.plickr.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebAddress extends Fragment implements InternetConnection.ConnectionProcessListener {


    Button btn, short_url_btn;
    ImageView img, remove_su_container;
    EditText url;
    TextView short_url_txt;
    LinearLayout shorturl_container;
    ProgressDialog p;

    public WebAddress() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.qr_layouts_web_address, container, false);
        btn = (Button) v.findViewById(R.id.btn_create_qr);
        img = (ImageView) v.findViewById(R.id.img_qr_view);
        url = (EditText) v.findViewById(R.id.url_txt);
        Home parent= (Home) getActivity();
        parent.setPageTitle("Create QR for WEB URL");
        short_url_btn = (Button) v.findViewById(R.id.generate_short_url);
        shorturl_container = (LinearLayout) v.findViewById(R.id.shorturl_container);
        short_url_txt = (TextView) v.findViewById(R.id.short_url_txt);
        remove_su_container = (ImageView) v.findViewById(R.id.remove_short_url_container);
        short_url_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnection.isConnectingToInternet(getActivity())) {
                    InternetConnection.connect conn = new InternetConnection.connect();
                    conn.setType(conn.POST_TYPE);
                    conn.setCallbackListener(WebAddress.this);

                    try {

                        if (url.getText().toString().equals("")) {
                            Global.alert(getActivity(), "URL Should not be empty", true);
                        } else {
                            Pattern p = Pattern.compile(Global.URL_REGEX);
                            Matcher url_match = p.matcher(url.getText().toString());
                            if (url_match.find()) {

                                ArrayList<NameValuePair> pdata = new ArrayList<NameValuePair>();
                                pdata.add(new BasicNameValuePair("url", url.getText().toString()));
                                conn.setPostData(pdata);

                                JSONObject response = conn.execute(new String[]{Global.LINK_URL_SHORTNER}).get();
                                if (response.getString("status").equals("ok")) {
                                    shorturl_container.setVisibility(View.VISIBLE);
                                    short_url_txt.setText(response.getString("short_url"));
                                } else {
                                    short_url_txt.setText("");
                                }
                            } else {
                                Global.alert(getActivity(), "Please Enter a valid URL", true);
                            }

                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        L.c("InterruptedException received  ");
                        L.t(getActivity(), "Connection Interrupted");
                    } catch (ExecutionException e) {
                        L.c("ExecutionException received  ");

                        e.printStackTrace();
                    } catch (JSONException e) {
                        L.c("JSONException received ");
                        L.t(getActivity(), "Problem occurred while requesting. Please try again");
                        e.printStackTrace();
                    }
                } else {
                    L.c("Not Connected to Internet");
                }
            }
        });

        remove_su_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shorturl_container.setVisibility(View.GONE);
                short_url_txt.setText("");
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                if (url.getText().toString().equals("")) {
                    Global.alert(getActivity(), "Required Field Missing", true);
                } else {
                    if (shorturl_container.getVisibility() == View.GONE) {
                        //big url qr

                        b.putInt(Global.QR_TYPE_ID, Global.QR_CREATE_TYPE.PLAIN);
                        b.putString(Global.QR_RAW_DATA, url.getText().toString());

                    } else {
                        //short url qr
                        if (short_url_txt.getText().equals("")) {
                            Global.alert(getActivity(), "Short URL is blank", true);
                            shorturl_container.setVisibility(View.GONE);
                        } else {

                            b.putInt(Global.QR_TYPE_ID, Global.QR_CREATE_TYPE.PLAIN);
                            b.putString(Global.QR_RAW_DATA, short_url_txt.getText().toString());

                        }
                    }

                    Global.swapFragment(getActivity(), new GeneratedQRFragment(), b, Global.FRAGMENT_TAG_CREATE_QR_URL);
/*
                    Bitmap b= GeneratedQRFragment.GenerateQRBitmap(getActivity(),url.getText().toString());
                    img.setImageBitmap(b);*/
                }

            }
        });
        return v;
    }

    @Override
    public void started() {
        p = new ProgressDialog(getActivity());
        p.setIndeterminate(false);
        p.setMessage("Please wait..");
        p.setTitle("Shortening URL");
        p.setCancelable(false);
        p.show();

    }

    @Override
    public void finished() {
        if (p != null) {
            p.dismiss();
        }
    }

    @Override
    public void onProgressUpdated(String i) {

    }
}
