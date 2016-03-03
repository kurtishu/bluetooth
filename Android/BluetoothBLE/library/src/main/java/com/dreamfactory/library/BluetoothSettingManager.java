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
     * @return BluetoothReadableSetting 对象，在外面直接使用对象
     */
    public BluetoothReadableSetting getReadableSetting(byte[] array) {

        // 解析返回数据，现在返回的是 1 2， 然后把返回的数据封装成 BluetoothReadableSetting对象
        int[] result = BluetoothConvert.decapsulateShowData(array);
        for (int res : result) {
            Log.i("Kurtis", "getReadableSetting:" + res);
        }

        //  返回一个对象，现在是mock的数据， 以后要换成实际的数据
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

    /**
     *  将设置界面封装好的对象，封装成可以发送到蓝牙段的数据
     *
     * @param setting 设置界面封装的对象
     *
     * @return NULL， 准备的数据不正确；否则为经过封装的数据，可以直接的通过蓝牙发送。
     */
    public byte[] getWriteableData(BluetoothWriteableSetting setting) {
        //获取 BluetoothWriteableSetting 对象里的属性 比如
        int array[] = new int[13];
        array[0] = setting.getTimingSetting();
        // array[1]...
        // 按照要求 赋值


        return BluetoothConvert.encapsulateSetData(array);
    }

    /**
     *  获取设置结果（解析设置操作后蓝牙端返回的数据）
     * @param array 蓝牙返回数据
     * @return  0， 准备的数据没有全部写入下位机； 1，写入正常； －1 通信数据不正确
     */
    public int getWriteableSettings(byte[] array) {
        return BluetoothConvert.decapsulateSetData(array);
    }
}
