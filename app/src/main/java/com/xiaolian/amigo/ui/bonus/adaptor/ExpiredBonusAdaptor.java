package com.xiaolian.amigo.ui.bonus.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;

/**
 * 过期代金券
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
            holder.setText(R.id.tv_amount, String.format(Locale.getDefault(), "¥%.0f", bonusWrapper.amount));
        }
        if (bonusWrapper.name != null) {
            holder.setText(R.id.tv_type, bonusWrapper.getName());
        }
        if (bonusWrapper.getStartTime() != null && bonusWrapper.getTimeEnd() != null) {
            holder.setText(R.id.tv_time_end, "有效期" + TimeUtils.millis2String(bonusWrapper.getStartTime(),
                    TimeUtils.MY_DATE_FORMAT2) + "至" + TimeUtils.millis2String(bonusWrapper.getTimeEnd(),
                    TimeUtils.MY_DATE_FORMAT2) );
        }
        holder.setText(R.id.tv_desc, bonusWrapper.desc);
    }
}
