package com.dreamfactory.bluetooth.view;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.service.BluetoothLeService;
import com.dreamfactory.bluetooth.view.adapter.DeviceAdapter;
import com.dreamfactory.bluetooth.view.base.BaseActivity;
import com.dreamfactory.bluetooth.wedigt.RadarScanView;

import java.util.List;

public class MainActivity extends BaseActivity {

    private RadarScanView mScanView;
    private DeviceAdapter mDeviceAdapter;

    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            performClickScanView();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
    }

    @Override
    protected void onReceived(Context context, Intent intent) {
        if (BluetoothLeService.ACTION_MESSAGE.equals(intent.getAction())) {
            List<BluetoothDevice> devices = intent.getParcelableArrayListExtra(BluetoothLeService.INTENT_DEVICE_LIST);
            if (null != devices) {
                mDeviceAdapter.setData(devices);
            } else {
                Toast.makeText(MainActivity.this, "搜索结束，没有发现蓝牙设备", Toast.LENGTH_LONG).show();
            }
            mScanView.stop();
        }
    }

    private void initViews() {
        mScanView = (RadarScanView) findViewById(R.id.radar_view);
        mScanView.setOnClickListener(mOnclickListener);
        mDeviceAdapter = new DeviceAdapter(this);
    }

    @Override
    protected int rootView() {
        return R.layout.activity_main;
    }

    private void performClickScanView() {

        if (mScanView.isRunning()) {
            mScanView.stop();
            sendCommand(BluetoothLeService.ACTION_SCANDEVICE_STOP);
        } else {
            mScanView.start();
            sendCommand(BluetoothLeService.ACTION_SCANDEVICE_START);
        }
    }
}
