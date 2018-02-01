package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 报修评价星星
 *
 * @author zcd
 * @date 17/9/21
 */

public class RepairEvaluationStarAdaptor extends CommonAdapter<RepairEvaluationStarAdaptor.Star> {
    public RepairEvaluationStarAdaptor(Context context, int layoutId, List<Star> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Star star, int position) {
        if (star.isStared()) {
            holder.setImageResource(R.id.iv_star, R.drawable.ic_stared);
        } else {
            holder.setImageResource(R.id.iv_star, R.drawable.ic_unstar);
        }
    }

    @Data
    public static class Star {
        private boolean isStared;

        public Star(boolean isStared) {
            this.isStared = isStared;
        }
    }
}
