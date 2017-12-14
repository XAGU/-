package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.RechargeStatus;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;
import com.xiaolian.amigo.data.enumeration.WithdrawalStatus;
import com.xiaolian.amigo.data.network.model.funds.FundsInListDTO;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 充值提现记录
 * <p>
 * Created by zcd on 10/18/17.
 */

public class WithdrawalAdaptor extends CommonAdapter<WithdrawalAdaptor.WithdrawalWrapper> {
    private Context context;
    public WithdrawalAdaptor(Context context, int layoutId, List<WithdrawalWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, WithdrawalWrapper withdrawalWrapper, int position) {
        holder.setText(R.id.tv_withdrawal, withdrawalWrapper.getTitle());
        if (withdrawalWrapper.getType() == WithdrawOperationType.WITHDRAW.getType()) {
            holder.setText(R.id.tv_withdrawal_status,
                    WithdrawalStatus.getWithdrawalStatus(withdrawalWrapper.getStatus()).getDesc());
            holder.setTextColor(R.id.tv_withdrawal_status,
                    ContextCompat.getColor(context,
                            WithdrawalStatus.getWithdrawalStatus(withdrawalWrapper.getStatus()).getColorRes()));
        } else {
            holder.setText(R.id.tv_withdrawal_status,
                    RechargeStatus.getRechargeStatus(withdrawalWrapper.getStatus()).getDesc());
            holder.setTextColor(R.id.tv_withdrawal_status,
                    ContextCompat.getColor(context,
                            RechargeStatus.getRechargeStatus(withdrawalWrapper.getStatus()).getColorRes()));
        }
        holder.setText(R.id.tv_withdrawal_time, TimeUtils.millis2String(withdrawalWrapper.getTime()));
    }

    @Data
    public static class WithdrawalWrapper {
        private Long time;
        private String title;
        private Integer type;
        private int status;
        private Long id;

        public WithdrawalWrapper(FundsInListDTO dto) {
            this.id = dto.getId();
            this.time = dto.getCreateTime();
            this.type = dto.getOperationType();
            this.title = WithdrawOperationType.getOperationType(dto.getOperationType()).getDesc()
                            + "：¥" + dto.getAmount();
            this.status = dto.getStatus();
        }
    }
}
