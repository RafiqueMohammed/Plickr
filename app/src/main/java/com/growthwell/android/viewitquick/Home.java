package com.growthwell.android.viewitquick;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import com.growthwell.android.QRLayouts.PlainText;
import com.growthwell.android.fragment.AboutFragment;

import com.growthwell.android.fragment.DashboardFragment;
import com.growthwell.android.fragment.FeedbackFragment;
import com.growthwell.android.fragment.GeneratedQRFragment;
import com.growthwell.android.fragment.FragmentCreateFromOption;

import com.growthwell.android.fragment.LoginFragment;
import com.growthwell.android.fragment.ScanFragment;
import com.growthwell.android.util.Global;
import com.growthwell.android.util.L;
import com.growthwell.android.util.SidebarAdapter;

import java.util.ArrayList;


public class Home extends Activity implements AdapterView.OnItemClickListener {


    private CharSequence mTitle;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionbar_toggle;
    boolean isDrawerOpen=false;
    ListView slide_list;
    ArrayList<SidebarAdapter.SidebarStruct> ss_al;
    String[] list_array;

    FragmentManager fManager=getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
         slide_list= (ListView) findViewById(R.id.slide_list);
         list_array= new String[]{"Scan Now", "Home", "Create", "About Plickr", "Login"};

        ss_al=new ArrayList<SidebarAdapter.SidebarStruct>();
        ss_al.add(new SidebarAdapter.SidebarStruct(R.drawable.ic_scan));
        //ss_al.add(new SidebarAdapter.SidebarStruct(R.drawable.ic_home));
        ss_al.add(new SidebarAdapter.SidebarStruct(R.drawable.ic_create));
      //  ss_al.add(new SidebarAdapter.SidebarStruct(R.drawable.ic_login));
        ss_al.add(new SidebarAdapter.SidebarStruct(R.drawable.ic_edit));
        ss_al.add(new SidebarAdapter.SidebarStruct(R.drawable.ic_info));

        slide_list.setAdapter(new SidebarAdapter(this, R.layout.sidebar_layout_row, ss_al));

        slide_list.setOnItemClickListener(this);



        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        actionbar_toggle=new ActionBarDrawerToggle(this,drawer,R.drawable.ic_drawer,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.setDrawerListener(actionbar_toggle);

        if (savedInstanceState == null) {
            selectMenu(0);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionbar_toggle.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectMenu(i);

    }



    public void selectMenu(int i) {

       slide_list.setItemChecked(i,true);
        drawer.closeDrawers();
       setPageTitle(list_array[i]);

        Fragment frag=null;
        String tag="";
        L.c("Clicked on "+i);
        switch(i){
            case 0:
                frag=new ScanFragment();
                tag=Global.FRAGMENT_TAG_SCAN;
            break;
           case 1:
                frag=new FragmentCreateFromOption();
                tag=Global.FRAGMENT_TAG_CREATE;
break;
            case 2: frag=new FeedbackFragment();
                tag=Global.FRAGMENT_TAG_FEEDBACK;

                break;
            case 3: frag=new AboutFragment();
                tag=Global.FRAGMENT_TAG_ABOUT;
                break;
            default:
                frag=new AboutFragment();
                tag=Global.FRAGMENT_TAG_ABOUT;
                break;
             }
        if(frag!=null){

            fManager.beginTransaction().replace(R.id.frame_container, frag,tag).commit();
        }

    }
    public void setPageTitle(String name){
        getActionBar().setTitle(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionbar_toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(Gravity.START)){
finish();
        }else{
            drawer.openDrawer(Gravity.START);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionbar_toggle.onConfigurationChanged(newConfig);
    }



}
