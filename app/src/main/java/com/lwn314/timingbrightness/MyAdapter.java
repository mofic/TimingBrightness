package com.lwn314.timingbrightness;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lwn31 on 2016/4/6.
 */
public class MyAdapter extends ArrayAdapter<Item> {

    private int resourceId;
    private Context context;

    public MyAdapter(Context context, int itemLayoutResourceId, List<Item> list) {
        super(context, itemLayoutResourceId, list);
        this.context = context;
        resourceId = itemLayoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Item item = getItem(position);
        View view;
        final ViewHolder viewHolder;
        int hour;
        int minute;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.timeText = (TextView) view.findViewById(R.id.time_text);
            viewHolder.repeatText = (TextView) view.findViewById(R.id.repeat_text);
            viewHolder.seekBarItem = (SeekBar) view.findViewById(R.id.seekBar_item);
            viewHolder.switchButton = (Switch) view.findViewById(R.id.switch_button);
            viewHolder.select = (CheckBox) view.findViewById(R.id.select);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        hour = item.getHour();
        minute = item.getMinute();
        /**
         * 四种情况判断是否补0
         */
        if (hour < 10 && minute < 10) {
            viewHolder.timeText.setText("0" + hour + ":" + "0" + minute);
        }
        if (hour < 10 && minute > 10) {
            viewHolder.timeText.setText("0" + hour + ":" + minute);
        }
        if (hour > 10 && minute < 10) {
            viewHolder.timeText.setText(hour + ":" + "0" + minute);
        }
        if (hour > 10 && minute > 10) {
            viewHolder.timeText.setText(hour + ":" + minute);
        }
        /**
         * 判断是否重复
         */
        if (item.getIsChecked()) {
            viewHolder.repeatText.setText("重复");
        } else {
            viewHolder.repeatText.setText("仅一次");
        }

        /**
         * 初始化开关为开
         */
        if (item.getIsSwitched()) {
            viewHolder.switchButton.setChecked(true);
        } else {
            viewHolder.switchButton.setChecked(false);
        }

        viewHolder.switchButton.setOnCheckedChangeListener(new CompoundButton
                .OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = item.getId();
                ItemDB.updateItem(isChecked, id);
                Intent i = new Intent(context, AlarmClock.class);
                i.putExtra("flag", MainActivity.UPDATE);
                i.putExtra("item", item);
                context.startService(i);
            }
        });

        /**
         * 设定亮度的设定值
         */
        viewHolder.seekBarItem.setMax(255);
        viewHolder.seekBarItem.setProgress(item.getBrightness());
        viewHolder.seekBarItem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int id = item.getId();
                ItemDB.updateItem(progress, id);
                Intent i = new Intent(context, AlarmClock.class);
                i.putExtra("flag", MainActivity.UPDATE);
                i.putExtra("item", item);
                context.startService(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /**
         * 长按多选
         */
        if (MainActivity.isLongClicked) {
            MainActivity.deleteList.clear();
            viewHolder.select.setVisibility(View.VISIBLE);
            viewHolder.switchButton.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer id = item.getId();
                    if (viewHolder.select.isChecked()) {
                        viewHolder.select.setChecked(false);
                        MainActivity.deleteList.remove(id);
                    } else {
                        viewHolder.select.setChecked(true);
                        MainActivity.deleteList.add(id);
                    }
                }
            });
        } else {
            viewHolder.select.setVisibility(View.GONE);
            viewHolder.switchButton.setVisibility(View.VISIBLE);
        }

        if (item != null) {
            Intent i = new Intent(context, AlarmClock.class);
            i.putExtra("item", item);
            context.startService(i);
        }

        return view;
    }

    private class ViewHolder {
        TextView timeText;
        TextView repeatText;
        SeekBar seekBarItem;
        Switch switchButton;
        CheckBox select;
    }

}
