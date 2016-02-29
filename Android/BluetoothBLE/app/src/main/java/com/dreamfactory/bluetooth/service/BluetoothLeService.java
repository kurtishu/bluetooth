package com.dreamfactory.bluetooth.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.widget.Toast;

import com.dreamfactory.bluetooth.event.ConnectDeviceEvent;
import com.dreamfactory.bluetooth.event.GetServicesEvent;
import com.dreamfactory.bluetooth.event.SelectCharacteristicEvent;
import com.dreamfactory.bluetooth.event.WriteDataEvent;
import com.dreamfactory.bluetooth.util.BluetoothHelper;
import com.dreamfactory.bluetooth.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Author：kurtishu on 1/11/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothLeService extends Service {

    public static final String ACTION_MESSAGE = "android.intent.action.MESSAGE";
    public static final String ACTION_COMMAND = "ACTION_COMMAND";

    public static final String INTENT_DEVICE_LIST = "INTENT_DEVICE_LIST";
    public static final String INTENT_DEVICE = "INTENT_DEVICE_SIGNLE";

    public static final String ACTION_SCANDEVICE_START = "ACTION_SCANDEVICE_START";
    public static final String ACTION_SCANDEVICE_STOP = "ACTION_SCANDEVICE_STOP";

    public static final String ACTION_READ_DATA = "ACTION_READ_DATA";

    private static final String TAG = "BluetoothLeService";
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler = new Handler();
    private BluetoothHelper bluetoothHelper;
    private BluetoothGattCharacteristic characteristic;

    private BluetoothHelper.BluetoothCallback scanCallback = new BluetoothHelper.BluetoothCallback() {

        @Override
        public void onScan(List<BluetoothDevice> devices) {
            LogUtil.i(TAG, "====> onScan success" + devices.toString());
            Intent intent = new Intent(ACTION_MESSAGE);
            intent.putParcelableArrayListExtra(INTENT_DEVICE_LIST, (ArrayList<? extends Parcelable>) devices);
            sendBroadcast(intent);
        }

        @Override
        public void onScanFinished() {
            LogUtil.i(TAG, "====> onScanFinished");
            Intent intent = new Intent(ACTION_MESSAGE);
            sendBroadcast(intent);
        }

        @Override
        public void getServices(List<BluetoothGattService> services) {
           EventBus.getDefault().post(new GetServicesEvent(services));
        }

        @Override
        public void onScanFailed() {
            LogUtil.i(TAG, "====> onScanFailed");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            //Auto enable bluttooth.
            mBluetoothAdapter.enable();
        }
        bluetoothHelper = new BluetoothHelper(this, mHandler, mBluetoothAdapter);

        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null != intent) {
            String command = intent.getStringExtra(ACTION_COMMAND);
            if (ACTION_SCANDEVICE_START.equals(command)) {
                // scan device
                bluetoothHelper.startLeScan(scanCallback);
            } else if (ACTION_SCANDEVICE_STOP.equals(command)) {
                // Stop scan device
                bluetoothHelper.stopLeScan();
            } else if (ACTION_READ_DATA.equals(command)) {
                readeData();
            }
        }
        return START_STICKY;
    }

    private void readeData() {
        Intent intent = new Intent(ACTION_MESSAGE);
        sendBroadcast(intent);
    }

    private void writeData(int[] array) {
        // Convert
        byte[] bytes = new byte[]{1, 2, 3, 4};
        characteristic.setValue(bytes);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onEvent(ConnectDeviceEvent event) {
        if (null != event && null != event.getDevice()) {
            bluetoothHelper.connectDevice(event.getDevice());
        }
    }

    public void onEvent(SelectCharacteristicEvent event) {
        if (null != event && null != event.getCharacteristic()) {
            characteristic = event.getCharacteristic();
        }
    }

    public void onEvent(WriteDataEvent event) {
        if (null != event ) {
            writeData(event.getDatas());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
