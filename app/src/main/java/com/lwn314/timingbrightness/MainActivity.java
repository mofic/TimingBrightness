package com.lwn314.timingbrightness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private MyAdapter adapter;
    private ListView timeList;
    private Button add;
    private Button delete;
    public static boolean isLongClicked = false;

    /**
     * 跳转标识
     */
    public static final int DEFAULT = 0;
    public static final int FROM_ADD = 1;
    public static final int FROM_ITEM = 2;
    public static final int UPDATE = 3;

    public static List<Integer> deleteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 初始化组件
         */
        add = (Button) findViewById(R.id.add);
        timeList = (ListView) findViewById(R.id.time_list);
        delete = (Button) findViewById(R.id.delete_button);
        deleteList = new ArrayList<>();
        ItemDB.getInstance(this);
        adapter = new MyAdapter(this, R.layout.item_layout, ItemDB.loadItem());
        timeList.setAdapter(adapter);

        /**
         * 点击add跳转设置界面
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PickerActivity.class);
                intent.putExtra("flag", FROM_ADD);
                startActivity(intent);
            }
        });

        /**
         * 长按显示选择界面
         */
        timeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                isLongClicked = true;
                timeList.setAdapter(adapter);
                add.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        /**
         * 点击timeList跳转设置界面并传入已设定的值
         */
        timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) timeList.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, PickerActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("flag", FROM_ITEM);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDB.deleteItem(deleteList);
                isLongClicked = false;
                delete.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                for (int id : deleteList) {
                    Intent cancelIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                    cancelIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    cancelIntent.putExtra("alarmId", id);
                    cancelIntent.putExtra("cancel", true);
                    sendBroadcast(cancelIntent);
                }
                refresh();
            }
        });

        ActivityCollector.addActivity(this);
    }

    public void refresh() {
        onCreate(null);
    }

    @Override
    public void onBackPressed() {
        if (isLongClicked) {
            isLongClicked = false;
            delete.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
            refresh();
        } else {
            finish();
        }
    }
}
