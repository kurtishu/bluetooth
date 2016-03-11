package com.dreamfactory.library.convert;

/**
 * Author：kurtishu on 3/3/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothConvert {

    /**
     *  封装显示栏需要读取的数据（读取的参数在实现方法体中组装）
     *
     *  READ栏在通过蓝牙发送之前封装数据，通过随机读模式(0x02)
     *
     * @return  经过封装的数据，可以直接通过蓝牙发送出去
     */
    public static native byte[] encapsulateShowData();


    /**
     *  解析显示栏需要读取的数据，数据来源于蓝牙设备
     *
     *  READ栏通过蓝牙接收到数据后，解析包数据
     *
     * @param array 通过蓝牙接收到的数据
     * @return  NULL， 读取的数据不正确，否则读取出正确的数据
     */
    public static native int[] decapsulateShowData(byte[] array);


    /**
     * 封装设置栏需要写入的数据
     *
     * WRITE栏在通过蓝牙发送之前封装数据，通过随机写模式(0x04)
     *
     * @param array 按照写顺序准备的数据
     * @return  NULL， 准备的数据不正确；否则为经过封装的数据，可以直接的通过蓝牙发送。
     */
    public static native byte[] encapsulateSetData(int[] array);

    /**
     * 解析设置栏写入数据后收到的命令
     *
     * WRITE栏通过蓝牙接收到数据后，解析包数据
     *
     * @param array 蓝牙反馈设置API的返回数据
     * @return 0， 准备的数据没有全部写入下位机； 1，写入正常； －1 通信数据不正确
     */
    public static native int decapsulateSetData(byte[] array);

    /**
     * 同步时间
     */
    public static native byte[] encapsulateSysTime(int[] array);
}
