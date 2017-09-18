package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.RepairStatus;
import com.xiaolian.amigo.data.network.model.repair.Repair;
import com.xiaolian.amigo.tmp.activity.repair.RepairDetailActivity;
import com.xiaolian.amigo.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/12.
 */
public class RepairAdaptor extends RecyclerView.Adapter<RepairAdaptor.ViewHolder> {

    private List<RepairWrapper> repairs;
    private Context context;

    public RepairAdaptor(List<RepairWrapper> repairs) {
        this.repairs = repairs;
    }

    public void updateData(List<RepairWrapper> repairs) {
        this.repairs = repairs;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair, parent, false);
        context = view.getContext();
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RepairWrapper wrapper = repairs.get(position);
        holder.tv_device.setText(wrapper.device);
        holder.tv_time.setText(wrapper.time);
        holder.tv_status.setText(wrapper.status);
        holder.tv_status.setBackgroundResource(RepairStatus.getStatus(wrapper.status).getCorlorRes());
    }

    @Override
    public int getItemCount() {
        return null == repairs ? 0 : repairs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_device)
        TextView tv_device;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_status)
        TextView tv_status;

        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RepairDetailActivity.class);
            context.startActivity(intent);
        }
    }

    public static class RepairWrapper {
        // 设备
        String device;
        // 时间
        String time;
        // 维修状态
        Integer status;

        public RepairWrapper(Repair repair) {
            this.device = Device.getDevice(repair.getDeviceType()) + Constant.CHINEASE_COLON + repair.getHardwareNo();
            this.time = repair.getCreateTime();
            this.status = repair.getStatus();
        }
    }
}
