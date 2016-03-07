package com.dreamfactory.bluetooth.view.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.ble.SampleGattAttributes;
import com.dreamfactory.bluetooth.event.SelectCharacteristicEvent;
import com.dreamfactory.bluetooth.view.SettingActivity;
import com.dreamfactory.bluetooth.view.adapter.base.ViewHolder;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Author：kurtishu on 3/2/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class ServicesExpandableListAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    List<BluetoothGattService> mServices;
    Context mContext;

    public ServicesExpandableListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setDatas(List<BluetoothGattService> services) {
        mServices = services;
    }

    @Override
    public int getGroupCount() {
        return mServices.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mServices.get(groupPosition).getCharacteristics().size();
    }

    @Override
    public BluetoothGattService getGroup(int groupPosition) {
        return mServices.get(groupPosition);
    }

    @Override
    public BluetoothGattCharacteristic getChild(int groupPosition, int childPosition) {
        return mServices.get(groupPosition).getCharacteristics().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final BluetoothGattService service = getGroup(groupPosition);
        ViewHolder viewHolder = ViewHolder.get(convertView, parent, groupPosition, R.layout.layout_item_group, inflater);
        viewHolder.setText(R.id.section_header_text, SampleGattAttributes.lookup(service.getUuid().toString(), "unknow service"));
        viewHolder.setText(R.id.sub_textview, service.getUuid().toString());
        return viewHolder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(convertView, parent, childPosition, R.layout.layout_item_child, inflater);
        final BluetoothGattCharacteristic characteristic = getChild(groupPosition, childPosition);
        viewHolder.setText(R.id.title_textview, String.valueOf(characteristic.getPermissions()));
        viewHolder.setText(R.id.sub_textview, characteristic.getUuid().toString());

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SelectCharacteristicEvent(characteristic));
                Intent intent = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intent);
            }
        });
        return viewHolder.getConvertView();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
