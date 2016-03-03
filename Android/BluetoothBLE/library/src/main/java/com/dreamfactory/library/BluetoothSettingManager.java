package com.dreamfactory.library;

import android.util.Log;

import com.dreamfactory.library.convert.BluetoothConvert;
import com.dreamfactory.library.model.BluetoothReadableSetting;
import com.dreamfactory.library.model.BluetoothWriteableSetting;

/**
 * Author：kurtishu on 3/3/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothSettingManager {

    static {
        System.loadLibrary("BluetoothLib");
    }

    private static BluetoothSettingManager instance;

    private BluetoothSettingManager() {
        // Empty
    }

    public static BluetoothSettingManager getInstance() {
        if (null == instance) {
            synchronized (BluetoothSettingManager.class) {
                if (null == instance) {
                    instance = new BluetoothSettingManager();
                }
            }
        }

        return instance;
    }

    /**
     * 获取封装好的数据，展示显示栏的数据
     *
     * @return
     */
    public byte[] getReadableData() {
        return BluetoothConvert.encapsulateShowData();
    }

    /**
     * 解析蓝牙返回显示栏的数据
     * @return BluetoothReadableSetting 对象
     */
    public BluetoothReadableSetting getReadableSetting(byte[] array) {

        int[] result = BluetoothConvert.decapsulateShowData(array);
        for (int res : result) {
            Log.i("Kurtis", "getReadableSetting:" + res);
        }

        BluetoothReadableSetting setting = new BluetoothReadableSetting();
        setting.setSnoringTime(1);
        setting.setSgWorkingTime(2);
        setting.setSgWorkingTimes(3);
        setting.setInflatedTime(4);
        setting.setDeflateTime(5);
        setting.setPumpStatus(6);
        setting.setQuietTime(7);
        setting.setSgStatus(8);

        return setting;
    }

    public byte[] getWriteableData(BluetoothWriteableSetting setting) {
        return BluetoothConvert.encapsulateSetData(null);
    }

    /**
     *  获取设置结果
     * @param array 蓝牙返回数据
     * @return  0， 准备的数据没有全部写入下位机； 1，写入正常； －1 通信数据不正确
     */
    public int getWriteableSettings(byte[] array) {
        return BluetoothConvert.decapsulateSetData(array);
    }
}
