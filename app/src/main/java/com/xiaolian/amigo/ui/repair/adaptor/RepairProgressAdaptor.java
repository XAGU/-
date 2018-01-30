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
 * 报修进度
 *
 * @author caidong
 * @date 17/9/12
 */
public class RepairProgressAdaptor extends RecyclerView.Adapter<RepairProgressAdaptor.ViewHolder> {
    public interface OnCancelRepairListener {
        /**
         * 取消报修点击事件
         */
        void onCacelRepair();
    }

    private List<ProgressWrapper> progresses;
    private Context context;
    private OnCancelRepairListener listener;

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
        holder.tvDesc.setText(progress.desc);
        holder.tvTime.setText(progress.time);

        RepairStatus status = RepairStatus.getStatus(progress.status);

        holder.tvStatus.setText(status.getDesc());
        // 第一条为报修最新进度，需要用颜色标识
        int colorGreyRes = context.getResources().getColor(R.color.colorTextGrayHeavy);
        if (position == 0) {
            holder.tvStatus.setTextColor(context.getResources().getColor(status.getTextCorlorRes()));
            holder.vDot.setBackgroundResource(status.getDotColorDrawableRes());
        } else {
            holder.tvStatus.setTextColor(colorGreyRes);
            holder.tvTime.setTextColor(colorGreyRes);
            holder.tvDesc.setTextColor(colorGreyRes);
            holder.vDot.setBackgroundResource(R.drawable.dot_circle_grey);
        }
        if (status == RepairStatus.AUDIT_PENDING
                && RepairStatus.getStatus(progresses.get(0).status) == RepairStatus.AUDIT_PENDING) {
            holder.tvCancelRepair.setVisibility(View.VISIBLE);
            holder.tvCancelRepair.setEnabled(true);
            holder.tvCancelRepair.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCacelRepair();
                }
            });
        } else {
            holder.tvCancelRepair.setEnabled(false);
            holder.tvCancelRepair.setVisibility(View.GONE);
        }
    }

    public void setOnCancelRepairListener(OnCancelRepairListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return null == progresses ? 0 : progresses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_dot)
        View vDot;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_cancel_repair)
        TextView tvCancelRepair;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ProgressWrapper {
        /**
         * 进度描述
         */
        String desc;
        /**
         * 时间
         */
        String time;
        /**
         * 进度状态
         */
        Integer status;

        public ProgressWrapper(RepairStep step) {
            this.desc = step.getContent();
            this.time = CommonUtil.stampToDate(step.getTime());
            this.status = step.getStatus();
        }
    }
}
