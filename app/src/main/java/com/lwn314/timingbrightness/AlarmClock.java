package com.lwn314.timingbrightness;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by lwn31 on 2016/4/10.
 */
public class AlarmClock extends Service {
    private Item item;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int flag = intent.getIntExtra("flag", 0);
        Bundle bundle = intent.getExtras();
        item = (Item) bundle.getSerializable("item");
        Boolean isSwitched = null;
        if (item != null) {
            isSwitched = item.getIsSwitched();
        }
        if (flag == MainActivity.UPDATE) {
            turnAlarm(item, false);
        }
        turnAlarm(item, isSwitched);
        return super.onStartCommand(intent, flags, startId);
    }

    public void turnAlarm(Item item, Boolean isSwitched) {
        this.item = item;
        AlarmManager mAlamManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int id = item.getId();
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("alarmId", id);
        intent.putExtra("cancel", false);
        Log.d("alarm", "id" + id);
        PendingIntent pi = PendingIntent.getBroadcast(this, id, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);
        if (isSwitched) {
            startAlarm(mAlamManager, pi);
        } else {
            cancelAlarm(intent);
        }
    }

    private void cancelAlarm(Intent intent) {
        intent.putExtra("cancel", true);
        this.sendBroadcast(intent);
    }

    public void startAlarm(AlarmManager mAlamManager, PendingIntent pi) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, item.getHour());
        c.set(Calendar.MINUTE, item.getMinute());
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (c.getTimeInMillis() < System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= 19) {
                mAlamManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60
                        * 1000, pi);
            } else {
                mAlamManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60 *
                        1000, pi);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                mAlamManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            } else {
                mAlamManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
