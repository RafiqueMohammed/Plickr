package com.growthwell.android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.growthwell.android.util.Global;
import com.growthwell.android.plickr.R;

public class AboutFragment extends Fragment implements View.OnClickListener {

    Button feedback;
    TextView visit_plickr, visit_growthwell,version_txt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_about, container, false);
        feedback = (Button) root.findViewById(R.id.report_bug);
        visit_growthwell = (TextView) root.findViewById(R.id.visit_growthwell);
        visit_plickr = (TextView) root.findViewById(R.id.visit_plickr);
        version_txt= (TextView) root.findViewById(R.id.version_txt);
        try {
            version_txt.setText("VERSION "+ getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        };
        visit_plickr.setOnClickListener(this);
        visit_growthwell.setOnClickListener(this);
        feedback.setOnClickListener(this);
        return root;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_bug:
                Global.swapFragment(getActivity(), new FeedbackFragment(), Global.FRAGMENT_TAG_FEEDBACK);
                break;
            case R.id.visit_growthwell:

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://growthwell.com/?ref=plickr_android_app"));
                startActivity(Intent.createChooser(i, "Open with.."));
                break;
            case R.id.visit_plickr:
                Intent iv = new Intent(Intent.ACTION_VIEW);
                iv.setData(Uri.parse("http://plickr.in/?ref=plickr_android_app"));
                startActivity(Intent.createChooser(iv, "Open with.."));
                break;

        }
    }
}
