package com.dreamfactory.bluetooth.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import com.dreamfactory.bluetooth.util.LogUtil;

import java.util.List;

/**
 * Author：kurtishu on 3/2/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothGattConnectCallback extends BluetoothGattCallback {

    private static final String TAG = "BluetoothGattConnectCallback";
    private DiscoverServiceListener discoverServiceListener;

    public BluetoothGattConnectCallback(DiscoverServiceListener discoverServiceListener) {
        this.discoverServiceListener = discoverServiceListener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        LogUtil.i(TAG, "onConnectionStateChange status=" + status + " newState=" + newState + " Service:" + gatt.getServices());

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        LogUtil.i(TAG, "onServicesDiscovered status=" + status + "Service:" + gatt.getServices());
        if (null != discoverServiceListener) {
            discoverServiceListener.onDiscoveredServices(gatt.getServices());
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        LogUtil.i(TAG, "onCharacteristicRead status=" + status + " characteristic =" + characteristic.toString());

        byte[] bytes = characteristic.getValue();
        for (byte b : bytes) {
            LogUtil.i(TAG, "onCharacteristicRead result:" + b);
        }

        if (null != discoverServiceListener) {
            discoverServiceListener.onCharacteristicRead(characteristic);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        LogUtil.i(TAG, "onCharacteristicWrite status=" + status + " characteristic =" + characteristic.toString());
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        LogUtil.i(TAG, "onCharacteristicChanged characteristic=" + characteristic);
        byte[] bytes = characteristic.getValue();
        for (byte b : bytes) {
            LogUtil.i(TAG, "onCharacteristicChanged result:" + b);
        }
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
        LogUtil.i(TAG, "onDescriptorRead status=" + status + " descriptor=" + descriptor.toString());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        LogUtil.i(TAG, "onDescriptorWrite status=" + status + " descriptor=" + descriptor.toString());
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
        LogUtil.i(TAG, "onReliableWriteCompleted status=" + status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        LogUtil.i(TAG, "onReadRemoteRssi status=" + status);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
        LogUtil.i(TAG, "onMtuChanged status=" + status);
    }


    public interface DiscoverServiceListener {

        void onDiscoveredServices(List<BluetoothGattService> services);

        void onCharacteristicRead(BluetoothGattCharacteristic characteristic);

    }
}
