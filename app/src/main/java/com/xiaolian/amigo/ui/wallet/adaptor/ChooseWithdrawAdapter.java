package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.dto.response.UserThirdAccountDTO;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 选择提现
 * <p>
 * Created by zcd on 10/27/17.
 */

public class ChooseWithdrawAdapter extends CommonAdapter<ChooseWithdrawAdapter.Item> {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnDeleteListener onDeleteListener;

    public ChooseWithdrawAdapter(Context context, int layoutId, List<Item> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Item item, int position) {
        holder.setText(R.id.tv_content, item.getContent());
        holder.getView(R.id.rl_item).setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
        holder.getView(R.id.rl_item).setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick();
            }
            return true;
        });
        holder.getView(R.id.tv_delete).setOnClickListener(v -> {
            if (onDeleteListener != null) {
                onDeleteListener.onDelete(holder.getAdapterPosition());
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick();
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.onDeleteListener = listener;
    }

    @Data
    public static class Item implements Serializable {
        private String content;
        private Long id;

        public Item(UserThirdAccountDTO dto) {
            this.content = dto.getAccountName();
            this.id = dto.getId();
        }

    }
}
