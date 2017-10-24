package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;
import com.xiaolian.amigo.data.enumeration.WithdrawalStatus;
import com.xiaolian.amigo.data.network.model.dto.response.FundsInListDTO;
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
    public WithdrawalAdaptor(Context context, int layoutId, List<WithdrawalWrapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, WithdrawalWrapper withdrawalWrapper, int position) {
        holder.setText(R.id.tv_withdrawal, withdrawalWrapper.getTitle());
        holder.setText(R.id.tv_withdrawal_status, WithdrawalStatus.getWithdrawalStatus(withdrawalWrapper.getStatus()).getDesc());
        holder.setText(R.id.tv_withdrawal_time, TimeUtils.millis2String(withdrawalWrapper.getTime()));
    }

    @Data
    public static class WithdrawalWrapper {
        private Long time;
        private String title;
        private int status;

        public WithdrawalWrapper(FundsInListDTO dto) {
            this.time = dto.getCreateTime();
            this.title = WithdrawOperationType.getOperationType(dto.getOperationType()).getDesc()
                            + "：￥" + dto.getAmount();
            this.status = dto.getStatus();
        }
    }
}
