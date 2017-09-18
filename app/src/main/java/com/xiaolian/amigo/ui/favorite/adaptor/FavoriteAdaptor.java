package com.xiaolian.amigo.ui.favorite.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.device.Device;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

/**
 * Created by caidong on 2017/9/12.
 */
public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.ViewHolder> {

    private List<FavoriteWrapper> favorites;
    private Context context;

    public FavoriteAdaptor(List<FavoriteWrapper> favorites) {
        this.favorites = favorites;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FavoriteWrapper wrapper = favorites.get(position);
        if (null != wrapper) {
            holder.tv_device.setText(com.xiaolian.amigo.data.enumeration.Device.getDevice(wrapper.getType()).getDesc());
            holder.tv_location.setText(wrapper.getLocation());
        }
    }

    @Override
    public int getItemCount() {
        return null == favorites ? 0 : favorites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device)
        TextView tv_device;
        @BindView(R.id.tv_location)
        TextView tv_location;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Data
    public static class FavoriteWrapper {
        // 设备类型
        Integer type;
        // 设备
        String location;

        public FavoriteWrapper(Device device) {
            this.type = device.getType();
            this.location = device.getLocation();
        }

        public FavoriteWrapper(Integer type, String location) {
            this.type = type;
            this.location = location;
        }
    }
}
