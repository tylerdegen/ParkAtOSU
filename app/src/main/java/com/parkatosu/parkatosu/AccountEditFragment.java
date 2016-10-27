package com.parkatosu.parkatosu;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by ajmcs on 10/25/2016.
 */
public class AccountEditFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = AccountEditFragment.class.getSimpleName();

    private EditText address;
    private EditText permits;
    private Button mSaveButton;
    private Spinner osuPermitSpinner;
    private DatabaseHelper dh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_account_edit, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        super.onViewCreated(v, savedInstanceState);

        address= (EditText) v.findViewById(R.id.address_edit_text);
        //permits= (EditText) v.findViewById(R.id.permits_edit_text);

        osuPermitSpinner = (Spinner) v.findViewById(R.id.osu_permit_spinner);

        ArrayAdapter<CharSequence> osuPermitsAdapter = ArrayAdapter
                .createFromResource(this.getActivity(), R.array.osu_permits_array,
                        android.R.layout.simple_spinner_item);

        osuPermitSpinner.setAdapter(osuPermitsAdapter);

        dh = new DatabaseHelper(this.getActivity());

        mSaveButton = (Button) v.findViewById(R.id.edit_account_save);
        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.edit_account_save:
                ContentValues values = new ContentValues();

                String addressText = address.getText().toString().trim();
                String permitsText = osuPermitSpinner.getSelectedItem().toString().trim();

                if (!addressText.isEmpty()){
                    values.put("address",addressText);
                }
                if (!permitsText.isEmpty()){
                    values.put("permits",permitsText);
                }

                SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(this.getActivity());
                String[] usernameArg = new String[1];
                usernameArg[0] = settings.getString("name","");

                dh.updateValue(values, "name = ?", usernameArg);
                Log.d(TAG, "Updated values");
                Toast.makeText(this.getActivity(),"Saved!",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
