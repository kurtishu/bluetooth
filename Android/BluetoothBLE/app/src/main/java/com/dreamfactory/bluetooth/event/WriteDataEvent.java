package com.dreamfactory.bluetooth.event;

/**
 * Author：kurtishu on 2/29/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class WriteDataEvent {

    private final int[] datas;

    public WriteDataEvent(int[] datas) {
        this.datas = datas;
    }

    public int[] getDatas() {
        return datas;
    }
}
