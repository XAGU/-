package com.xiaolian.amigo.ui.order.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.util.CommonUtil;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

/**
 * Created by caidong on 2017/9/12.
 */
public class OrderAdaptor extends RecyclerView.Adapter<OrderAdaptor.ViewHolder> {
    public interface OrderDetailClickListener {
        void orderDetailClick(Order order);
    }
    private OrderDetailClickListener listener;
    private List<OrderWrapper> orders;
    private Context context;
    private int errorOrderColorRes = R.color.order_error;

    public OrderAdaptor(List<OrderWrapper> orders) {
        this.orders = orders;
    }

    public void setOrderDetailClickListener(OrderDetailClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderWrapper wrapper = orders.get(position);
        holder.itemView.setOnClickListener(v -> {
            listener.orderDetailClick(holder.order);
        });
        if (null != wrapper) {
            // status为3表示异常订单
            if (CommonUtil.equals(wrapper.getOrder().getStatus(), 3)) {
                holder.v_type.setBackgroundResource(errorOrderColorRes);
                holder.tv_device.setText(wrapper.getDevice());
                holder.tv_time.setText(wrapper.getTime());
                holder.tv_amount.setText("免费");
                holder.tv_minus.setVisibility(View.GONE);
                holder.order = wrapper.getOrder();
            } else {
                holder.tv_minus.setVisibility(View.VISIBLE);
                holder.v_type.setBackgroundResource(Device.getDevice(wrapper.getType()).getColorRes());
                holder.tv_device.setText(wrapper.getDevice());
                holder.tv_time.setText(wrapper.getTime());
                holder.tv_amount.setText(String.valueOf(wrapper.getAmount()));
                holder.order = wrapper.getOrder();
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == orders ? 0 : orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_type)
        View v_type;
        @BindView(R.id.tv_device)
        TextView tv_device;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_amount)
        TextView tv_amount;
        @BindView(R.id.tv_minus)
        TextView tv_minus;

        Context context;
        Order order;

        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }

    @Data
    public static class OrderWrapper {
        // 订单类型
        Integer type;
        // 设备
        String device;
        // 时间
        String time;
        // 金额
        String amount;
        // 原始订单内容，供查询订单详情时使用
        Order order;

        public OrderWrapper(Order order) {
            this.type = order.getDeviceType();
            this.device = Device.getDevice(order.getDeviceType()).getDesc() + "：" + order.getLocation();
            this.time = CommonUtil.stampToDate(order.getCreateTime());
            this.amount = order.getConsume();
            this.order = order;
        }
    }
}
