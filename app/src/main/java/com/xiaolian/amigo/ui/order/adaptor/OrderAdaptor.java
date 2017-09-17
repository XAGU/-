package com.xiaolian.amigo.ui.order.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

/**
 * Created by caidong on 2017/9/12.
 */
public class OrderAdaptor extends RecyclerView.Adapter<OrderAdaptor.ViewHolder> {

    private List<Order> orders;
    private Context context;

    public OrderAdaptor(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orders.get(position);
        if (null != order) {
            if (Device.getDevice(order.getType()) == Device.HEARTER) {
                holder.v_type.setBackgroundColor(context.getResources().getColor(R.color.device_heator));
            } else { // Device.getDevice(order.getType()) == Device.DISPENSER
                holder.v_type.setBackgroundColor(context.getResources().getColor(R.color.device_dispenser));
            }
            holder.tv_device.setText(order.getDevice());
            holder.tv_time.setText(order.getTime());
            holder.tv_amount.setText(order.getAmount());
        }
    }

    @Override
    public int getItemCount() {
        return null == orders ? 0 : orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.v_type)
        View v_type;
        @BindView(R.id.tv_device)
        TextView tv_device;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_amount)
        TextView tv_amount;

        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(context, RepairDetailActivity.class);
//            context.startActivity(intent);
        }
    }

    @Data
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
