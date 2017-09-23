package com.xiaolian.amigo.ui.ble.adaptor;

import android.content.Context;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * Created by caidong on 2017/9/23.
 */
public class BLEAdaptor extends CommonAdapter<BLEAdaptor.Device> {

    public BLEAdaptor(Context context, int layout, List<BLEAdaptor.Device> devices) {
        super(context, layout, devices);
    }

    @Override
    protected void convert(ViewHolder holder, BLEAdaptor.Device device, int position) {
        holder.setText(R.id.name, device.name);
        holder.setText(R.id.mac, device.mac);
        holder.setText(R.id.rssi, device.rssi.toString());
    }


    public static class Device {
        String name;
        String mac;
        Integer rssi;

        public Device(ScanResult result) {
            this.name = result.getBleDevice().getName();
            this.mac = result.getBleDevice().getMacAddress();
            this.rssi = result.getRssi();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Device device = (Device) o;

            return mac != null ? mac.equals(device.mac) : device.mac == null;

        }

        @Override
        public int hashCode() {
            return mac != null ? mac.hashCode() : 0;
        }
    }
}
