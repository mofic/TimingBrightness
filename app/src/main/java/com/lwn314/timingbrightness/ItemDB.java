package com.lwn314.timingbrightness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwn31 on 2016/4/6.
 */
public class ItemDB {
    private static final String DB_NAME = "item";
    private static final int version = 1;
    private static ItemDB itemDB;
    private static SQLiteDatabase db;

    private ItemDB(Context context) {
        ItemDBHelper itemDBHelper = new ItemDBHelper(context, DB_NAME, null, version);
        db = itemDBHelper.getWritableDatabase();
    }

    public synchronized static ItemDB getInstance(Context context) {
        if (itemDB == null) {
            itemDB = new ItemDB(context);
        }
        return itemDB;
    }

    public static void saveItem(Item item) {
        ContentValues values = new ContentValues();
        values.put("hour", item.getHour());
        values.put("minute", item.getMinute());
        values.put("brightness", item.getBrightness());
        if (item.getIsChecked()) {
            values.put("isChecked", 1);
        } else {
            values.put("isChecked", 0);
        }
        if (item.getIsSwitched()) {
            values.put("isSwitched", 1);
        } else {
            values.put("isSwitched", 0);
        }
        db.insert("Item", null, values);
    }

    public static List<Item> loadItem() {
        List<Item> list = new ArrayList<>();
        Cursor cursor = db.query("Item", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int hour = cursor.getInt(cursor.getColumnIndex("hour"));
                int minute = cursor.getInt(cursor.getColumnIndex("minute"));
                int brightness = cursor.getInt(cursor.getColumnIndex("brightness"));
                boolean isChecked;
                isChecked = cursor.getInt(cursor.getColumnIndex("isChecked")) == 1;
                boolean isSwitched;
                isSwitched = cursor.getInt(cursor.getColumnIndex("isSwitched")) == 1;
                Item item = new Item();
                item.setHour(hour);
                item.setMinute(minute);
                item.setChecked(isChecked);
                item.setBrightness(brightness);
                item.setSwitched(isSwitched);
                item.setId(id);
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public static Item queryItem(int id) {
        Item item = new Item();
        Cursor cursor = db.query("Item", null, "id = ?", new String[]{String.valueOf(id)}, null,
                null, null);
        if (cursor.moveToFirst()) {
            int hour = cursor.getInt(cursor.getColumnIndex("hour"));
            int minute = cursor.getInt(cursor.getColumnIndex("minute"));
            int brightness = cursor.getInt(cursor.getColumnIndex("brightness"));
            boolean isChecked;
            isChecked = cursor.getInt(cursor.getColumnIndex("isChecked")) == 1;
            boolean isSwitched;
            isSwitched = cursor.getInt(cursor.getColumnIndex("isSwitched")) == 1;
            item.setHour(hour);
            item.setMinute(minute);
            item.setChecked(isChecked);
            item.setBrightness(brightness);
            item.setSwitched(isSwitched);
        }
        cursor.close();
        return item;
    }

    public static void updateItem(boolean isSwitched, int id) {
        ContentValues values = new ContentValues();
        if (isSwitched) {
            values.put("isSwitched", 1);
        } else {
            values.put("isSwitched", 0);
        }
        db.update("Item", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public static void updateItem(int brightness, int id) {
        ContentValues values = new ContentValues();
        values.put("brightness", brightness);
        db.update("Item", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public static void updateItem(Item item, int id) {
        ContentValues values = new ContentValues();
        values.put("hour", item.getHour());
        values.put("minute", item.getMinute());
        values.put("brightness", item.getBrightness());
        if (item.getIsChecked()) {
            values.put("isChecked", 1);
        } else {
            values.put("isChecked", 0);
        }
        if (item.getIsSwitched()) {
            values.put("isSwitched", 1);
        } else {
            values.put("isSwitched", 0);
        }
        db.update("Item", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public static void updateItem(int id) {
        ContentValues values = new ContentValues();
        values.put("isSwitched", 0);
        db.update("Item", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public static void deleteItem(List<Integer> list) {
        for (int id : list) {
            db.delete("Item", "id = ?", new String[]{String.valueOf(id)});
        }
    }
}
