package com.dreamfactory.bluetooth.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.widget.Toast;


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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    public static final String ACTION_SYNC_DATE = "ACTION_SYNC_DATE";

    private static final String TAG = "BluetoothLeService";

    private BluetoothHelper bluetoothHelper;
    private boolean isReadingCharacteristic = false;
    private boolean isWrittingCharacteristic = false;
    private boolean isSyncDateCharacteristic = false;
    private boolean isWrittingSuccess = false;

    private static final int TIMEOUT_DURATION = 200;

    private int readCnt = 0;
    private int respCnt = 0;
    private int timoutCnt = 0;

    private static final int  MSG_WRITE_FAIL = 0;
    private static final int  MSG_WRITE_SUCCESS = 1;
    private static final int  MSG_WRITE_TIMEOUT = 2;

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
                // When receive a command to scan device
                bluetoothHelper.startScanBLEDevice();
            } else if (ACTION_SCANDEVICE_STOP.equals(command)) {
                // When receive a command to stop scan device
                bluetoothHelper.stopScanBLEDevice();
            } else if (ACTION_READ_DATA.equals(command)) {
               // When receive a command to read setting data
                readeData();
            }else if (ACTION_SYNC_DATE.equals(command)) {
                // When receive a command to sync data
                syncDate();
            }
        }
        return START_STICKY;
    }

    /**
     * 同步时间
     */
    private void syncDate() {
        LogUtil.i(TAG, "sync time");
        int[] sysdate = new int[6];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        sysdate[0] = calendar.get(Calendar.YEAR)-2000;
        sysdate[1] = calendar.get(Calendar.MONTH)+1;
        sysdate[2] = calendar.get(Calendar.DAY_OF_MONTH);
        sysdate[3] = calendar.get(Calendar.HOUR_OF_DAY);
        sysdate[4] = calendar.get(Calendar.MINUTE);
        sysdate[5] = calendar.get(Calendar.SECOND);

        byte[] requestCommad = BluetoothSettingManager.getInstance().setDateTime(sysdate);

        //开始写指令
        bluetoothHelper.getSelectedCharacteristic().setValue(requestCommad);

        // 给蓝牙写特征值，值就写入蓝牙模块
        bluetoothHelper.writeCharacteristic(bluetoothHelper.getSelectedCharacteristic());

        isSyncDateCharacteristic = true;
    }

    /**
     * 完整write过程，实现超时重传
     */
    private void writeProcess(final int[] array) {
        LogUtil.i(TAG, "============> writeProcess");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isWrittingSuccess && (timoutCnt < 5) ) {
                    writeData(array);
                    try {
                        Thread.sleep(TIMEOUT_DURATION);
                    } catch (InterruptedException e) {
                    }
                    if (isWrittingSuccess) {
                        break;
                    }
                    timoutCnt++;
                    LogUtil.i(TAG, "============> writeProcess--Timeout" + timoutCnt);
                }

                Message msg = mHandler.obtainMessage();
                msg.arg1 = (isWrittingSuccess) ? 1 : 0;                  //超过5次超时或写失败则显示提示
                mHandler.sendMessage(msg);

                synchronized (this) {
                    // Restore flags
                    isWrittingSuccess = false;
                    timoutCnt = 0;
                }
            }
        }).start();
    }

    /**
     * 获取设置显示数据
     */
    private void readeData() {
        if (null == bluetoothHelper.getSelectedCharacteristic()) return;

        LogUtil.i(TAG, "readeData start");
        LogUtil.i(TAG, "read_cnt(%d)",readCnt);
        LogUtil.i(TAG, "resp_cnt(%d)",respCnt);

        // 写入读取显示设置的指令
        byte[] requestCommad = BluetoothSettingManager.getInstance().getReadableData();

        //开始写指令
        bluetoothHelper.getSelectedCharacteristic().setValue(requestCommad);

        // 给蓝牙写特征值，值就写入蓝牙模块
        bluetoothHelper.writeCharacteristic(bluetoothHelper.getSelectedCharacteristic());
        //发送读取Characteristic 命令
        bluetoothHelper.readerCharacteristic(bluetoothHelper.getSelectedCharacteristic());
        //设置开始读的标志
        isReadingCharacteristic = true;

        readCnt++;

    }

    /**
     * 设置数据
     * @param array 界面封装好 BluetoothWriteableSetting对象
     */
    private void writeData(int[] array) {
        if (null == bluetoothHelper.getSelectedCharacteristic()) return;

        LogUtil.i(TAG, "writeData start");
        //根据BluetoothWriteableSetting对象，封装设置指令
        byte[] requestCommad = BluetoothSettingManager.getInstance().getWriteableData(array);
        // 写特征值，同上
        bluetoothHelper.getSelectedCharacteristic().setValue(requestCommad);

        bluetoothHelper.writeCharacteristic(bluetoothHelper.getSelectedCharacteristic());

        isWrittingCharacteristic = true;
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
        if (null != event) {
            writeProcess(event.getWriteableSetting());
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

        @Override
        public void onCharacteristicRead(BluetoothGattCharacteristic characteristic) {

            // 获取返回特征值，特征值里面就带有返回结果
            byte[] result = characteristic.getValue();

            if ((result.length > 0) && (result[0] == 0x01)) {
                if (isReadingCharacteristic && (result[1] == 0x02)) {
                    // 解析返回的特征值，这里会调用Native 方法， 返回一个对象 BluetoothReadableSetting
                    BluetoothReadableSetting setting = BluetoothSettingManager.getInstance().getReadableSetting(result);

                    // 显示数据，界面显示（这边使用的EventBus，订阅模式，一种解耦合的方法）
                    EventBus.getDefault().post(new ReadableSettingEvent(setting));
                    isReadingCharacteristic = false;

                    respCnt++;
                }
                if (isWrittingCharacteristic && (result[1] == 0x04)) {
                    int write = BluetoothSettingManager.getInstance().getWriteableSettings(result);
                    //解析返回的结果，获取写操作后的结果 0， 准备的数据没有全部写入下位机； 1，写入正常； －1 通信数据不正确
                    synchronized (this) {
                        isWrittingSuccess = (write == 1) ? true : false;
                    }
                    LogUtil.i(TAG, "--->Setting Page return result:" + write);

                    isWrittingCharacteristic = false;
                }

                if (isSyncDateCharacteristic && (result[1] == 0x03)) {
                    //// TODO: 2016/3/10
                    LogUtil.i(TAG, "date sync finished");
                    isSyncDateCharacteristic = false;
                }
                if ((result[1] == 0x08)) {  //通信错误代码
                    //// TODO: 2016/3/10
                    LogUtil.i(TAG, "**Error Command**");
                }
            }
            else {
                LogUtil.i(TAG, "***************Unknown Data***************");

                for (byte b : result) {
                    LogUtil.i(TAG, "Unknown Data:" + b);
                }
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case MSG_WRITE_FAIL:
                    Toast.makeText(getApplicationContext(), "数据写入失败!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_WRITE_SUCCESS:
                    Toast.makeText(getApplicationContext(), "写入成功!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_WRITE_TIMEOUT:
                    Toast.makeText(getApplicationContext(), "写操作超时!", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }


        }
    };

}
