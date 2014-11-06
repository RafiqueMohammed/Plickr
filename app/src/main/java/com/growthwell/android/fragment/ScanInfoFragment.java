package com.growthwell.android.fragment;

import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.growthwell.android.viewitquick.MainActivity;
import com.growthwell.android.viewitquick.R;


/**
 * Created by Rafique on 11/10/2014.
 */
public class ScanInfoFragment extends Fragment {

    TextView scan_output;
    WebView wv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.scan_info,container,false);
        scan_output= (TextView) view.findViewById(R.id.qr_txt_result_output);
        wv= (WebView) view.findViewById(R.id.web_output);
        wv.setHorizontalScrollBarEnabled(false);
        Bundle data=getArguments();
        String out="";
        if(data.getString("SCAN_RESULT")!=null){
            out=data.getString("SCAN_RESULT");
        }else{
            out="Scan result is empty";
        }
        wv.loadData(out,"text/html","UTF-8");

        scan_output.setText(out);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
inflater.inflate(R.menu.main,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.copy_clipboard:

                               //place your TextView's text in clipboard
                final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                if(!scan_output.getText().equals("")){
                    clipboard.setText(scan_output.getText().toString());
                    Toast.makeText(getActivity(),"Copied Successful",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),"Blank cannot be copied",Toast.LENGTH_LONG).show();
                }

                return true;
            case R.id.scan_again:
                startActivity(new Intent(getActivity(),MainActivity.class));

                return true;

            default: return true;
        }

    }
}
