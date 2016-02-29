package com.dreamfactory.bluetooth.view.base;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.service.BluetoothLeService;
import com.dreamfactory.bluetooth.wedigt.SystemBarTintManager;

import java.util.List;

/**
 * Author：kurtishu on 1/11/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(rootView());
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);

        IntentFilter intentFilter = new IntentFilter(BluetoothLeService.ACTION_MESSAGE);
        registerReceiver(mReceiver, intentFilter);
    }

    protected abstract int rootView();

    protected void onReceived(Context context, Intent intent) {}

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onReceived(context, intent);
        }
    };

    protected void sendCommand(String command) {
        Intent intent = new Intent(BaseActivity.this, BluetoothLeService.class);
        intent.putExtra(BluetoothLeService.ACTION_COMMAND, command);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
