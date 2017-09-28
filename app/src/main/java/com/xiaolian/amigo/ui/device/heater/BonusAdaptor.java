package com.xiaolian.amigo.ui.device.heater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bonus.Bonus;
import com.xiaolian.amigo.tmp.component.recyclerview.BaseWrapperRecyclerAdapter;

import java.util.List;

import lombok.Data;

/**
 * BonusAdapter
 * @author zcd
 */

public class BonusAdaptor extends BaseWrapperRecyclerAdapter<BonusAdaptor.BonusWrapper, BonusAdaptor.ItemViewHolder> {

    public BonusAdaptor(List<BonusWrapper> items) {
        appendToList(items);
    }

    @Override
    public BonusAdaptor.ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bonus, parent, false);

        return new BonusAdaptor.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(BonusAdaptor.ItemViewHolder viewHolder, int position) {
        BonusWrapper bonus = getItem(position);
        viewHolder.tv_amount.setText(bonus.getAmount().toString());
        viewHolder.tv_type.setText(bonus.getType().toString());
        viewHolder.tv_time_end.setText(bonus.getTimeEnd());
        viewHolder.tv_desc.setText(bonus.getDesc());
        viewHolder.tv_time_left.setText(bonus.getTimeLeft().toString());

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position, List payloads) {
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v, position);
            }
        });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_amount;
        TextView tv_type;
        TextView tv_time_end;
        TextView tv_desc;
        TextView tv_time_left;
        Button bt_bonus_use;

        public ItemViewHolder(View view) {
            super(view);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            tv_type = (TextView) view.findViewById(R.id.tv_type);
            tv_time_end = (TextView) view.findViewById(R.id.tv_time_end);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_time_left = (TextView) view.findViewById(R.id.tv_time_left);
            bt_bonus_use = (Button) view.findViewById(R.id.bt_bonus_use);
//            setOnRecyclerItemClickListener(SimpleAdapter.this);
//            addOnItemViewClickListener();
//            addOnViewClickListener(mTvContent);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Data
    public static class BonusWrapper {
        // 红包类型
        Integer type;
        // 红包金额
        Long amount;
        // 到期时间
        String timeEnd;
        // 描述信息
        String desc;
        // 剩余时间
        Long timeLeft;

        public BonusWrapper(Integer type, Long amount, String timeEnd, String desc, Long timeLeft) {
            this.type = type;
            this.amount = amount;
            this.timeEnd = timeEnd;
            this.desc = desc;
            this.timeLeft = timeLeft;
        }

        public BonusWrapper(Bonus bonus) {
            this.type = bonus.getDeviceType();
            this.amount = bonus.getAmount();
            this.timeEnd = bonus.getEndTime();
            this.desc = bonus.getRemarks();
            this.timeLeft = bonus.getTimeLimit();
        }
    }

}
