package com.dreamfactory.bluetooth.service;

import android.annotation.TargetApi;
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
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：kurtishu on 1/11/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothLeService extends Service {


    private static final String TAG = "BluetoothLeService";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mScanner;
    private BluetoothGatt mBluetoothGatt;
    private Handler mHandler = new Handler();
    private boolean mScanning;

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
    }


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void scanLeDevice() {
//        if (null == mScanner) {
//            mScanner = mBluetoothAdapter.getBluetoothLeScanner();
//        }
//
//        // Stops scanning after a pre-defined scan period.
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScanner.stopScan(mScanCallback);
//            }
//        }, 10000);
//
//        mScanner.startScan(mScanCallback);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void stopScanLeDevice() {
//        if (null != mScanner) {
//            mScanner.stopScan(mScanCallback);
//        }
//    }


    private String readerValue(List<BluetoothGattService> gattServices) {
        for(BluetoothGattService service : gattServices) {
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            Log.i(TAG , "Service UUID: " + service.getUuid().toString());
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                Log.i(TAG, "Value:" + characteristic.getValue());
                Log.i(TAG, "Description:" + characteristic.getDescriptors().toString());
            }
        }

        return null;

    }

    private void write(byte[] data) {

    }

//    private ScanCallback mScanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            super.onScanResult(callbackType, result);
//        }
//
//        @Override
//        public void onBatchScanResults(List<ScanResult> results) {
//            super.onBatchScanResults(results);
//
//            // Get device info.
//            List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
//            for (ScanResult scanResult : results) {
//                devices.add(scanResult.getDevice());
//            }
//
//            //Stop Scanner.
//            mScanner.stopScan(this);
//        }
//
//        @Override
//        public void onScanFailed(int errorCode) {
//            super.onScanFailed(errorCode);
//        }
//    };



    private void connectGattServer(BluetoothDevice device) {
        // Connecting to a GATT Server
        device.connectGatt(this, false, bluetoothGattCallback);
    }

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
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (null != intent) {

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
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
