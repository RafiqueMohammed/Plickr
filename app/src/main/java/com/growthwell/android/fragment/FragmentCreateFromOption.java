package com.growthwell.android.fragment;



import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.growthwell.android.viewitquick.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FragmentCreateFromOption extends Fragment implements AdapterView.OnItemClickListener {

    ListView list;
    int[] list_icon = {R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
    String[] list_titles = {"bussiness card","contact sharing","sms","web address","plain text","zippr"};
    public FragmentCreateFromOption() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_create_from_option, container, false);

        list = (ListView) v.findViewById(R.id.listview);
        CustomAdaptor adaptor = new CustomAdaptor(getActivity().getApplicationContext(),list_titles,list_icon);
        list.setAdapter(adaptor);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String item = ((TextView)view).getText().toString();

        Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
    }
}

class CustomAdaptor extends BaseAdapter
{
    Context context;
    int[] images;
    String[] titles;
    CustomAdaptor(Context c,String[] list_titles,int imgs[])
    {
        this.context=c;
        this.images=imgs;
        this.titles=list_titles;
    }

    @Override
    public int getCount() {
        return this.titles.length;
    }

    @Override
    public Object getItem(int i) {
        return titles[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       if(convertView==null){
           convertView = inflater.inflate(R.layout.qr_option_list_row,parent,false);
       }

        TextView mytitle = (TextView) convertView.findViewById(R.id.menu_title);
        ImageView myicon = (ImageView) convertView.findViewById(R.id.menu_icon);

        myicon.setImageResource(images[position]);
        mytitle.setText(titles[position]);

        return convertView;
    }
}
