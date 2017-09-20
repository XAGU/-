package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.repair.RepairProblem;

import java.util.ArrayList;
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
            List<Long> ids = problem.ids;
            if (btn.getCurrentTextColor() == ContextCompat.getColor(context, R.color.problem_grey)) {
                btn.setTextColor(ContextCompat.getColor(context, R.color.problem_blue));
                btn.setBackgroundResource(R.drawable.device_problem_blue);

                switch (btn.getId()) {
                    case R.id.bt_first:
                        ids.add(problem.first.getId());
                        break;
                    case R.id.bt_second:
                        ids.add(problem.second.getId());
                        break;
                    case R.id.bt_third:
                        ids.add(problem.third.getId());
                        break;
                }
                this.listener.onClick(btn);
            } else { // btn.getCurrentTextColor() == ContextCompat.getColor(context, R.color.problem_blue
                btn.setTextColor(ContextCompat.getColor(context, R.color.problem_grey));
                btn.setBackgroundResource(R.drawable.device_problem_grey);

                switch (btn.getId()) {
                    case R.id.bt_first:
                        ids.remove(problem.first.getId());
                        break;
                    case R.id.bt_second:
                        ids.remove(problem.second.getId());
                        break;
                    case R.id.bt_third:
                        ids.remove(problem.third.getId());
                        break;
                }
                this.listener.onClick(btn);
            }
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
        // 存放选中的问题描述id列表
        List<Long> ids = new ArrayList<>();

        public ProblemWrapper(RepairProblem first, RepairProblem second, RepairProblem third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

}

