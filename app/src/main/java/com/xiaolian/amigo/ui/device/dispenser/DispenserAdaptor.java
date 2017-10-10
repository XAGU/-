package com.xiaolian.amigo.ui.device.dispenser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.recyclerview.BaseWrapperRecyclerAdapter;

import java.util.List;

/**
 * 饮水机适配器
 * @author zcd
 */

public class DispenserAdaptor extends BaseWrapperRecyclerAdapter<DispenserAdaptor.WaterFountain, DispenserAdaptor.ItemViewHolder> {

    public DispenserAdaptor(List<WaterFountain> items) {
        appendToList(items);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder holder, int position) {
        WaterFountain waterFountain = getItem(position);
        holder.tv_device.setText(waterFountain.type == 0 ? "饮水机" : "");
        holder.tv_location.setText(waterFountain.location);


    }

    public static class WaterFountain {
        // 设备类型
        Integer type;
        // 设备位置
        String location;

        public WaterFountain(Integer type, String location) {
            this.type = type;
            this.location = location;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_device;
        TextView tv_location;

        public ItemViewHolder(View view) {
            super(view);
            tv_device = (TextView) view.findViewById(R.id.tv_device);
            tv_location = (TextView) view.findViewById(R.id.tv_location);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
