package com.dreamfactory.bluetooth.view.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.event.SelectCharacteristicEvent;
import com.dreamfactory.bluetooth.view.SettingActivity;
import com.dreamfactory.bluetooth.view.adapter.base.CommonAdapter;
import com.dreamfactory.bluetooth.view.adapter.base.ViewHolder;

import de.greenrobot.event.EventBus;

/**
 * Author：kurtishu on 2/29/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class CharacteristicAdapter extends CommonAdapter<BluetoothGattCharacteristic> {

    public CharacteristicAdapter(Context context) {
        super(context, R.layout.layout_item_device);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position) {
        final BluetoothGattCharacteristic characteristic = getItem(position);
        viewHolder.setText(R.id.title_textview, String.valueOf(characteristic.getPermissions()));
        viewHolder.setText(R.id.sub_textview, characteristic.getUuid().toString());

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SelectCharacteristicEvent(characteristic));
                Intent intent = new Intent(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
}
