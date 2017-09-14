package com.xiaolian.amigo.tmp.activity.order.adaptor;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import java.util.List;

/**
 * Created by caidong on 2017/9/7.
 */

public class OrderAdaptor extends ArrayAdapter<OrderAdaptor.Order> {

    private int resourceId;
    private Context context;

    public OrderAdaptor(@NonNull Context context, @LayoutRes int resource, List<Order> bonuses) {
        super(context, resource, bonuses);
        this.context = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.tv_device = (TextView) view.findViewById(R.id.tv_device);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHolder.tv_amount = (TextView) view.findViewById(R.id.tv_amount);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    private class ViewHolder {
        TextView tv_type;
        TextView tv_device;
        TextView tv_time;
        TextView tv_amount;
    }

    public static class Order {
        // 订单类型
        Integer type;
        // 设备
        String device;
        // 时间
        String time;
        // 金额
        Integer amount;

        public Order(Integer type, String device, String time, Integer amount) {
            this.type = type;
            this.device = device;
            this.time = time;
            this.amount = amount;
        }
    }
}
