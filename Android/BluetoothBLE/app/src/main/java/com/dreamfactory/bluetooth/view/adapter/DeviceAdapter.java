package com.dreamfactory.bluetooth.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.service.BluetoothLeService;
import com.dreamfactory.bluetooth.view.SettingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：kurtishu on 1/14/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class DeviceAdapter extends BaseAdapter {

    private Context mContext;
    private List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
    private LayoutInflater mInflater;

    public DeviceAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<BluetoothDevice> mDevices) {
        this.mDevices = mDevices;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.layout_item_device, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDeviceName = (TextView)convertView.findViewById(R.id.device_name_textview);
            viewHolder.tvPaired = (TextView)convertView.findViewById(R.id.paired_textview);
            viewHolder.settingIcon = (AppCompatImageView)convertView.findViewById(R.id.icon_settings);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final BluetoothDevice device = mDevices.get(position);
        viewHolder.tvDeviceName.setText(device.getName());
        viewHolder.tvPaired.setText(device.getAddress());
        viewHolder.settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                intent.putExtra(BluetoothLeService.INTENT_DEVICE, device);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvDeviceName;
        TextView tvPaired;
        AppCompatImageView settingIcon;
    }
}
