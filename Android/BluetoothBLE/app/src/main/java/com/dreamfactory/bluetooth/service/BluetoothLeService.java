package com.dreamfactory.bluetooth.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;

import com.dreamfactory.bluetooth.ble.BLEScanner;
import com.dreamfactory.bluetooth.ble.BluetoothGattConnectCallback;
import com.dreamfactory.bluetooth.ble.BluetoothHelper;
import com.dreamfactory.bluetooth.event.ConnectDeviceEvent;
import com.dreamfactory.bluetooth.event.GetServicesEvent;
import com.dreamfactory.bluetooth.event.SelectCharacteristicEvent;
import com.dreamfactory.bluetooth.event.WriteDataEvent;
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

    private BluetoothHelper bluetoothHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);
        bluetoothHelper = new BluetoothHelper(this);
        bluetoothHelper.setScannerListener(scannerListener);
        bluetoothHelper.setDiscoverServiceListener(discoverServiceListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null != intent) {
            String command = intent.getStringExtra(ACTION_COMMAND);
            if (ACTION_SCANDEVICE_START.equals(command)) {
                // scan device
                bluetoothHelper.startScanBLEDevice();
            } else if (ACTION_SCANDEVICE_STOP.equals(command)) {
                // Stop scan device
                bluetoothHelper.stopScanBLEDevice();
            } else if (ACTION_READ_DATA.equals(command)) {
                readeData();
            }
        }
        return START_STICKY;
    }

    private void readeData() {

        byte[] bits = bluetoothHelper.getSelectedCharacteristic().getValue();
        String value = "";
        for (byte b : bits) {
            value += String.valueOf(b);
        }
        LogUtil.i(TAG, "Read Result: "+ value );
        //Intent intent = new Intent(ACTION_MESSAGE);
        //sendBroadcast(intent);
    }

    private void writeData(int[] array) {
        // Convert
        byte[] bytes = new byte[]{1, 2, 3, 4};
        bluetoothHelper.getSelectedCharacteristic().setValue(bytes);
        bluetoothHelper.writeCharacteristic(bluetoothHelper.getSelectedCharacteristic());
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
            bluetoothHelper.setSelectedCharacteristic(event.getCharacteristic());
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
        bluetoothHelper.disconnectDevice();
    }


    private BLEScanner.BLEScannerListener scannerListener = new BLEScanner.BLEScannerListener() {
        @Override
        public void onScanResult(List<BluetoothDevice> device) {
            LogUtil.i(TAG, "====> onScan success" + device.toString());
            Intent intent = new Intent(ACTION_MESSAGE);
            intent.putParcelableArrayListExtra(INTENT_DEVICE_LIST, (ArrayList<? extends Parcelable>) device);
            sendBroadcast(intent);
        }

        @Override
        public void onScanFinished() {
            LogUtil.i(TAG, "====> onScanFinished");
            Intent intent = new Intent(ACTION_MESSAGE);
            sendBroadcast(intent);
        }
    };

    private BluetoothGattConnectCallback.DiscoverServiceListener discoverServiceListener = new BluetoothGattConnectCallback.DiscoverServiceListener() {
        @Override
        public void onDiscoveredServices(List<BluetoothGattService> services) {
            EventBus.getDefault().post(new GetServicesEvent(services));
        }
    };
}
