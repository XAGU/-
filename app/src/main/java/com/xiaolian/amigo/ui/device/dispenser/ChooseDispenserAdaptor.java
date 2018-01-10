package com.xiaolian.amigo.ui.device.dispenser;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.network.model.device.WaterInListDTO;
import com.xiaolian.amigo.data.vo.ScanDeviceGroup;

import java.util.List;

import lombok.Data;

/**
 * 饮水机适配器
 * @author zcd
 */

public class ChooseDispenserAdaptor extends RecyclerView.Adapter<ChooseDispenserAdaptor.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(String deviceNo, Boolean isFavor, Long residenceId, String usefor, String location, Integer price);
    }
    private OnItemClickListener itemClickListener;
    private int lastExpandPosition = -1;
    private List<ChooseDispenserAdaptor.DispenserWrapper> mData;
    private int layoutId;
    private Context context;
    private boolean expandAble = true;

    public ChooseDispenserAdaptor(Context context, int layoutId,
                                  List<DispenserWrapper> datas ,
                                  boolean expandable) {
        this.mData = datas;
        this.context = context;
        this.layoutId = layoutId;
        this.expandAble = expandable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChooseDispenserAdaptor.DispenserWrapper dispenserWrapper =
                mData.get(holder.getAdapterPosition());
        holder.tv_location.setText(dispenserWrapper.getLocation());
        if (!expandAble) {
            holder.iv_arrow.setVisibility(View.GONE);
        }
        holder.tv_title.setText(Device.getDevice(dispenserWrapper.getDeviceGroup().getType()).getDesc());
        holder.tv_title.setTextColor(ContextCompat.getColor(context,
                Device.getDevice(dispenserWrapper.getDeviceGroup().getType()).getColorRes()));
        holder.rl_top.setOnClickListener(v -> {
            if (!expandAble && itemClickListener != null) {
                itemClickListener.onItemClick(dispenserWrapper.getDeviceGroup().getWater().get(0).getMacAddress(),
                        dispenserWrapper.isFavor(),
                        dispenserWrapper.getResidenceId(),
                        null,
                        dispenserWrapper.getLocation(),
                        dispenserWrapper.getDeviceGroup().getWater().get(0).getPrice());
                return;
            }
            if (lastExpandPosition != -1) {
                mData.get(lastExpandPosition).setExpanded(false);
            }
            boolean expand;
            if (lastExpandPosition == holder.getAdapterPosition()) {
                expand = false;
                mData.get(holder.getAdapterPosition()).setExpanded(false);
                lastExpandPosition = -1;
            } else {
                expand = true;
                mData.get(holder.getAdapterPosition()).setExpanded(true);
                lastExpandPosition = holder.getAdapterPosition();
            }
            ObjectAnimator anim =
                    ObjectAnimator.ofFloat(holder.iv_arrow,
                            "rotation", expand ? 0f : -180f, expand ? 180f : 0f);
            anim.setDuration(200);
            anim.start();
            notifyDataSetChanged();
        });
        if (dispenserWrapper.isExpanded()) {
            holder.v_divide.setVisibility(View.VISIBLE);
            holder.rl_bottom.setVisibility(View.VISIBLE);
            lastExpandPosition = holder.getAdapterPosition();
        } else {
            if (holder.rl_bottom.getVisibility() == View.VISIBLE) {
                ObjectAnimator anim =
                        ObjectAnimator.ofFloat(holder.iv_arrow,
                                "rotation", -180f, 0f);
                anim.setDuration(200);
                anim.start();
            }
            holder.v_divide.setVisibility(View.GONE);
            holder.rl_bottom.setVisibility(View.GONE);
        }
        for (TextView textView : holder.tv_water) {
            textView.setVisibility(View.GONE);
        }
        for (int i = 0; i < dispenserWrapper.getDeviceGroup().getWater().size(); i ++) {
            final int waterPosition = i;
            String tempName = dispenserWrapper.getDeviceGroup().getWater().get(i).getName();
            if (TextUtils.isEmpty(tempName)) {
                holder.tv_water[i].setText(
                        DispenserWater.getTemperature(dispenserWrapper.getDeviceGroup().getWater()
                                .get(i).getUsefor()).getDesc());
            } else {
                holder.tv_water[i].setText(tempName);
            }
            holder.tv_water[i].setVisibility(View.VISIBLE);
            holder.tv_water[i].setBackgroundResource(DispenserWater.getTemperature(dispenserWrapper.getDeviceGroup().getWater()
                    .get(i).getUsefor()).getBackgroundDrawable());
            holder.tv_water[i].setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(dispenserWrapper.getDeviceGroup().getWater().get(waterPosition).getMacAddress(),
                            dispenserWrapper.isFavor(),
                            dispenserWrapper.getResidenceId(),
                            dispenserWrapper.getDeviceGroup().getWater().get(waterPosition).getUsefor(),
                            dispenserWrapper.getLocation(),
                            dispenserWrapper.getDeviceGroup().getWater().get(waterPosition).getPrice());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_location;
        TextView tv_title;
        RelativeLayout rl_top;
        View v_divide;
        RelativeLayout rl_bottom;
        TextView[] tv_water;
        ImageView iv_arrow;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_water = new TextView[] {
                    itemView.findViewById(R.id.tv_cold_water),
                    itemView.findViewById(R.id.tv_ice_water),
                    itemView.findViewById(R.id.tv_hot_water)
            };
            iv_arrow = itemView.findViewById(R.id.iv_arrow);
            tv_location = itemView.findViewById(R.id.tv_location);
            rl_top = itemView.findViewById(R.id.rl_top);
            v_divide = itemView.findViewById(R.id.v_divide);
            rl_bottom = itemView.findViewById(R.id.rl_bottom);
        }
    }

    @Data
    public static class DispenserWrapper {
        // 设备位置
        String location;
        ScanDeviceGroup deviceGroup;
        boolean expanded = false;
        boolean favor = false;
        Long residenceId;

        public DispenserWrapper(ScanDeviceGroup device) {
            this.favor = device.getFavor();
            this.location = device.getLocation();
            this.deviceGroup = device;
            this.residenceId = device.getResidenceId();
        }
    }

}
