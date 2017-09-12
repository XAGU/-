package com.xiaolian.amigo.activity.bonus.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.bonus.viewmodel.Bonus;

import java.util.List;

/**
 * Created by adamzfc on 9/12/17.
 */

public class BonusAdaptor2 extends BaseWrapperRecyclerAdapter<Bonus, BonusAdaptor2.ItemViewHolder> {

    public BonusAdaptor2(List<Bonus> items) {
        appendToList(items);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bonus, parent, false);

        return new BonusAdaptor2.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(BonusAdaptor2.ItemViewHolder viewHolder, int position) {
        Bonus bonus = getItem(position);
        viewHolder.tv_amount.setText(bonus.getAmount().toString());
        viewHolder.tv_type.setText(bonus.getType().toString());
        viewHolder.tv_time_end.setText(bonus.getTimeEnd());
        viewHolder.tv_desc.setText(bonus.getDesc());
        viewHolder.tv_time_left.setText(bonus.getTimeLeft().toString());

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position, List payloads) {
        if(payloads != null && payloads.size() > 0 && vh instanceof BonusAdaptor2.ItemViewHolder){
            for(Object o : payloads){
                if(o != null && o instanceof Integer) {
//                    ((BonusAdaptor2.ItemViewHolder) vh).mTvContent.setTextColor((Integer) o);
                }
            }
        } else {
            super.onBindViewHolder(vh, position);
        }
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

}
