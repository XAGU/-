package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.RechargeStatus;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;
import com.xiaolian.amigo.data.enumeration.WithdrawalStatus;
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

    private Context context;

    public BillListAdaptor(Context context, int layoutId, List<BillListAdaptor.BillListAdaptorWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, BillListAdaptor.BillListAdaptorWrapper billListAdaptorWrapper, int position) {
        //设置type类型
        holder.setText(R.id.tv_bill_type, getTypeName(billListAdaptorWrapper.getType(), billListAdaptorWrapper.getStatus()));
        //设置时间
        holder.setText(R.id.tv_bill_time, TimeUtils.millis2String(billListAdaptorWrapper.getCreateTime()));

        holder.setText(R.id.tv_bill_money, billListAdaptorWrapper.getAmount());
        if (billListAdaptorWrapper.getType() == WithdrawOperationType.WITHDRAW.getType()) {
            holder.setText(R.id.tv_bill_status,
                    WithdrawalStatus.getWithdrawalStatus(billListAdaptorWrapper.getStatus()).getDesc());
            holder.setTextColor(R.id.tv_bill_status,
                    ContextCompat.getColor(context,
                            WithdrawalStatus.getWithdrawalStatus(billListAdaptorWrapper.getStatus()).getColorRes()));
        } else {
            holder.setText(R.id.tv_bill_status,
                    RechargeStatus.getRechargeStatus(billListAdaptorWrapper.getStatus()).getDesc());
            holder.setTextColor(R.id.tv_bill_status,
                    ContextCompat.getColor(context,
                            RechargeStatus.getRechargeStatus(billListAdaptorWrapper.getStatus()).getColorRes()));
        }
        holder.setText(R.id.tv_bill_type, String.valueOf(billListAdaptorWrapper.getType()));
//        R.id.tv_bill_Status_only
    }

    private String getTypeName(Integer billType, Integer status) {
        if (billType == XLFilterContentViewBillTypeRecharge) {
            return "充值";
        } else if (billType == XLFilterContentViewBillTypeWithdraw) {
            if (status == 304) {
                return "退款申请";
            }
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

        public BillListAdaptorWrapper(HashMap<String, Object> billDic) {
            this.type = ((Double)billDic.get("type")).intValue();
            Map<String, Object> billDetail = (Map<String, Object>) billDic.get("billDetail");
            this.amount = (String) billDetail.get("amount");
            this.detailId = ((Double) billDetail.get("detailId")).longValue();
            this.id = ((Double) billDetail.get("id")).longValue();
            this.status = ((Double)billDetail.get("status")).intValue();
            this.createTime = ((Double) billDetail.get("createTime")).longValue();
        }
    }
}
