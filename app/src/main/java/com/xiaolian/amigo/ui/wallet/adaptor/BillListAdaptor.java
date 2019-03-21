package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.RechargeStatus;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;
import com.xiaolian.amigo.data.enumeration.WithdrawalStatus;
import com.xiaolian.amigo.data.network.model.funds.FundsInListDTO;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.data.network.model.order.OrderInListDTO;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

public class BillListAdaptor extends CommonAdapter<BillListAdaptor.BillListAdaptorWrapper> {
    public static final int XLFilterContentViewBillTypeRecharge = 1/*余额充值*/;
    public static final int XLFilterContentViewBillTypeWithdraw = 2/*余额退款*/;
    /*下面这些类型需要根据是否开通才显示*/
    public static final int XLFilterContentViewBillTypePayHeater = 3/*热水澡*/;
    public static final int XLFilterContentViewBillTypePayDrinking = 4/*饮水机*/;
    public static final int XLFilterContentViewBillTypePayBlower = 5/*吹风机*/;
    public static final int XLFilterContentViewBillTypePayWashing = 6/*洗衣机*/;
//    public static final int XLFilterContentViewBillTypePayPublicBath = 7/*公共浴室，已合并到热水澡中*/;
    public static final int XLFilterContentViewBillTypePayDry = 8/*烘干机*/;
    public static final int XLFilterContentViewBillTypePayBill = 9/*消费账单*/;

    /**
     * #222
     */
    public static final int COLOR_222 = 1 ;

    /**
     * #ff5555
     */
    public static final int COLOR_ff5555 = 2  ;

    private Context context;

    public BillListAdaptor(Context context, int layoutId, List<BillListAdaptor.BillListAdaptorWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }


    @Override
    protected void convert(ViewHolder holder, BillListAdaptor.BillListAdaptorWrapper billListAdaptorWrapper, int position) {
        //设置type类型
        holder.setText(R.id.tv_bill_type, BillListAdaptor.getTypeName(billListAdaptorWrapper.getType(), billListAdaptorWrapper.getStatus()));
        //设置时间
        holder.setText(R.id.tv_bill_time, TimeUtils.millis2String(billListAdaptorWrapper.getCreateTime()));
        //设置金额和状态（有关联，所以需要一起设置）
        setMoneyAndStatus(holder, billListAdaptorWrapper.getAmount(), billListAdaptorWrapper.getType(), billListAdaptorWrapper.getStatus());
    }

