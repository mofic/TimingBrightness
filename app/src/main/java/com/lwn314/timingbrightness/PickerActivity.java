package com.lwn314.timingbrightness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by lwn31 on 2016/4/5.
 */
public class PickerActivity extends Activity implements View.OnClickListener {

    private int hour;
    private int minute;
    private boolean isChecked;
    private int brightness;
    private int flag;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pick_layout);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        Button cancle = (Button) findViewById(R.id.cancle);
        Button define = (Button) findViewById(R.id.define);
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        /**
         * 设置seekBar的最大值为系统最大亮度255*/
        seekBar.setMax(255);

        flag = getIntent().getIntExtra("flag", MainActivity.DEFAULT);
        /**
         * 是否使用24小时制
         */
        timePicker.setIs24HourView(true);

        /**
         * 判断从哪个按钮跳转过来
         */
        if (flag == MainActivity.FROM_ITEM) {
            id = getIntent().getIntExtra("id", 0);
            Item item = ItemDB.queryItem(id);

            hour = item.getHour();
            minute = item.getMinute();
            brightness = item.getBrightness();
            isChecked = item.getIsChecked();

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
            seekBar.setProgress(brightness);
            checkBox.setChecked(isChecked);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checkBoxIsChecked) {
                isChecked = checkBoxIsChecked;
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfHour) {
                hour = hourOfDay;
                minute = minuteOfHour;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cancle.setOnClickListener(this);
        define.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                finish();
                break;
            case R.id.define:
                Item item = new Item();
                item.setHour(hour);
                item.setMinute(minute);
                item.setChecked(isChecked);
                item.setBrightness(brightness);
                item.setSwitched(true);
                if (flag == MainActivity.FROM_ITEM) {
                    ItemDB.updateItem(item, id);
                    Intent i = new Intent(this, AlarmClock.class);
                    i.putExtra("flag", MainActivity.UPDATE);
                    i.putExtra("item", item);
                    startService(i);
                } else {
                    ItemDB.saveItem(item);
                }
                Intent intent = new Intent(PickerActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim
                        .slide_out_right);
                ActivityCollector.finishAll();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
