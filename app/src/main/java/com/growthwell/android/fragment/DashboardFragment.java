package com.growthwell.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.growthwell.android.viewitquick.R;

public class DashboardFragment extends Fragment  {

    public DashboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
View v=inflater.inflate(R.layout.fragment_dashboard,container,false);

        return v;

    }
}
