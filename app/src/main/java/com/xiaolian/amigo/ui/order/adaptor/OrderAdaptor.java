package com.xiaolian.amigo.ui.order.adaptor;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

/**
 * 账单列表adapter
 *
 * @author caidong
 * @date 17/9/12
 */
public class OrderAdaptor extends RecyclerView.Adapter<OrderAdaptor.ViewHolder> {
    private static final int ORDER_ERROR_STATUS = 3;

    public interface OrderDetailClickListener {
        /**
         * 账单列表点击事件
         *
         * @param order 被点击账单
         */
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_balance_detail_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderWrapper wrapper = orders.get(position);
        holder.itemView.setOnClickListener(v ->
                listener.orderDetailClick(holder.order));
        if (null != wrapper) {
            // status为3表示异常订单
            if (ObjectsCompat.equals(wrapper.getOrder().getStatus(), ORDER_ERROR_STATUS)) {
                holder.vType.setBackgroundResource(Device.getDevice(wrapper.getType()).getColorRes());
                holder.tvDevice.setText(wrapper.getDevice());
                holder.tvTime.setText(wrapper.getTime());
                if (!TextUtils.isEmpty(wrapper.getOrder().getExceptionOrderCopy())) {
                    holder.tvAmount.setText(wrapper.getOrder().getExceptionOrderCopy());
                } else {
                    if (Device.getDevice(wrapper.getOrder().getDeviceType()) == Device.WASHER) {
                        // 洗衣机自动退单 触发条件：当用户扫码支付后，未及时使用二维码反扫设备，被他人先行使用
                        // 此时系统自动退单 列表中金额处显示"已退款¥2"
                        holder.tvAmount.setText(String.format("已退款%s", wrapper.getOrder().getActualDebit()));
                    } else {
                        holder.tvAmount.setText("免费");
                    }
                }
                holder.tvMinus.setVisibility(View.GONE);
                holder.order = wrapper.getOrder();
            } else {
                holder.tvMinus.setVisibility(View.VISIBLE);
                holder.vType.setBackgroundResource(Device.getDevice(wrapper.getType()).getColorRes());
                holder.tvDevice.setText(wrapper.getDevice());
                holder.tvTime.setText(wrapper.getTime());
                holder.tvAmount.setText(String.valueOf(wrapper.getAmount()));
                holder.order = wrapper.getOrder();
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == orders ? 0 : orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_bill_type)
        View vType;
        @BindView(R.id.tv_device)
        TextView tvDevice;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.tv_minus)
        TextView tvMinus;

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
        /**
         * 订单类型
         */
        Integer type;
        /**
         * 设备
         */
        String device;
        /**
         * 时间
         */
        String time;
        /**
         * 金额
         */
        String amount;
        /**
         * 原始订单内容，供查询订单详情时使用
         */
        Order order;

        public OrderWrapper(Order order) {
            this.type = order.getDeviceType();
//            this.device = Device.getDevice(order.getDeviceType()).getDesc() + "：" + order.getLocation();
            this.device = Device.getDevice(order.getDeviceType()).getDesc();
            this.time = stampToDate(order.getCreateTime());
            this.amount = order.getActualDebit();
            this.order = order;
        }

        public static String stampToDate(Long s) {
            if (s == null) {
                return "----";
            }
//            String res;
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
//                    Locale.getDefault());
//            Date date = new Date(s);
//            res = simpleDateFormat.format(date);
            return TimeUtils.orderTimestampFormat(s);
        }
    }
}
