package com.growthwell.android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.growthwell.android.viewitquick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafique on 17/10/2014.
 */
public class SidebarAdapter extends ArrayAdapter {

    ArrayList<SidebarAdapter.SidebarStruct> struct_list;
    Context c;
    TextView title;
    ImageView icon;


    public SidebarAdapter(Context context, int resource,ArrayList<SidebarAdapter.SidebarStruct> struct) {
        super(context, resource,struct);
        this.c=context;
        this.struct_list = struct;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            LayoutInflater inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.sidebar_layout_row,null);
        }

      //  title= (TextView) view.findViewById(R.id.menu_title);
      //  title.setText(struct_list.get(i).getTitle());
        icon= (ImageView) view.findViewById(R.id.menu_icon);
        icon.setImageResource(struct_list.get(i).getIcon());
        return view;
    }




    public static class SidebarStruct{
        String title;
        int icon;

        public SidebarStruct(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
        public SidebarStruct(int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public int getIcon() {
            return icon;
        }
    }
}
