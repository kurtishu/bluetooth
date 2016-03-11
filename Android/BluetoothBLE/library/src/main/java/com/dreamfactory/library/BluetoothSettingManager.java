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

    private static int READ_INDEX_LEN  = 8;     //显示界面数据长度
    private static int WRITE_INDEX_LEN = 13;    //设置界面数据长度

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
        BluetoothReadableSetting setting = null;
        int[] result = BluetoothConvert.decapsulateShowData(array);

        if ( result  != null ) {

            for (int res : result) {
                Log.i("Kurtis", "getReadableSetting:" + res);
            }

            //  返回一个对象，现在是mock的数据， 以后要换成实际的数据
            setting = new BluetoothReadableSetting();

            setting.setSnoringTime(result[0]);          //打鼾时间
            setting.setQuietTime(result[1]);            //安静时间
            setting.setSgWorkingTime(result[2]);        //SG工作时间
            setting.setSgWorkingTimes(result[3]);       //SG工作次数
            setting.setSgStatus(result[4]);             //SG状态
            setting.setPumpStatus(result[5]);           //泵状态
            setting.setInflatedTime(result[6]);         //充气时间
            setting.setDeflateTime(result[7]);          //放气时间
        }

        return setting;
    }

    /**
     *  将设置界面封装好的对象，封装成可以发送到蓝牙段的数据
     *
     * @param array 设置界面封装的修改的数据
     *
     * @return NULL， 准备的数据不正确；否则为经过封装的数据，可以直接的通过蓝牙发送。
     */
    public byte[] getWriteableData(int[] array) {
//        int array[] = new int[WRITE_INDEX_LEN];
//
//        array[0] = setting.getTimingSetting();      //定时时间设置
//        array[1] = setting.getStartTimingHour();    //定时开始时间(小时)
//        array[2] = setting.getStartTimingMis();     //定时开始时间(分钟)
//        array[3] = setting.getEndTimingHour();      //定时结束时间(小时)
//        array[4] = setting.getEndTimingMis();       //定时结束时间(分钟)
//        array[5] = setting.getInflatedTime();       //充气时间设置
//        array[6] = setting.getDeflatedTime();       //放气时间设置
//        array[7] = setting.getWorkingThreshold();   //工作阈值设置
//        array[8] = setting.getDegree();             //枕头软硬程度
//        array[9] = setting.getWokingTime();         //工作延时设置
//        array[10] = setting.getRestDevice();        //复位设备
//        array[11] = setting.getResetData();         //恢复出厂
//        array[12] = setting.getClearData();         //清除数据

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

    /**
     *  封装时间数据
     *
     * @param array 系统时间
     *
     * @return NULL， 准备的数据不正确；否则为经过封装的数据，可以直接的通过蓝牙发送。
     */
    public byte[] setDateTime(int[] array) {
        return BluetoothConvert.encapsulateSysTime(array);
    }
}
