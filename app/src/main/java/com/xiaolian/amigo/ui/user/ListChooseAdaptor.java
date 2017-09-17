package com.xiaolian.amigo.ui.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ListChooseAdapter
 * @author zcd
 */
public class ListChooseAdaptor extends RecyclerView.Adapter<ListChooseAdaptor.ViewHolder> {

    private List<Item> datas;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public ListChooseAdaptor(List<Item> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_choose, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
        holder.tv_content.setText(datas.get(position).content);
        if (datas.get(position).tick) {
            holder.iv_tick.setVisibility(View.VISIBLE);
        } else {
            holder.iv_tick.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.iv_tick)
        ImageView iv_tick;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class Item {
        String content;
        boolean tick;

        public Item(String content, boolean tick) {
            this.content = content;
            this.tick = tick;
        }
    }
}
