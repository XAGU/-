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
import com.xiaolian.amigo.ui.repair.RepairDetailActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by caidong on 2017/9/12.
 */
public class RepairProblemAdaptor extends RecyclerView.Adapter<RepairProblemAdaptor.ViewHolder> {

    private List<ProblemWrapper> problems;
    private Context context;

    public RepairProblemAdaptor(List<ProblemWrapper> problems) {
        this.problems = problems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem, parent, false);
        context = view.getContext();
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProblemWrapper problem = problems.get(position);
        if (!TextUtils.isEmpty(problem.first)) {
            holder.bt_first.setVisibility(View.VISIBLE);
            holder.bt_first.setText(problem.first);
        }
        if (!TextUtils.isEmpty(problem.second)) {
            holder.bt_second.setVisibility(View.VISIBLE);
            holder.bt_second.setText(problem.second);
        }
        if (!TextUtils.isEmpty(problem.third)) {
            holder.bt_third.setVisibility(View.VISIBLE);
            holder.bt_third.setText(problem.third);
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

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        // 切换按钮状态
        @OnClick({R.id.bt_first, R.id.bt_second, R.id.bt_third})
        void toggleSelectStatus(Button btn) {
            if(btn.getCurrentTextColor() == ContextCompat.getColor(context, R.color.problem_grey)) {
                btn.setTextColor(ContextCompat.getColor(context, R.color.problem_blue));
                btn.setBackgroundResource(R.drawable.device_problem_blue);
            } else { // btn.getCurrentTextColor() == ContextCompat.getColor(context, R.color.problem_blue
                btn.setTextColor(ContextCompat.getColor(context, R.color.problem_grey));
                btn.setBackgroundResource(R.drawable.device_problem_grey);
            }
        }
    }

    public static class ProblemWrapper {
        // 问题一
        String first;
        // 问题二
        String second;
        // 问题三
        String third;

        public ProblemWrapper(String first, String second, String third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }
}
