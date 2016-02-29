package com.dreamfactory.bluetooth.view;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.event.ConnectDeviceEvent;
import com.dreamfactory.bluetooth.event.GetServicesEvent;
import com.dreamfactory.bluetooth.service.BluetoothLeService;
import com.dreamfactory.bluetooth.view.adapter.CharacteristicAdapter;
import com.dreamfactory.bluetooth.view.adapter.ServicesAdapter;
import com.dreamfactory.bluetooth.view.base.BaseActivity;

import java.util.List;

import de.greenrobot.event.EventBus;

public class SelectServiceActivity extends BaseActivity {

    private TextView tvAddress;
    private ListView mListView;
    private BluetoothDevice mDevice;

    private ServicesAdapter servicesAdapter;
    private CharacteristicAdapter characteristicAdapter;

    private boolean isDisplayServices = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevice = getIntent().getParcelableExtra(BluetoothLeService.INTENT_DEVICE);
        if (null == mDevice) {
            finish();
        }

        initViews();

        EventBus.getDefault().register(this);

        EventBus.getDefault().post(new ConnectDeviceEvent(mDevice));
    }


    private void initViews() {
        tvAddress = (TextView) findViewById(R.id.device_address);
        mListView = (ListView) findViewById(R.id.lists);
    }

    @Override
    protected int rootView() {
        return R.layout.activity_select_service;
    }


    public void onEvent(GetServicesEvent event) {
        if (null != event && null != event.getServices()) {
            servicesAdapter = new ServicesAdapter(SelectServiceActivity.this, mHandler);
            servicesAdapter.setDatas(event.getServices());
            mListView.setAdapter(servicesAdapter);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           if (msg.what == 1) {
               List<BluetoothGattCharacteristic> characteristics = (List<BluetoothGattCharacteristic>) msg.obj;
               characteristicAdapter = new CharacteristicAdapter(SelectServiceActivity.this);
               characteristicAdapter.setDatas(characteristics);
               mListView.setAdapter(characteristicAdapter);
           }
        }
    };

    @Override
    public void onBackPressed() {
        if (isDisplayServices) {
            super.onBackPressed();
        } else if (null != characteristicAdapter) {
            mListView.setAdapter(characteristicAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
