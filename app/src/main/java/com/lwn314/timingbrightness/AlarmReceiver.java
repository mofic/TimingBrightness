package com.lwn314.timingbrightness;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lwn31 on 2016/4/10.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private Context context;
    private int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        id = intent.getIntExtra("alarmId", 0);
        if (intent.getBooleanExtra("cancel", false)) {
            cancelAlarm(intent);
        } else {
            startAlarm();
        }
    }

    private void cancelAlarm(Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }

    private void startAlarm() {
        Intent changeBrightnessIntent = new Intent(context, BrightnessController.class);
        changeBrightnessIntent.putExtra("id", id);
        context.startService(changeBrightnessIntent);
    }
}
