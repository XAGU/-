package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.xiaolian.amigo.data.network.model.repair.RepairProblem;
import com.xiaolian.amigo.ui.repair.RepairDetailActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Data;

/**
 * Created by caidong on 2017/9/12.
 */
public class RepairProblemAdaptor extends RecyclerView.Adapter<RepairProblemAdaptor.ViewHolder> {

    private List<ProblemWrapper> problems;
    private Context context;
    private View.OnClickListener listener;

    public RepairProblemAdaptor(List<ProblemWrapper> problems) {
        this.problems = problems;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem, parent, false);
        context = view.getContext();
        return new ViewHolder(view, context, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProblemWrapper problem = problems.get(position);
        holder.problem = problem;
        if (null != problem.first) {
            holder.bt_first.setVisibility(View.VISIBLE);
            holder.bt_first.setText(problem.first.getDescription());
        }
        if (null != problem.second) {
            holder.bt_second.setVisibility(View.VISIBLE);
            holder.bt_second.setText(problem.second.getDescription());
        }
        if (null != problem.third) {
            holder.bt_third.setVisibility(View.VISIBLE);
            holder.bt_third.setText(problem.third.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return null == problems ? 0 : problems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bt_first)
        Button bt_first;
        @BindView(R.id.bt_second)
        Button bt_second;
        @BindView(R.id.bt_third)
        Button bt_third;

        Context context;
        View.OnClickListener listener;
        ProblemWrapper problem;

        public ViewHolder(View itemView, Context context, View.OnClickListener listener) {
            super(itemView);
            this.context = context;
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        // 切换按钮状态
        @OnClick({R.id.bt_first, R.id.bt_second, R.id.bt_third})
        void toggleSelectStatus(Button btn) {
            if (btn.getCurrentTextColor() == ContextCompat.getColor(context, R.color.problem_grey)) {
                btn.setTextColor(ContextCompat.getColor(context, R.color.problem_blue));
                btn.setBackgroundResource(R.drawable.device_problem_blue);
                problem.selectedNum++;
            } else { // btn.getCurrentTextColor() == ContextCompat.getColor(context, R.color.problem_blue
                btn.setTextColor(ContextCompat.getColor(context, R.color.problem_grey));
                btn.setBackgroundResource(R.drawable.device_problem_grey);
                problem.selectedNum--;
            }
            this.listener.onClick(btn);
        }
    }

    @Data
    public static class ProblemWrapper {
        // 问题一
        RepairProblem first;
        // 问题二
        RepairProblem second;
        // 问题三
        RepairProblem third;
        // 图片选中的数量
        int selectedNum = 0;

        public ProblemWrapper(RepairProblem first, RepairProblem second, RepairProblem third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

}
