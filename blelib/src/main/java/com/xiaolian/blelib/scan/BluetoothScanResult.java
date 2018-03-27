package com.xiaolian.blelib.scan;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * @author zcd
 * @date 18/3/27
 */

public class BluetoothScanResult implements Parcelable {
    private BluetoothDevice device;
    private int rssi;
    private byte[] scanRecord;


    public BluetoothScanResult(BluetoothDevice device) {
        this(device, 0, null);
    }

    public BluetoothScanResult(BluetoothDevice device, int rssi, byte[] scanRecord) {
        this.device = device;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
    }

    public String getName() {
        String name = device.getName();
        return TextUtils.isEmpty(name) ? "NULL" : name;
    }

    public String getAddress() {
        return device != null ? device.getAddress() : "";
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public int getRssi() {
        return rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.device, flags);
        dest.writeInt(this.rssi);
        dest.writeByteArray(this.scanRecord);
    }

    protected BluetoothScanResult(Parcel in) {
        this.device = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.rssi = in.readInt();
        this.scanRecord = in.createByteArray();
    }

    public static final Parcelable.Creator<BluetoothScanResult> CREATOR = new Parcelable.Creator<BluetoothScanResult>() {
        @Override
        public BluetoothScanResult createFromParcel(Parcel source) {
            return new BluetoothScanResult(source);
        }

        @Override
        public BluetoothScanResult[] newArray(int size) {
            return new BluetoothScanResult[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BluetoothScanResult that = (BluetoothScanResult) o;

        return device.equals(that.device);
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }
}
