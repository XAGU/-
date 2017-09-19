package com.xiaolian.amigo.tmp.activity.repair.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/12.
 */

public class RepairProgressAdaptor extends RecyclerView.Adapter<RepairProgressAdaptor.ViewHolder> {

    private List<Progress> progresses;

    public RepairProgressAdaptor(List<Progress> progresses) {
        this.progresses = progresses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair_progress, parent, false);
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
        return null == progresses ? 0 : progresses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_status)
        TextView tv_status;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_desc)
        TextView tv_desc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class Progress {
        // 进度描述
        String device;
        // 时间
        String time;
        // 进度状态
        Integer stautus;

        public Progress(String device, String time, Integer stautus) {
            this.device = device;
            this.time = time;
            this.stautus = stautus;
        }
    }
}
