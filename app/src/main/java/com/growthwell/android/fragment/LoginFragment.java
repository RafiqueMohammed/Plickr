package com.growthwell.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.growthwell.android.util.InternetConnection;
import com.growthwell.android.util.L;
import com.growthwell.android.plickr.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginFragment extends Fragment implements View.OnClickListener {

    Button register, login, verify_btn;
    LinearLayout step1, step2, circle;
    ProgressBar loading;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        login = (Button) v.findViewById(R.id.btn_login);
        register = (Button) v.findViewById(R.id.btn_register_account);
        verify_btn = (Button) v.findViewById(R.id.btn_email_verify);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        verify_btn.setOnClickListener(this);
        step1 = (LinearLayout) v.findViewById(R.id.login_step_1_container);
        step2 = (LinearLayout) v.findViewById(R.id.login_step_2_container);
        loading = (ProgressBar) v.findViewById(R.id.login_icon_loading);
        circle = (LinearLayout) v.findViewById(R.id.login_circle_layout);
        /** SET CIRCLE WIDTH AND HEIGHT AUTOMATICALLY DEPENDS ON SCREEN SIZE **/

        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x * 18 / 20;
        int height = point.y * 18 / 20;
        int smallerDimension = width < height ? width : height;

        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) circle.getLayoutParams();
        p.width = smallerDimension;
        p.height = smallerDimension;
        circle.setLayoutParams(p);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                ;
                break;
            case R.id.btn_register_account:
                step1.setVisibility(View.GONE);
                step2.setVisibility(View.VISIBLE);

                break;
            case R.id.btn_email_verify:
                InternetConnection c = new InternetConnection();
                JSONObject result = new JSONObject();
                if (c.isConnectingToInternet(getActivity())) {

                    InternetConnection.connect conn = new InternetConnection.connect();
                    try {
                        loading.setVisibility(View.VISIBLE);
                        result = conn.execute(new String[]{"http://192.168.1.200/plickr"}).get();
                        if (result.getString("status").equals("ok")) {
                            loading.setVisibility(View.INVISIBLE);
                            L.c("status ok");
                        } else {
                            L.c("Status not ok");
                        }
                        L.t(getActivity(), "Successfully executed");
                    } catch (InterruptedException e) {
                        L.c("InterruptedException " + e.toString());
                        loading.setVisibility(View.INVISIBLE);
                    } catch (ExecutionException e) {
                        L.c("ExecutionException " + e.toString());
                        loading.setVisibility(View.INVISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    L.t(getActivity(), "Connection Failed");

                }
                break;
        }
    }
}
