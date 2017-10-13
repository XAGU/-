package com.xiaolian.amigo.ui.device.dispenser;

import android.content.Context;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.device.Device;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 饮水机适配器
 * @author zcd
 */

public class ChooseDispenserAdaptor extends CommonAdapter<ChooseDispenserAdaptor.DispenserWapper> {
    private OnItemClickListener mOnItemClickListener;
    private int lastExpandPosition = -1;
    public ChooseDispenserAdaptor(Context context, int layoutId, List<DispenserWapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, DispenserWapper dispenserWapper, int position) {
        holder.setText(R.id.tv_location, dispenserWapper.getLocation());
        holder.getView(R.id.rl_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(v, position);
//                }
                if (lastExpandPosition != -1) {
                    getDatas().get(lastExpandPosition).setExpanded(false);
                }
                if (lastExpandPosition == position) {
                    getDatas().get(position).setExpanded(false);
                    lastExpandPosition = -1;
                } else {
                    getDatas().get(position).setExpanded(true);
                    lastExpandPosition = position;
                }
                notifyDataSetChanged();

            }
        });
        if (dispenserWapper.isExpanded()) {
            holder.getView(R.id.v_divide).setVisibility(View.VISIBLE);
            holder.getView(R.id.rl_bottom).setVisibility(View.VISIBLE);
            lastExpandPosition = position;
        } else {
            holder.getView(R.id.v_divide).setVisibility(View.GONE);
            holder.getView(R.id.rl_bottom).setVisibility(View.GONE);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Data
    public static class DispenserWapper {
        // 设备位置
        String location;
        Device device;
        boolean expanded = false;

        public DispenserWapper(Device device) {
            this.location = device.getLocation();
            this.device = device;
        }
    }

}
