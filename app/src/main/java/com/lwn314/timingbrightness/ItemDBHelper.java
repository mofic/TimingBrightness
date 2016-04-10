package com.lwn314.timingbrightness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lwn31 on 2016/4/6.
 */
public class ItemDBHelper extends SQLiteOpenHelper {

    public static final String CREATE_ITEM = "create table Item ("
            + "id integer primary key autoincrement,"
            + "hour integer,"
            + "minute integer,"
            + "brightness integer,"
            + "isSwitched integer ,"
            + "isChecked integer )";

    public ItemDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int
            version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
