package com.dreamfactory.bluetooth.ble;

import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Handler;

import com.dreamfactory.bluetooth.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：kurtishu on 1/14/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothHelper {

    private static final String TAG = "BluetoothHelper";
    private static final int DEFAULTSCANDURATION = 60000;
    private Context mContext;
    private BluetoothAdapter mAdapter;
    private BluetoothGattConnectCallback.DiscoverServiceListener discoverServiceListener;
    private BluetoothGatt mBluetoothGatt;
    private BLEScanner bleScanner;
    private BluetoothGattCharacteristic selectedCharacteristic;

    public BluetoothHelper(Context mContext) {

        final BluetoothManager bluetoothManager =
                (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = bluetoothManager.getAdapter();

        if (!mAdapter.isEnabled()) {
            //Auto enable bluttooth.
            mAdapter.enable();
        }

        bleScanner = new BLEScanner(mAdapter);
    }

    /**
     * 开始搜索设备
     */
    public void startScanBLEDevice() {
        bleScanner.startLeScan();
    }

    /**
     * 停止搜索设备
     */
    public void stopScanBLEDevice() {
        bleScanner.stopLeScan();
    }

    /**
     * 连接设备
     * Connect Connect to GATT Server hosted by this device.
     *
     * @param device select device;
     */
    public void connectDevice(BluetoothDevice device) {
        mBluetoothGatt = device.connectGatt(mContext, false,
                new BluetoothGattConnectCallback(discoverServiceListener));
    }

    /**
     * 断开连接设备
     * Disconnects an established connection, or cancels a connection attempt
     * currently in progress.
     */
    public void disconnectDevice() {
        if (null != mBluetoothGatt) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt = null;
        }
    }

    /**
     * 获取Service，好像暂时没用的
     */
    public void getServices() {
        List<BluetoothGattService> result = mBluetoothGatt.getServices();
        LogUtil.i(TAG, "getServices=" + result.toString());
        for (BluetoothGattService service : result) {
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                LogUtil.i(TAG, "characteristic =" + String.valueOf(characteristic.getValue()));
            }
        }
    }

    /**
     * 获取Characteristics
     */
    public List<BluetoothGattCharacteristic> getCharacteristics(BluetoothGattService service) {
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : characteristics) {

            LogUtil.i(TAG, "characteristic =" + String.valueOf(characteristic.getValue()));
        }
        return characteristics;
    }

    /**
     * 往BluetoothGattCharacteristic上写数据
     */
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic){
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    /**
     * 读取BluetoothGattCharacteristic数据
     */
    public void readerCharacteristic(BluetoothGattCharacteristic characteristic) {
        mBluetoothGatt.setCharacteristicNotification(characteristic, true);
        mBluetoothGatt.readCharacteristic(characteristic);
    }


    public void setSelectedCharacteristic(BluetoothGattCharacteristic selectedCharacteristic) {
        this.selectedCharacteristic = selectedCharacteristic;
    }

    public BluetoothGattCharacteristic getSelectedCharacteristic() {
        return selectedCharacteristic;
    }

    public void setDiscoverServiceListener(BluetoothGattConnectCallback.DiscoverServiceListener discoverServiceListener) {
        this.discoverServiceListener = discoverServiceListener;
    }

    public void setScannerListener(BLEScanner.BLEScannerListener scannerListener) {
        bleScanner.setBLEScannerListener(scannerListener);
    }
}
