package com.xiaolian.amigo.ui.bonus.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 过期红包
 * <p>
 * Created by zcd on 9/18/17.
 */

public class ExpiredBonusAdaptor extends CommonAdapter<BonusAdaptor.BonusWrapper> {
    public ExpiredBonusAdaptor(Context context, int layoutId, List<BonusAdaptor.BonusWrapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, BonusAdaptor.BonusWrapper bonusWrapper, int position) {
        if (bonusWrapper.amount != null) {
            holder.setText(R.id.tv_amount, bonusWrapper.amount.toString());
        }
        if (bonusWrapper.type != null) {
            holder.setText(R.id.tv_type, bonusWrapper.type.toString());
        }
        holder.setText(R.id.tv_time_end, TimeUtils.millis2String(bonusWrapper.getTimeEnd(),
                TimeUtils.MY_DATE_FORMAT2) + "到期");
        holder.setText(R.id.tv_desc, bonusWrapper.desc);
//        if (bonusWrapper.timeLeft != null) {
//            holder.setText(R.id.tv_time_left, String.valueOf(bonusWrapper.timeLeft));
//        }
    }
}
