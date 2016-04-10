package com.lwn314.timingbrightness;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwn31 on 2016/4/7.
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
