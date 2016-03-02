package com.dreamfactory.bluetooth.view.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.view.adapter.base.ViewHolder;

import java.util.List;

/**
 * Author：kurtishu on 3/2/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class ServicesExpandableListAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    List<BluetoothGattService> services;

    public ServicesExpandableListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setDatas(List<BluetoothGattService> services) {
        services = services;
    }

    @Override
    public int getGroupCount() {
        return services.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return services.get(groupPosition).getCharacteristics().size();
    }

    @Override
    public BluetoothGattService getGroup(int groupPosition) {
        return services.get(groupPosition);
    }

    @Override
    public BluetoothGattCharacteristic getChild(int groupPosition, int childPosition) {
        return services.get(groupPosition).getCharacteristics().get(childPosition);
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
        ViewHolder viewHolder = ViewHolder.get(convertView, parent, 0, R.layout.layout_item_group, inflater);
        return viewHolder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(convertView, parent, 0, R.layout.layout_item_child, inflater);
        return viewHolder.getConvertView();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
