package com.xiaolian.amigo.ui.ble.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.ble.BleInteractiveActivity;
import com.xiaolian.amigo.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by caidong on 2017/9/23.
 */
public class BleAdaptor extends RecyclerView.Adapter<BleAdaptor.ViewHolder> {

    private List<BleAdaptor.Device> devices;
    private Context context;

    public BleAdaptor(List<BleAdaptor.Device> devices) {
        this.devices = devices;
    }

    @Override
    public BleAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ble_device, parent, false);
        return new BleAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BleAdaptor.ViewHolder holder, int position) {
        BleAdaptor.Device device = devices.get(position);
        if (null != device) {
            holder.name.setText(device.name);
            holder.mac.setText(device.mac);
            holder.rssi.setText(device.rssi.toString());
        }
    }

    @Override
    public int getItemCount() {
        return null == devices ? 0 : devices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.mac)
        TextView mac;
        @BindView(R.id.rssi)
        TextView rssi;

        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            // 跳转至订单详情
            Intent intent = new Intent(context, BleInteractiveActivity.class);
            intent.putExtra(Constant.MAC, mac.getText());
            context.startActivity(intent);
        }
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

        public String getMac() {
            return mac;
        }

        @Override
        public int hashCode() {
            return mac != null ? mac.hashCode() : 0;
        }
    }
}
