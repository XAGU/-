package com.xiaolian.amigo.activity.device.waterfountain;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.base.BaseActivity;
import com.xiaolian.amigo.component.recyclerview.BaseWrapperRecyclerAdapter;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by adamzfc on 9/13/17.
 */

public class WaterFuntainAdaptor extends BaseWrapperRecyclerAdapter<WaterFuntainAdaptor.WaterFuntation, WaterFuntainAdaptor.ItemViewHolder> {

    public WaterFuntainAdaptor(List<WaterFuntation> items) {
        appendToList(items);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder holder, int position) {
        WaterFuntation waterFuntation = getItem(position);
        holder.tv_device.setText(waterFuntation.type == 0 ? "饮水机" : "");
        holder.tv_location.setText(waterFuntation.location);


    }

    public static class WaterFuntation {
        // 设备类型
        Integer type;
        // 设备位置
        String location;

        public WaterFuntation(Integer type, String location) {
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
