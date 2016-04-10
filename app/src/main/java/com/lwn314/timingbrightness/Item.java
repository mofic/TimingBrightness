package com.lwn314.timingbrightness;

import java.io.Serializable;

/**
 * Created by lwn31 on 2016/4/6.
 */
public class Item implements Serializable {
    private int hour;
    private int minute;
    private boolean isChecked;
    private int brightness;
    private boolean isSwitched;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setSwitched(boolean isSwitched) {
        this.isSwitched = isSwitched;
    }

    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public int getBrightness() {
        return brightness;
    }

    public boolean getIsSwitched() {
        return isSwitched;
    }
}
