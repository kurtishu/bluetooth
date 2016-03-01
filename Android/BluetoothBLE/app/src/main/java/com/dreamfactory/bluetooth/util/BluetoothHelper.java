package com.dreamfactory.bluetooth.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：kurtishu on 1/14/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothHelper implements BluetoothAdapter.LeScanCallback {

    private static final String TAG = "BluetoothHelper";
    private static final int DEFAULTSCANDURATION = 60000;
    private Context mContext;
    private BluetoothAdapter mAdapter;
    private BluetoothCallback mBluetoothCallback;
    private List<BluetoothDevice> mDevices;
    private Handler mHandler;
    private DeviceLeScanCallback mDeviceLeScanCallback;
    private boolean isScanning;
    private BluetoothGatt mBluetoothGatt;


    public BluetoothHelper(Context mContext, Handler mHandler, BluetoothAdapter mAdapter) {
        this.mContext = mContext;
        this.mAdapter = mAdapter;
        this.mHandler = mHandler;
        mDevices = new ArrayList<BluetoothDevice>();
    }

    //=================================Scan Device Start=========================
    public void startLeScan(BluetoothCallback callback) {

        LogUtil.i(TAG, "============> Start scan");
        mDevices.clear();
        mBluetoothCallback = callback;

        performStopLeScan();

        if (!isScanning) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isScanning) {
                        performStopLeScan();
                    }
                }
            }, DEFAULTSCANDURATION);
            performStartLeScan();
        }
    }

    public void stopLeScan() {
        LogUtil.i(TAG, "============> Stop scan");
        performStopLeScan();
    }

    private void performStartLeScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BluetoothLeScanner scanner = mAdapter.getBluetoothLeScanner();
            mDeviceLeScanCallback = new DeviceLeScanCallback();
            scanner.startScan(mDeviceLeScanCallback);
        } else {
            mAdapter.startLeScan(this);
        }
        isScanning = true;
    }

    private void performStopLeScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            BluetoothLeScanner scanner = mAdapter.getBluetoothLeScanner();
            scanner.stopScan(mDeviceLeScanCallback);

        } else {
            mAdapter.stopLeScan(this);
        }

        if (null != mBluetoothCallback) {
            mBluetoothCallback.onScanFinished();
        }
        isScanning = false;
    }

    // 旧的API的Callback
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        LogUtil.i(TAG, "onLeScan result: " + device.toString());
        addDevice(device);
    }

    private void addDevice(BluetoothDevice targetDevice) {
        mDevices.add(targetDevice);
        if (null != mBluetoothCallback) {
            mBluetoothCallback.onScan(mDevices);
        }
    }

    public List<BluetoothDevice> getDeviceList() {
        return mDevices;
    }

    //=================================Scan Device End=========================

    /**
     * Connect Connect to GATT Server hosted by this device.
     *
     * @param device select device;
     */
    public void connectDevice(BluetoothDevice device) {
        mBluetoothGatt = device.connectGatt(mContext, false, new MyBluetoothGattConnectCallback());

    }

    /**
     * Disconnects an established connection, or cancels a connection attempt
     * currently in progress.
     */
    public void disconnectDevice() {
        if (null != mBluetoothGatt) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt = null;
        }
    }


    //=================================Connect Device=========================

    public void getServices() {
        List<BluetoothGattService> result = mBluetoothGatt.getServices();
        LogUtil.i(TAG, "getServices=" + result.toString());
        for (BluetoothGattService service : result) {
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                LogUtil.i(TAG, "characteristic =" + String.valueOf(characteristic.getValue()));
            }
        }
        if (null != mBluetoothCallback) {
            mBluetoothCallback.getServices(result);
        }
    }

    public List<BluetoothGattCharacteristic> getCharacteristics(BluetoothGattService service) {
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : characteristics) {

            LogUtil.i(TAG, "characteristic =" + String.valueOf(characteristic.getValue()));
        }
        return characteristics;
    }

    public interface BluetoothCallback {

        public void onScan(List<BluetoothDevice> devices);

        public void onScanFailed();

        public void onScanFinished();

        public void getServices(List<BluetoothGattService> services);
    }

    // 新的API的Callback
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    class DeviceLeScanCallback extends ScanCallback {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            LogUtil.i(TAG, "onScanResult " + result.toString());
            addDevice(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            LogUtil.i(TAG, "onBatchScanResults" + results.toString());
            for (ScanResult result : results) {
                addDevice(result.getDevice());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            LogUtil.i(TAG, "on ScanFaild with error code = " + errorCode);
            if (null != mBluetoothCallback) {
                mBluetoothCallback.onScanFailed();
            }
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic){
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public void readerCharacteristic(BluetoothGattCharacteristic characteristic) {
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    class MyBluetoothGattConnectCallback extends BluetoothGattCallback {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            LogUtil.i(TAG, "onConnectionStateChange status=" + status + " newState=" + newState + " Service:" + gatt.getServices());

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            LogUtil.i(TAG, "onServicesDiscovered status=" + status + "Service:" + gatt.getServices());
            getServices();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            LogUtil.i(TAG, "onCharacteristicRead status=" + status + " characteristic =" + characteristic.toString());
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            LogUtil.i(TAG, "onCharacteristicWrite status=" + status + " characteristic =" + characteristic.toString());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            LogUtil.i(TAG, "onServicesDiscovered characteristic=" + characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            LogUtil.i(TAG, "onDescriptorRead status=" + status + " descriptor=" + descriptor.toString());
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            LogUtil.i(TAG, "onDescriptorWrite status=" + status + " descriptor=" + descriptor.toString());
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            LogUtil.i(TAG, "onReliableWriteCompleted status=" + status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            LogUtil.i(TAG, "onReadRemoteRssi status=" + status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            LogUtil.i(TAG, "onMtuChanged status=" + status);
        }
    }
}
