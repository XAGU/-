package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.EvaluateStatus;
import com.xiaolian.amigo.data.enumeration.RepairStatus;
import com.xiaolian.amigo.data.network.model.repair.Repair;
import com.xiaolian.amigo.ui.repair.RepairDetailActivity;
import com.xiaolian.amigo.ui.repair.RepairEvaluationActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 报修列表adpter
 *
 * @author caidong
 * @date 17/9/12
 */
public class RepairAdaptor extends RecyclerView.Adapter<RepairAdaptor.ViewHolder> {

    private List<RepairWrapper> repairs;
    private Context context;

    public RepairAdaptor(List<RepairWrapper> repairs) {
        this.repairs = repairs;
    }

    public void updateData(List<RepairWrapper> repairs) {
        this.repairs = repairs;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RepairWrapper wrapper = repairs.get(position);
        holder.tvDevice.setText(wrapper.device);
        holder.tvTime.setText(wrapper.time);
        holder.tvStatus.setText(RepairStatus.getStatus(wrapper.status).getDesc());
        holder.tvStatus.setTextColor(context.getResources().getColor(RepairStatus.getStatus(wrapper.status).getTextCorlorRes()));

        if (RepairStatus.getStatus(wrapper.status) == RepairStatus.REPAIR_DONE) {
            // 状态为维修完成成需要呈现评价按钮
            EvaluateStatus evaluate;
            if (wrapper.rated != null) {
                evaluate = EvaluateStatus.getStatus(wrapper.rated);
            } else {
                evaluate = EvaluateStatus.EVALUATE_PENDING;
            }
            if (evaluate == null) {
                holder.btEvaluate.setVisibility(View.GONE);
                return;
            }
            if (evaluate == EvaluateStatus.EVALUATE_DONE) {
                holder.btEvaluate.setEnabled(false);
            } else {
                holder.btEvaluate.setEnabled(true);
            }
            holder.btEvaluate.setText(evaluate.getDesc());
            holder.btEvaluate.setBackgroundResource(evaluate.getBackGroundRes());
            holder.btEvaluate.setTextColor(ContextCompat.getColor(context, evaluate.getTextColor()));
            holder.btEvaluate.setVisibility(View.VISIBLE);
            holder.btEvaluate.setOnClickListener(v -> {
                Intent intent = new Intent(context, RepairEvaluationActivity.class);
                intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_ID, wrapper.id);
                intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_REPAIR_MAN_NAME, wrapper.repairmanName);
                intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_DEVICE_LOCATION, wrapper.location);
                intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_DEVICE_TYPE, wrapper.deviceType);
                intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_TIME, wrapper.time);
                intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_CREDITS, wrapper.credits);
                context.startActivity(intent);
            });
        } else {
            holder.btEvaluate.setVisibility(View.GONE);
        }


        holder.detailId = wrapper.id;
    }

    @Override
    public int getItemCount() {
        return null == repairs ? 0 : repairs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_device)
        TextView tvDevice;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.bt_evaluate)
        Button btEvaluate;

        Context context;
        /**
         * 报修单id
         */
        Long detailId;

        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RepairDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(Constant.BUNDLE_ID, detailId);
            intent.putExtra(Constant.EXTRA_KEY, bundle);
            context.startActivity(intent);
        }
    }

    public static class RepairWrapper {
        /**
         * 设备
         */
        String device;
        /**
         * 时间
         */
        String time;
        /**
         * 维修状态
         */
        Integer status;
        /**
         * 评价状态
         */
        Integer rated;
        //
        /**
         * 维修人员名称
         */
        String repairmanName;
        /**
         * 维修人员Id
         */
        Long repairmanId;
        /**
         * 报修单id
         */
        Long id;
        /**
         * 设备类型
         */
        Integer deviceType;
        /**
         * 设备位置
         */
        String location;

        /**
         *
         * @param 评价积分
         */
        Integer credits;

        public RepairWrapper(Repair repair) {
            String location = repair.getLocation() == null ? "未知位置" : repair.getLocation();
            this.device = Device.getDevice(repair.getDeviceType()).getDesc() + Constant.CHINEASE_COLON + location;
            this.time = CommonUtil.stampToDate(repair.getCreateTime());
            // 防止status为null 造成app崩溃
            if (repair.getStatus() == null) {
                this.status = RepairStatus.UNKNOWN.getType();
            } else {
                this.status = repair.getStatus();
            }
            this.rated = repair.getRated();
            this.repairmanId = repair.getRepairmanId();
            this.repairmanName = repair.getRepairmanName();
            this.id = repair.getId();
            this.deviceType = repair.getDeviceType();
            this.location = repair.getLocation();
            this.credits = repair.getCredits();
        }
    }
}
