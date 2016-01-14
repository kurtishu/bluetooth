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

import java.util.ArrayList;
import java.util.List;

/**
 * Author：kurtishu on 1/11/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothLeService extends Service {

    public static final String ACTION_MESSAGE = "android.intent.action.MESSAGE";
    public static final String ACTION_COMMAND = "Action_Command";
    public static final String ACTION_DEVICES = "Action_DEVICE";
    public static final String ACTION_SCANDEVICE_START = "Action_ScanDevice_Start";
    public static final String ACTION_SCANDEVICE_STOP = "Action_ScanDevice_Stop";
    private static final String TAG = "BluetoothLeService";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mScanner;
    private BluetoothGatt mBluetoothGatt;
    private Handler mHandler = new Handler();
    private boolean mScanning;
    private BluetoothHelper bluetoothHelper;

    private BluetoothHelper.DeviceScanCallback scanCallback = new BluetoothHelper.DeviceScanCallback() {

        @Override
        public void onScan(List<BluetoothDevice> devices) {
            Intent intent = new Intent(ACTION_MESSAGE);
            intent.putParcelableArrayListExtra(ACTION_DEVICES, (ArrayList<? extends Parcelable>) devices);
            sendBroadcast(intent);
        }

        @Override
        public void onScanFinished() {

        }

        @Override
        public void onScanFailed() {

        }
    };

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "Connected to GATT server.");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> services = gatt.getServices();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
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
            mBluetoothAdapter.isEnabled();
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
            }

        }
        return START_STICKY;
    }

    private String readerValue(List<BluetoothGattService> gattServices) {
        for (BluetoothGattService service : gattServices) {
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            Log.i(TAG, "Service UUID: " + service.getUuid().toString());
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                Log.i(TAG, "Value:" + characteristic.getValue());
                Log.i(TAG, "Description:" + characteristic.getDescriptors().toString());
            }
        }

        return null;

    }

    private void write(byte[] data) {

    }

    private void connectGattServer(BluetoothDevice device) {
        // Connecting to a GATT Server
        device.connectGatt(this, false, bluetoothGattCallback);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
}
