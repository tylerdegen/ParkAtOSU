package com.parkatosu.parkatosu;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ajmcs on 11/2/2016.
 */
public class NotificationsDialogFragment extends DialogFragment{
    private ArrayList mSelectedItems;
    private DatabaseHelper dh;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList();  // Where we track the selected items
        dh = new DatabaseHelper(getActivity());

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.notifications_subscribe)
                .setMultiChoiceItems(R.array.notifications_array, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                }
                                else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));

                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        ContentValues values = new ContentValues();

                        if(mSelectedItems.contains(1)) {
                            values.put("ss_notify", "true");
                        }else{
                            values.put("ss_notify", "false");

                        }

                        if(mSelectedItems.contains(0)) {
                            values.put("gd_notify", "true");
                        }else{
                            values.put("gd_notify", "false");

                        }

                        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String[] usernameArg = new String[1];
                        usernameArg[0] = settings.getString("name","");

                        dh.updateValue(values, "name = ?", usernameArg);

                        Toast.makeText(getActivity(),"Saved Notification Preferences!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
