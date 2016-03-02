package com.dreamfactory.bluetooth.view;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.event.ConnectDeviceEvent;
import com.dreamfactory.bluetooth.event.GetServicesEvent;
import com.dreamfactory.bluetooth.service.BluetoothLeService;
import com.dreamfactory.bluetooth.view.adapter.CharacteristicAdapter;
import com.dreamfactory.bluetooth.view.adapter.ServicesAdapter;
import com.dreamfactory.bluetooth.view.adapter.ServicesExpandableListAdapter;
import com.dreamfactory.bluetooth.view.base.BaseActivity;

import java.util.List;

import de.greenrobot.event.EventBus;

public class SelectServiceActivity extends BaseActivity {

    private TextView tvAddress;
    private ExpandableListView mListView;
    private BluetoothDevice mDevice;

    private ServicesExpandableListAdapter servicesAdapter;

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
        mListView = (ExpandableListView) findViewById(R.id.lists);
        tvAddress.setText(mDevice.getAddress());
    }

    @Override
    protected int rootView() {
        return R.layout.activity_select_service;
    }

    public void onEventMainThread(GetServicesEvent event) {
        if (null != event && null != event.getServices()) {
            servicesAdapter = new ServicesExpandableListAdapter(SelectServiceActivity.this);
            servicesAdapter.setDatas(event.getServices());
            mListView.setAdapter(servicesAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
