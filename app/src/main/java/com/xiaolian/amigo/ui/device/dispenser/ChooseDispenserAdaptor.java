package com.xiaolian.amigo.ui.device.dispenser;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.network.model.device.ScanDevice;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 饮水机适配器
 * @author zcd
 */

public class ChooseDispenserAdaptor extends CommonAdapter<ChooseDispenserAdaptor.DispenserWrapper> {
    private OnItemClickListener mOnItemClickListener;
    private int lastExpandPosition = -1;
    private Context context;
    public ChooseDispenserAdaptor(Context context, int layoutId, List<DispenserWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, DispenserWrapper dispenserWrapper, int position) {
        holder.setText(R.id.tv_location, dispenserWrapper.getLocation());
        holder.getView(R.id.rl_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(v, position);
//                }
                if (lastExpandPosition != -1) {
                    getDatas().get(lastExpandPosition).setExpanded(false);
                }
                if (lastExpandPosition == position) {
                    getDatas().get(position).setExpanded(false);
                    lastExpandPosition = -1;
                } else {
                    getDatas().get(position).setExpanded(true);
                    lastExpandPosition = position;
                }
                notifyDataSetChanged();

            }
        });
        if (dispenserWrapper.isExpanded()) {
            holder.getView(R.id.v_divide).setVisibility(View.VISIBLE);
            holder.getView(R.id.rl_bottom).setVisibility(View.VISIBLE);
            lastExpandPosition = position;
        } else {
            holder.getView(R.id.v_divide).setVisibility(View.GONE);
            holder.getView(R.id.rl_bottom).setVisibility(View.GONE);
        }
        // 添加饮水机点击事件
        // 冷水
        holder.getView(R.id.tv_cold_water).setEnabled(dispenserWrapper.getCold() != null);
        holder.getView(R.id.tv_cold_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context.getApplicationContext(), DispenserActivity.class)
                        .putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS, dispenserWrapper.getCold().getMacAddress())
                        .putExtra(DispenserActivity.INTENT_KEY_FAVOR, dispenserWrapper.isFavor())
                        .putExtra(DispenserActivity.INTENT_KEY_ID, dispenserWrapper.getCold().getId())
                        .putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE, DispenserWater.COLD.getType())
                        .putExtra(MainActivity.INTENT_KEY_LOCATION, dispenserWrapper.getLocation())
                        .putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType()));
            }
        });
        // 冰水
        holder.getView(R.id.tv_ice_water).setEnabled(dispenserWrapper.getIce() != null);
        holder.getView(R.id.tv_ice_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context.getApplicationContext(), DispenserActivity.class)
                        .putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS, dispenserWrapper.getIce().getMacAddress())
                        .putExtra(DispenserActivity.INTENT_KEY_FAVOR, dispenserWrapper.isFavor())
                        .putExtra(DispenserActivity.INTENT_KEY_ID, dispenserWrapper.getIce().getId())
                        .putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE, DispenserWater.ICE.getType())
                        .putExtra(MainActivity.INTENT_KEY_LOCATION, dispenserWrapper.getLocation())
                        .putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType()));
            }
        });
        // 热水
        holder.getView(R.id.tv_hot_water).setEnabled(dispenserWrapper.getHot() != null);
        holder.getView(R.id.tv_hot_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context.getApplicationContext(), DispenserActivity.class)
                        .putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS, dispenserWrapper.getHot().getMacAddress())
                        .putExtra(DispenserActivity.INTENT_KEY_FAVOR, dispenserWrapper.isFavor())
                        .putExtra(DispenserActivity.INTENT_KEY_ID, dispenserWrapper.getHot().getId())
                        .putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE, DispenserWater.HOT.getType())
                        .putExtra(MainActivity.INTENT_KEY_LOCATION, dispenserWrapper.getLocation())
                        .putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType()));
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Data
    public static class DispenserWrapper {
        // 设备位置
        String location;
        ScanDeviceGroup deviceGroup;
        boolean expanded = false;
        boolean favor = false;
        ScanDevice cold;
        ScanDevice ice;
        ScanDevice hot;

        public DispenserWrapper(ScanDeviceGroup device) {
            this.favor = device.getFavor();
            this.location = device.getLocation();
            this.deviceGroup = device;
            for (String key : device.getWater().keySet()) {
                if (TextUtils.equals(key, DispenserWater.HOT.getType())) {
                    this.hot = device.getWater().get(key);
                } else if (TextUtils.equals(key, DispenserWater.COLD.getType())) {
                    this.cold = device.getWater().get(key);
                } else if (TextUtils.equals(key, DispenserWater.ICE.getType())) {
                    this.ice = device.getWater().get(key);
                }
            }
        }
    }

}
