package com.xiaolian.amigo.ui.device.dispenser;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;

import java.util.List;

import lombok.Data;

/**
 * 饮水机适配器
 * @author zcd
 */

public class ChooseDispenserAdaptor extends RecyclerView.Adapter<ChooseDispenserAdaptor.ViewHolder> {
    private int lastExpandPosition = -1;
    private List<ChooseDispenserAdaptor.DispenserWrapper> mData;
    private Context context;
    private int layoutId;
    private IChooseDispenserPresenter<IChooseDispenerView> presenter;
    private OrderPreInfoDTO orderPreInfo;

    public ChooseDispenserAdaptor(Context context, int layoutId,
                                  List<DispenserWrapper> datas ,
                                  IChooseDispenserPresenter<IChooseDispenerView> presenter,
                                  OrderPreInfoDTO orderPreInfo) {
        this.context = context;
        this.mData = datas;
        this.layoutId = layoutId;
        this.presenter = presenter;
        this.orderPreInfo = orderPreInfo;
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
        holder.rl_top.setOnClickListener(v -> {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(v, position);
//                }
            if (lastExpandPosition != -1) {
                mData.get(lastExpandPosition).setExpanded(false);
            }
            boolean expand = false;
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
            holder.tv_water[i].setText(
                    DispenserWater.getTemperature(dispenserWrapper.getDeviceGroup().getWater()
                            .get(i).getUsefor()).getDesc());
            holder.tv_water[i].setVisibility(View.VISIBLE);
            holder.tv_water[i].setBackgroundResource(DispenserWater.getTemperature(dispenserWrapper.getDeviceGroup().getWater()
                    .get(i).getUsefor()).getBackgroundDrawable());
            holder.tv_water[i].setOnClickListener(v -> {
                presenter.closeBleConnection();
                presenter.gotoDispenser(dispenserWrapper.getDeviceGroup().getWater().get(waterPosition).getMacAddress(),
                        dispenserWrapper.isFavor(), dispenserWrapper.getResidenceId(),
                        dispenserWrapper.getDeviceGroup().getWater().get(waterPosition).getUsefor(),
                        dispenserWrapper.getLocation());
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_location;
        RelativeLayout rl_top;
        View v_divide;
        RelativeLayout rl_bottom;
        TextView[] tv_water;
        TextView tv_cold_water;
        TextView tv_ice_water;
        TextView tv_hot_water;
        ImageView iv_arrow;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_water = new TextView[] {
                    itemView.findViewById(R.id.tv_cold_water),
                    itemView.findViewById(R.id.tv_ice_water),
                    itemView.findViewById(R.id.tv_hot_water)
            };
            iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            rl_top = (RelativeLayout) itemView.findViewById(R.id.rl_top);
            v_divide = itemView.findViewById(R.id.v_divide);
            rl_bottom = (RelativeLayout) itemView.findViewById(R.id.rl_bottom);
//            tv_cold_water = (TextView) itemView.findViewById(R.id.tv_cold_water);
//            tv_ice_water = (TextView) itemView.findViewById(R.id.tv_ice_water);
//            tv_hot_water = (TextView) itemView.findViewById(R.id.tv_hot_water);
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
