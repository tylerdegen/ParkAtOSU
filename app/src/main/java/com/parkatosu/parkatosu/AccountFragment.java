package com.parkatosu.parkatosu;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ajmcs on 10/24/2016.
 */
public class AccountFragment extends Fragment {

    private TextView address;
    private TextView permits;
    private TextView mySpot;
    private DatabaseHelper dh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //your code which you want to refresh
        populateAccountInformation(this.getView());
    }

    @Override
    public void onResume(){
        super.onResume();
        populateAccountInformation(this.getView());

    }

    public void populateAccountInformation(View v){
        address = (TextView) v.findViewById(R.id.address_text_view);
        permits = (TextView) v.findViewById(R.id.permits_text_view);
        mySpot = (TextView) v.findViewById(R.id.my_spot_text_view);

        dh = new DatabaseHelper(this.getActivity());

        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        List<String> propsList = dh.selectProps(settings.getString("name",""),"ACCOUNTS");
        if (propsList != null) {
            if (propsList.get(0) != null) {
                address.setText(propsList.get(0));
            } else {
                address.setText(R.string.no_address);
            }

            if (propsList.get(1) != null) {
                permits.setText(propsList.get(1));
            } else {
                permits.setText(R.string.no_osu_permit);
            }

            if (propsList.get(2) != null) {
                mySpot.setText(propsList.get(2) + ", " + propsList.get(3));
            } else {
                mySpot.setText(R.string.no_spot);
            }
        }
    }
}
