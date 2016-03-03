package com.dreamfactory.library.model;

/**
 * Author：kurtishu on 3/3/16
 * Eevery one should have a dream, what if one day it comes true!
 *
 * Model for display setting info
 */
public class BluetoothReadableSetting {

    //打鼾时间
    private int snoringTime;

    // 安静时间
    private int quietTime;

    private int sgWorkingTime;

    private int sgWorkingTimes;

    private int sgStatus;

    private int pumpStatus;

    private int inflatedTime;

    private int deflateTime;


    public int getSnoringTime() {
        return snoringTime;
    }

    public void setSnoringTime(int snoringTime) {
        this.snoringTime = snoringTime;
    }

    public int getQuietTime() {
        return quietTime;
    }

    public void setQuietTime(int quietTime) {
        this.quietTime = quietTime;
    }

    public int getSgWorkingTime() {
        return sgWorkingTime;
    }

    public void setSgWorkingTime(int sgWorkingTime) {
        this.sgWorkingTime = sgWorkingTime;
    }

    public int getSgWorkingTimes() {
        return sgWorkingTimes;
    }

    public void setSgWorkingTimes(int sgWorkingTimes) {
        this.sgWorkingTimes = sgWorkingTimes;
    }

    public int getSgStatus() {
        return sgStatus;
    }

    public void setSgStatus(int sgStatus) {
        this.sgStatus = sgStatus;
    }

    public int getPumpStatus() {
        return pumpStatus;
    }

    public void setPumpStatus(int pumpStatus) {
        this.pumpStatus = pumpStatus;
    }

    public int getInflatedTime() {
        return inflatedTime;
    }

    public void setInflatedTime(int inflatedTime) {
        this.inflatedTime = inflatedTime;
    }

    public int getDeflateTime() {
        return deflateTime;
    }

    public void setDeflateTime(int deflateTime) {
        this.deflateTime = deflateTime;
    }

    @Override
    public String toString() {
        return "BluetoothReadableSetting{" +
                "snoringTime=" + snoringTime +
                ", quietTime=" + quietTime +
                ", sgWorkingTime=" + sgWorkingTime +
                ", sgWorkingTimes=" + sgWorkingTimes +
                ", sgStatus=" + sgStatus +
                ", pumpStatus=" + pumpStatus +
                ", inflatedTime=" + inflatedTime +
                ", deflateTime=" + deflateTime +
                '}';
    }
}
