package com.dreamfactory.bluetooth.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
    private Context mContext;
    private BluetoothAdapter mAdapter;
    private DeviceScanCallback mDeviceScanCallback;
    private List<BluetoothDevice> mDevices;
    private Handler mHandler;
    private DeviceLeScanCallback mDeviceLeScanCallback;
    private boolean isScanning;


    public BluetoothHelper(Context mContext, Handler mHandler, BluetoothAdapter mAdapter) {
        this.mContext = mContext;
        this.mAdapter = mAdapter;
        this.mHandler = mHandler;
        mDevices = new ArrayList<BluetoothDevice>();
    }

   //=================================Scan Device Start=========================
   public void startLeScan(DeviceScanCallback callback) {

       LogUtil.i(TAG, "============> Start scan");
        mDevices.clear();
        mDeviceScanCallback = callback;
        if (!isScanning) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  if (isScanning) {
                      performStopLeScan();
                  }
                }
            }, 10000);
            performStartLeScan();
        }
    }

   public void stopLeScan() {
       LogUtil.i(TAG, "============> Stop scan");
       mDeviceScanCallback = null;
       performStopLeScan();
   }

    private void performStartLeScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            BluetoothLeScanner scanner =  mAdapter.getBluetoothLeScanner();
            mDeviceLeScanCallback = new DeviceLeScanCallback();
            scanner.startScan(mDeviceLeScanCallback);

        } else {
            mAdapter.startLeScan(this);
        }
        isScanning = true;
    }

    private void performStopLeScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            BluetoothLeScanner scanner =  mAdapter.getBluetoothLeScanner();
            scanner.stopScan(mDeviceLeScanCallback);

        } else {
            mAdapter.stopLeScan(this);
        }

        if (null != mDeviceScanCallback) {
            mDeviceScanCallback.onScanFinished();
        }
        isScanning = false;
    }

    // 新的API的Callback
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    class DeviceLeScanCallback extends ScanCallback {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            addDevice(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            if (null != mDeviceScanCallback) {
                mDeviceScanCallback.onScanFailed();
            }
        }
    }

    // 旧的API的Callback
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (null == device && null != mDeviceScanCallback) {
            mDeviceScanCallback.onScanFailed();
            return;
        }
        addDevice(device);
    }


    private void addDevice(BluetoothDevice targetDevice) {
        for (BluetoothDevice device : mDevices) {
            if (!device.getAddress().equals(targetDevice.getAddress())) {
                mDevices.add(targetDevice);
            }
        }

        if (null != mDeviceScanCallback) {
            mDeviceScanCallback.onScan(mDevices);
        }
    }

    //=================================Scan Device End=========================

   public interface DeviceScanCallback {

        public void onScan(List<BluetoothDevice> devices);

       public void onScanFailed();

       public void onScanFinished();
    }
}