    private void setMoneyAndStatus(ViewHolder holder, String amount, int billType, int status) {
        Log.e("COLOR", "setMoneyAndStatus: " + status +"\t" + amount  );
        TextView moneyView = holder.getView(R.id.tv_bill_money);
        TextView statusView = holder.getView(R.id.tv_bill_status);
        TextView statusViewOnly = holder.getView(R.id.tv_bill_Status_only);
        moneyView.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.VISIBLE);
        statusViewOnly.setVisibility(View.INVISIBLE);
        if (status < 100 ) /*status的状态不同，此处两边的值状态不一致！！！*/{
            holder.setText(R.id.tv_bill_status, WithdrawalStatus.getWithdrawalStatus(status).getDesc());
            setAmount(holder ,amount ,"-￥" , "-");
            return;
        }
        /*1xx表示消费订单，2xx表示充值，3xx表示提现，4xx表示活动*/
        if (status == 100) /*预付待找零*/{
            setAmount(holder ,amount ,"-￥" ,"-");
            holder.setText(R.id.tv_bill_status, "待找零");
            setColor( moneyView ,COLOR_222);
        } else if (status == 101) /*订单已完结*/{
            setAmount(holder ,amount ,"-￥" , "-");
            statusView.setVisibility(View.INVISIBLE);
            setColor(moneyView , COLOR_222);
        } else if (status == 102) /*已退单*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "已退款");
        } else if (status == 103) /*异常订单，一般不会出现*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "订单异常");
        } else if (status == 200) /*充值成功*/{
            setAmount(holder ,amount ,"+￥","+");
            holder.setText(R.id.tv_bill_status, "充值成功");
            setColor(moneyView , COLOR_ff5555);
        } else if (status == 201) /*充值失败*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "充值失败");
        } else if (status == 202) /*取消充值*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "取消充值");
        } else if (status == 203) /*充值异常，一般不会出现*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "充值异常");
        } else if (status == 204) /*等待到账*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "等待到账");
        } else if (status == 300) /*提现待审核*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "待审核");
        } else if (status == 301) /*提现审核未通过*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "审核未通过");
        } else if (status == 302) /*提现失败*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "退款失败");
        } else if (status == 303) /*等待到账*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "等待到账");
        } else if (status == 304) /*提现成功*/{
            setAmount(holder ,amount ,"-￥" , "-");
            holder.setText(R.id.tv_bill_status, "退款成功");
            setColor(moneyView , COLOR_222);
        } else if (status == 305) /*取消提现*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "取消退款");
        } else if (status == 306) /*提现异常，一般不会出现*/{
            moneyView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            statusViewOnly.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_bill_Status_only, "退款异常");
        } else if (status == 400) /*活动*/{
            setAmount(holder ,amount ,"+￥" , "+");
            statusView.setVisibility(View.INVISIBLE);
            setColor(moneyView , COLOR_ff5555);
        }
    }


    private void setColor(TextView view,int colorType){
        switch (colorType){
            case COLOR_222:
                view.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case COLOR_ff5555:
                view.setTextColor(context.getResources().getColor(R.color.colorFullRed));
                break;
        }
    }

    /**
     * 设置金钱金额
     * @param amount
     * @param preSymbol
     */
    private void setAmount(ViewHolder holder,String amount , String preSymbol , String pre){
        if (amount.contains("¥")) {
            holder.setText(R.id.tv_bill_money,  amount);
        }else {
            holder.setText(R.id.tv_bill_money, preSymbol+amount);
        }
    }

    public static String getTypeName(int billType, int status) {
        if (billType == XLFilterContentViewBillTypeRecharge) {
            return "余额充值";
        } else if (billType == XLFilterContentViewBillTypeWithdraw) {
            return "余额退款";
        } else if (billType == XLFilterContentViewBillTypePayHeater) {
            return "热水澡消费";
        } else if (billType == XLFilterContentViewBillTypePayDrinking) {
            return "饮水机消费";
        } else if (billType == XLFilterContentViewBillTypePayWashing) {
            return "洗衣机消费";
        } else if (billType == XLFilterContentViewBillTypePayBlower) {
            return "吹风机消费";
        }  else if (billType == XLFilterContentViewBillTypePayDry) {
            return "烘干机消费";
        }
        return "活动";//活动status>=400

    }

    @Data
    public static class BillListAdaptorWrapper {
        private int type;
        private String amount;
        private Long createTime;
        private Long detailId;
        private int status;
        private Long id;
        private int deviceType ;

        public BillListAdaptorWrapper(HashMap<String, Object> billDic) {
            this.type = ((Double)billDic.get("type")).intValue();
            Map<String, Object> billDetail = (Map<String, Object>) billDic.get("billDetail");
            this.amount = (String) billDetail.get("amount");
            this.detailId = ((Double) billDetail.get("detailId")).longValue();
            this.id = ((Double) billDetail.get("id")).longValue();
            this.status = ((Double)billDetail.get("status")).intValue();
            this.createTime = ((Double) billDetail.get("createTime")).longValue();
            this.deviceType = type ;
        }

        //  兼容orderList 数据
        public BillListAdaptorWrapper(Order order){
            // 新type中多添加了充值和退款，  所以需要在原有的基础上+2
            this.type = order.getDeviceType() + 2  ;
            this.amount = order.getActualDebit();
//            this.detailId = orderInListDTO.get();
            this.status = order.getBillStatus();
            this.id = order.getId();
            this.createTime = order.getCreateTime();
            this.deviceType = order.getDeviceType();
        }

        public BillListAdaptorWrapper(FundsInListDTO dto) {
            this.type = dto.getOperationType().intValue();
            this.amount = dto.getAmount();
//            this.detailId = dto.getOrderNo();
            this.id = dto.getId();
            this.status = dto.getBillStatus();
            this.createTime = dto.getCreateTime();
            this.deviceType = type ;
        }


    }
}
