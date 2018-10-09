package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.content.Context;
import android.view.View;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/12
 */
public class ShowerAddressAdapter extends CommonAdapter<ShowerAddressAdapter.ShowerAddressWrapper> {
    private OnItemLongClickListener longClickListener;
    public ShowerAddressAdapter(Context context, int layoutId, List<ShowerAddressWrapper> datas) {
        super(context, layoutId, datas);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, ShowerAddressWrapper showerAddressWrapper, int position) {
        holder.setText(R.id.tv_content, showerAddressWrapper.getAddress());
        if (showerAddressWrapper.isSelected()) {
            holder.getView(R.id.iv_selected).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.iv_selected).setVisibility(View.GONE);
        }
        holder.getView(R.id.ll_item).setOnLongClickListener(v -> {
            longClickListener.onItemLongClick();
            return true;
        });
    }

    public interface OnItemLongClickListener {
        /**
         * 列表长按事件
         */
        void onItemLongClick();
    }

    @Data
    public static final class ShowerAddressWrapper {
        private Long id;
        private String address;
        private boolean selected = false;

        public ShowerAddressWrapper(String address) {
            this.address = address;
        }
    }
}
