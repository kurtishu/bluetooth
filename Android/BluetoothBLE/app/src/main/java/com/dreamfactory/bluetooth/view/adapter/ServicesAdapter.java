package com.dreamfactory.bluetooth.view.adapter;

import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.view.adapter.base.CommonAdapter;
import com.dreamfactory.bluetooth.view.adapter.base.ViewHolder;

/**
 * Author：kurtishu on 2/29/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class ServicesAdapter extends CommonAdapter<BluetoothGattService> {

    private Handler mHandler;

    public ServicesAdapter(Context context, Handler handler) {
        super(context, R.layout.layout_item_device);
        mHandler = handler;
    }

    @Override
    public void convert(ViewHolder viewHolder, int position) {
        final BluetoothGattService service = getItem(position);
        viewHolder.setText(R.id.title_textview,
                service.getType() == BluetoothGattService.SERVICE_TYPE_PRIMARY
                        ? "SERVICE_TYPE_PRIMARY" : "SERVICE_TYPE_SECONDARY");
        viewHolder.setText(R.id.sub_textview, service.getUuid().toString());

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = service.getCharacteristics();
                mHandler.sendMessage(msg);
            }
        });
    }
}
