package com.dreamfactory.bluetooth.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.service.BluetoothLeService;
import com.dreamfactory.bluetooth.view.SelectServiceActivity;
import com.dreamfactory.bluetooth.view.adapter.base.CommonAdapter;
import com.dreamfactory.bluetooth.view.adapter.base.ViewHolder;

/**
 * 作者：Kurtis on 2016/2/23 22:16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class DeviceAdapter extends CommonAdapter<BluetoothDevice> {

    public DeviceAdapter(Context context) {
        super(context, R.layout.layout_item_device);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position) {
        final BluetoothDevice device = getItem(position);
        viewHolder.setText(R.id.title_textview, device.getName());
        viewHolder.setText(R.id.sub_textview, device.getAddress());
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectServiceActivity.class);
                intent.putExtra(BluetoothLeService.INTENT_DEVICE, device);
                getContext().startActivity(intent);
            }
        });
    }
}
