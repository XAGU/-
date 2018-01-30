package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 报修评价标签
 *
 * @author zcd
 * @date 17/9/21
 */

public class RepairEvaluationLabelAdaptor extends CommonAdapter<RepairEvaluationLabelAdaptor.Label> {
    private Context context;

    public RepairEvaluationLabelAdaptor(Context context, int layoutId, List<Label> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, Label label, int position) {
        if (label.isChecked()) {
            holder.getView(R.id.tv_label).setBackgroundResource(R.drawable.bg_rect_blue_stroke);
            ((TextView) holder.getView(R.id.tv_label)).setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
        } else {
            holder.getView(R.id.tv_label).setBackgroundResource(R.drawable.bg_rect_gray_stroke);
            ((TextView) holder.getView(R.id.tv_label)).setTextColor(ContextCompat.getColor(context, R.color.colorTextGray));
        }
        ((TextView) holder.getView(R.id.tv_label)).setText(label.getContent());
    }

    @Data
    public static class Label {
        private String content;
        private boolean isChecked;

        public Label(String content) {
            this.content = content;
            this.isChecked = false;
        }
    }
}
