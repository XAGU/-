package com.xiaolian.amigo.tmp.activity.lostandfound.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.repair.RepairDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/12.
 */
public class LostAndFoundAdaptor extends RecyclerView.Adapter<LostAndFoundAdaptor.ViewHolder> {

    private List<Info> infos;

    public LostAndFoundAdaptor(List<Info> infos) {
        this.infos = infos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost_and_found, parent, false);
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
        return null == infos ? 0 : infos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_title)
        LinearLayout ll_title;
        @BindView(R.id.ll_good)
        LinearLayout ll_good;
        @BindView(R.id.ll_location)
        LinearLayout ll_location;
        @BindView(R.id.ll_time)
        LinearLayout ll_time;

        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_good)
        TextView tv_good;
        @BindView(R.id.tv_location)
        TextView tv_location;
        @BindView(R.id.tv_time)
        TextView tv_time;

        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }

    public static class Info {
        // 标题
        String title;
        // 内容
        String content;
        // 物品
        String good;
        // 地点
        String location;
        // 时间
        String time;

        public Info(String title, String content, String good, String location, String time) {
            this.title = title;
            this.content = content;
            this.good = good;
            this.location = location;
            this.time = time;
        }
    }
}
