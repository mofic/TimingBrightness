package com.lwn314.timingbrightness;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by lwn31 on 2016/4/8.
 */
public class BrightnessController extends Service {

    public void controller(int brightness) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
                    brightness);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private int getScreenMode() {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System
                    .SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenMode;
    }

    private void setScreenMode(int screenMode) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                    screenMode);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int screenMode = getScreenMode();
        if (screenMode == 1) {
            setScreenMode(0);
        }
        int id = intent.getIntExtra("id", 0);
        Item item = ItemDB.queryItem(id);
        if (item != null) {
            boolean isChecked = item.getIsChecked();
            int brightness = item.getBrightness();
            controller(brightness);
            if (!isChecked) {
                ItemDB.updateItem(id);
            }
            Intent i = new Intent(this, MyReceiver.class);
            sendBroadcast(i);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
