package com.dreamfactory.bluetooth.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;

import com.dreamfactory.bluetooth.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：kurtishu on 3/2/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BLEScanner {

    private static final String TAG = "BLEScanner";
    private static final int DEFAULTSCANDURATION = 60000;
    private BluetoothAdapter mAdapter;
    private BLEScannerListener scannerListener;
    private DeviceLeScanCallback mDeviceLeScanCallback;
    private boolean isScanning;
    private List<BluetoothDevice> mDevices;


    public BLEScanner(BluetoothAdapter adapter) {
        this.mAdapter = adapter;
        mDevices = new ArrayList<>();
    }


    //=================================Scan Device Start=========================
    public void startLeScan() {

        LogUtil.i(TAG, "============> Start scan");
        mDevices.clear();

        if (!isScanning) {
            new Handler().postDelayed(new Runnable() {
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
            mAdapter.startLeScan(callback);
        }
        isScanning = true;
    }

    private void performStopLeScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BluetoothLeScanner scanner = mAdapter.getBluetoothLeScanner();
            scanner.stopScan(mDeviceLeScanCallback);

        } else {
            mAdapter.stopLeScan(callback);
        }

        isScanning = false;
        if (null != scannerListener) {
            scannerListener.onScanFinished();
        }
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
            if (null != scannerListener) {
                scannerListener.onScanFinished();
            }
        }
    }


    private BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            addDevice(device);
        }
    };

    private void addDevice(BluetoothDevice targetDevice) {
        mDevices.add(targetDevice);

        if (null != scannerListener) {
            scannerListener.onScanResult(mDevices);
        }
    }

    public void setBLEScannerListener(BLEScannerListener scannerListener) {
        this.scannerListener = scannerListener;
    }

    public interface BLEScannerListener {

        public void onScanResult(List<BluetoothDevice> device);

        public void onScanFinished();
    }
}
