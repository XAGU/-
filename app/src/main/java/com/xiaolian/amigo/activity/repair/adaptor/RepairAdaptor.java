package com.xiaolian.amigo.activity.repair.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.repair.RepairDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/12.
 */

public class RepairAdaptor extends RecyclerView.Adapter<RepairAdaptor.ViewHolder> {

    private List<Repair> repairs;

    public RepairAdaptor(List<Repair> repairs) {
        this.repairs = repairs;
    }

    public void updateData(List<Repair> repairs) {
        this.repairs = repairs;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO 绑定数据
        // holder.tv_device.setText(repairs.get(position).device);
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

    public static class Repair {
        // 设备
        String device;
        // 时间
        String time;
        // 维修状态
        Integer stautus;

        public Repair(String device, String time, Integer stautus) {
            this.device = device;
            this.time = time;
            this.stautus = stautus;
        }
    }
}
