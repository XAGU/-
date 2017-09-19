package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.RepairStatus;
import com.xiaolian.amigo.data.network.model.repair.RepairStep;
import com.xiaolian.amigo.util.CommonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/12.
 */
public class RepairProgressAdaptor extends RecyclerView.Adapter<RepairProgressAdaptor.ViewHolder> {

    private List<ProgressWrapper> progresses;
    private Context context;

    public RepairProgressAdaptor(List<ProgressWrapper> progresses) {
        this.progresses = progresses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair_progress, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProgressWrapper progress = progresses.get(position);
        holder.tv_desc.setText(progress.desc);
        holder.tv_time.setText(progress.time);

        RepairStatus status = RepairStatus.getStatus(progress.status);

        holder.tv_status.setText(status.getDesc());
        // 第一条为报修最新进度，需要用颜色标识
        int colorGreyRes = context.getResources().getColor(R.color.colorTextGrayHeavy);
        if (position == 0) {
            holder.tv_status.setTextColor(context.getResources().getColor(status.getTextCorlorRes()));
            holder.v_dot.setBackgroundResource(status.getDotColorDrawableRes());
        } else {
            holder.tv_status.setTextColor(colorGreyRes);
            holder.tv_time.setTextColor(colorGreyRes);
            holder.tv_desc.setTextColor(colorGreyRes);
            holder.v_dot.setBackgroundResource(R.drawable.dot_circle_grey);
        }
    }

    @Override
    public int getItemCount() {
        return null == progresses ? 0 : progresses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_dot)
        View v_dot;
        @BindView(R.id.tv_status)
        TextView tv_status;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_desc)
        TextView tv_desc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ProgressWrapper {
        // 进度描述
        String desc;
        // 时间
        String time;
        // 进度状态
        Integer status;

        public ProgressWrapper(RepairStep step) {
            this.desc = step.getContent();
            this.time = CommonUtil.stampToDate(step.getTime());
            this.status = step.getStatus();
        }
    }
}
