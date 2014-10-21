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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FragmentCreateFromOption extends Fragment implements AdapterView.OnItemClickListener {

    ListView list;
    ArrayList<OptionStructure> list_data;
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
        list_data=new ArrayList<OptionStructure>();
        list_data.add(new OptionStructure(0,list_titles[0],list_icon[0]));
        list_data.add(new OptionStructure(1,list_titles[1],list_icon[0]));
        list_data.add(new OptionStructure(2,list_titles[2],list_icon[0]));
        list_data.add(new OptionStructure(3,list_titles[3],list_icon[0]));
        list_data.add(new OptionStructure(4,list_titles[4],list_icon[0]));
        list_data.add(new OptionStructure(5,list_titles[5],list_icon[0]));


        list = (ListView) v.findViewById(R.id.listview);
        CustomAdaptor adaptor = new CustomAdaptor(getActivity().getApplicationContext(),list_data);
        list.setAdapter(adaptor);
        list.setOnItemClickListener(this);


        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
OptionStructure opt= (OptionStructure) adapterView.getItemAtPosition(i);
        Toast.makeText(getActivity(),"You have click on id "+opt.getId(),Toast.LENGTH_LONG).show();
    }
}

class OptionStructure
{
    String titles;
    int img;

    public int getId() {
        return id;
    }

    int id;

    OptionStructure(int id,String titles, int img)
    {
        this.id=id;
        this.titles = titles;
        this.img = img;
    }

    public String getTitles() {
        return titles;
    }

    public int getImg() {
        return img;
    }
}

class CustomAdaptor extends BaseAdapter
{
    Context context;
    ArrayList<OptionStructure> mylist;

    CustomAdaptor(Context c,ArrayList<OptionStructure> l)
    {
        this.context=c;
        mylist=l;

    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int i) {
        return mylist.get(i);
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

        myicon.setImageResource(mylist.get(position).getImg());
        mytitle.setText(mylist.get(position).getTitles());

        return convertView;
    }
}
