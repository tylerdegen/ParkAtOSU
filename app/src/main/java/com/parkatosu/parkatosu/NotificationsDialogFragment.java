package com.parkatosu.parkatosu;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ajmcs on 11/2/2016.
 */
public class NotificationsDialogFragment extends DialogFragment{
    private ArrayList mSelectedItems;
    private DatabaseHelper dh;

    private int REQUEST_CODE = 0;
    private Context context;
    private boolean streetSweeping = false;
    private boolean gameDays = false;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList();  // Where we track the selected items
        context = this.getContext();
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
                            streetSweeping = true;
                            System.out.println("streetSweeping = " + streetSweeping);
                        }else{
                            values.put("ss_notify", "false");
                            streetSweeping = false;

                        }

                        if(mSelectedItems.contains(0)) {
                            values.put("gd_notify", "true");
                            gameDays = true;
                            System.out.println("gameDays = " + gameDays);
                        }else{
                            values.put("gd_notify", "false");
                            gameDays = false;

                        }

                        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String[] usernameArg = new String[1];
                        usernameArg[0] = settings.getString("name","");

                        dh.updateValue(values, "name = ?", usernameArg);

                        Toast.makeText(getActivity(),"Saved Notification Preferences!",Toast.LENGTH_SHORT).show();

                        setAlarm();
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

    private void setAlarm(){
        dh = new DatabaseHelper(getActivity());

        Log.d("NotificationsDialog", "Setting Alarm for notification.");
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[] usernameArg = new String[1];
        usernameArg[0] = settings.getString("name","");

        String permit = dh.selectPermit(usernameArg[0], "ACCOUNTS");

        Intent intent = new Intent(this.getActivity(), NotificationReceiver.class);
        intent.putExtra("permit", permit);
        if (permit != null) {
            if (streetSweeping) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getActivity(), REQUEST_CODE, intent, 0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                //Street Sweeping Notification
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.DAY_OF_WEEK, 4);
        calendar.set(Calendar.WEEK_OF_MONTH, 2);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);*/
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 30);
                calendar.set(Calendar.SECOND, 00);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                System.out.println("Time Total 1----- " + (System.currentTimeMillis()));
            }

            REQUEST_CODE = 1;
            Intent intent2 = new Intent(this.getActivity(), GameDayReceiver.class);
            intent2.putExtra("permit", permit);

            if (gameDays) {
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this.getActivity(), REQUEST_CODE, intent2, 0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                //Game Days Notification
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);*/
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.HOUR_OF_DAY, 12);
                calendar2.set(Calendar.MINUTE, 30);
                calendar2.set(Calendar.SECOND, 00);
                //calendar2.set(Calendar.DAY_OF_WEEK, 7);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
                System.out.println("Time Total 2 ----- " + (System.currentTimeMillis()));
            }
        }


    }
}
