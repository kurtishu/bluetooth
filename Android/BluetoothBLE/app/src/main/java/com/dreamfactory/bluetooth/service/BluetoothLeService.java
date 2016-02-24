package com.dreamfactory.bluetooth.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import com.dreamfactory.bluetooth.util.BluetoothHelper;
import com.dreamfactory.bluetooth.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：kurtishu on 1/11/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothLeService extends Service {

    public static final String ACTION_MESSAGE = "android.intent.action.MESSAGE";
    public static final String ACTION_COMMAND = "Action_Command";
    public static final String INTENT_DEVICE_LIST = "Intent_Device_List";
    public static final String INTENT_DEVICE = "Intent_Device_signle";
    public static final String ACTION_SCANDEVICE_START = "Action_ScanDevice_Start";
    public static final String ACTION_SCANDEVICE_STOP = "Action_ScanDevice_Stop";
    public static final String ACTION_CONNECTDEVICE = "Action_ConnecDevice";
    public static final String ACTION_WRITEDATA = "Action_WriteData";

    private static final String TAG = "BluetoothLeService";
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler = new Handler();
    private BluetoothHelper bluetoothHelper;

    private BluetoothHelper.DeviceScanCallback scanCallback = new BluetoothHelper.DeviceScanCallback() {

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
            } else if (ACTION_CONNECTDEVICE.equals(command)) {
                bluetoothHelper.connectDevice((BluetoothDevice) intent.getParcelableExtra(INTENT_DEVICE));
            } else if (ACTION_WRITEDATA.equals(command)) {
               //TODO Write data
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
