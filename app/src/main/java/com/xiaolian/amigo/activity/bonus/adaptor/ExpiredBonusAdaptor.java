package com.xiaolian.amigo.activity.bonus.adaptor;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import java.util.List;

/**
 * Created by caidong on 2017/9/7.
 */

public class ExpiredBonusAdaptor extends ArrayAdapter<ExpiredBonusAdaptor.Bonus> {

    private int resourceId;
    private Context context;

    public ExpiredBonusAdaptor(@NonNull Context context, @LayoutRes int resource, List<Bonus> bonuses) {
        super(context, resource, bonuses);
        this.context = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bonus bonus = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.tv_time_end = (TextView) view.findViewById(R.id.tv_time_end);
            viewHolder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            viewHolder.iv_expired = (ImageView) view.findViewById(R.id.iv_expired);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
//        viewHolder.tv_amount.setText(bonus.amount.toString());
//        viewHolder.tv_type.setText(bonus.type.toString());
//        viewHolder.tv_time_end.setText(bonus.timeEnd);
//        viewHolder.tv_desc.setText(bonus.desc);
//        viewHolder.tv_time_left.setText(bonus.timeLeft.toString());
        return view;
    }

    private class ViewHolder {
        TextView tv_amount;
        TextView tv_type;
        TextView tv_time_end;
        TextView tv_desc;
        ImageView iv_expired;
    }

    public static class Bonus {
        // 红包类型
        Integer type;
        // 红包金额
        Integer amount;
        // 到期时间
        String timeEnd;
        // 描述信息
        String desc;

        public Bonus(Integer type, Integer amount, String timeEnd, String desc) {
            this.type = type;
            this.amount = amount;
            this.timeEnd = timeEnd;
            this.desc = desc;
        }
    }
}
