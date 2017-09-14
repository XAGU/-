package com.xiaolian.amigo.tmp.activity.common.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.repair.RepairDetailActivity;
import com.xiaolian.amigo.tmp.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/12.
 */
public class LocationAdaptor extends RecyclerView.Adapter<LocationAdaptor.ViewHolder> {

    private List<Location> locations;
    private Class<? extends BaseActivity> targetClazz;

    public LocationAdaptor(List<Location> locations, Class<? extends BaseActivity> targetClazz) {
        this.locations = locations;
        this.targetClazz = targetClazz;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, targetClazz);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO 绑定数据
        holder.tv_loc.setText(locations.get(position).loc);
        if (locations.get(position).status == 1) {
            holder.iv_status.setVisibility(View.VISIBLE);
        } else { // locations.get(position).status == 2
            holder.iv_status.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return null == locations ? 0 : locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_loc)
        TextView tv_loc;
        @BindView(R.id.iv_status)
        ImageView iv_status;

        Context context;
        Class<? extends BaseActivity> targetClazz;

        public ViewHolder(View itemView, Class<? extends BaseActivity> targetClazz) {
            super(itemView);
            this.context = itemView.getContext();
            this.targetClazz = targetClazz;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, targetClazz);
            context.startActivity(intent);
        }
    }

    public static class Location {
        // 位置
        String loc;
        // 选中状态
        Integer status;

        public Location(String loc, Integer status) {
            this.loc = loc;
            this.status = status;
        }
    }
}
