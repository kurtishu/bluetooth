package com.dreamfactory.bluetooth.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;

import com.dreamfactory.bluetooth.ble.BLEScanner;
import com.dreamfactory.bluetooth.ble.BluetoothGattConnectCallback;
import com.dreamfactory.bluetooth.ble.BluetoothHelper;
import com.dreamfactory.bluetooth.event.ConnectDeviceEvent;
import com.dreamfactory.bluetooth.event.GetServicesEvent;
import com.dreamfactory.bluetooth.event.ReadableSettingEvent;
import com.dreamfactory.bluetooth.event.SelectCharacteristicEvent;
import com.dreamfactory.bluetooth.event.WriteableSettingEvent;
import com.dreamfactory.bluetooth.util.LogUtil;
import com.dreamfactory.library.BluetoothSettingManager;
import com.dreamfactory.library.model.BluetoothReadableSetting;
import com.dreamfactory.library.model.BluetoothWriteableSetting;

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
                testing();
            }
        }
        return START_STICKY;
    }

    /**
     * 获取显示数据
     */
    private void readeData() {
        if (null == bluetoothHelper.getSelectedCharacteristic()) return;

        LogUtil.i(TAG, "readeData start");

        // 写入读取显示设置的指令
        byte[] requestCommad = BluetoothSettingManager.getInstance().getReadableData();
        bluetoothHelper.getSelectedCharacteristic().setValue(requestCommad);
        bluetoothHelper.writeCharacteristic(bluetoothHelper.getSelectedCharacteristic());

        byte[] result = bluetoothHelper.getSelectedCharacteristic().getValue();
        //校验数据的完整性。。 省略

        BluetoothReadableSetting setting = BluetoothSettingManager.getInstance().getReadableSetting(result);

        // 显示数据
        EventBus.getDefault().post(new ReadableSettingEvent(setting));
    }

    /**
     * 设置数据
     * @param setting
     */
    private void writeData(BluetoothWriteableSetting setting) {
        if (null == bluetoothHelper.getSelectedCharacteristic()) return;

        LogUtil.i(TAG, "writeData start");
        //获取设置指令
        byte[] requestCommad = BluetoothSettingManager.getInstance().getWriteableData(setting);

        bluetoothHelper.getSelectedCharacteristic().setValue(requestCommad);
        bluetoothHelper.writeCharacteristic(bluetoothHelper.getSelectedCharacteristic());

        byte[] result = bluetoothHelper.getSelectedCharacteristic().getValue();

        //校验数据的完整性。。 省略

        //获取写操作后的结果
        int status = BluetoothSettingManager.getInstance().getWriteableSettings(result);
        LogUtil.i(TAG, "writeData with status " + status);
    }


    /**
     * 测试四个接口
     */
    private void testing() {
        byte[] requestCommad = BluetoothSettingManager.getInstance().getReadableData();
        for (byte req : requestCommad) {
            LogUtil.i(TAG, "requestCommad: " + String.valueOf(req));
        }
        BluetoothReadableSetting setting = BluetoothSettingManager.getInstance().getReadableSetting(null);
        LogUtil.i(TAG, "Readable result: " + setting.toString());

        byte[] requestCommad2 = BluetoothSettingManager.getInstance().getWriteableData(null);
        for (byte req : requestCommad2) {
            LogUtil.i(TAG, "requestCommad2: " + String.valueOf(req));
        }

        int result = BluetoothSettingManager.getInstance().getWriteableSettings(null);
        LogUtil.i(TAG, "Writeable result: " + result);
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

    public void onEvent(WriteableSettingEvent event) {
        if (null != event ) {
            writeData(event.getWriteableSetting());
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
