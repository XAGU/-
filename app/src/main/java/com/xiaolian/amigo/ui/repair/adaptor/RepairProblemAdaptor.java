package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.repair.RepairProblem;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * Created by caidong on 2017/9/12.
 */
public class RepairProblemAdaptor extends CommonAdapter<RepairProblemAdaptor.ProblemWrapper> {

//    private int lastChoosePosition = -1;
    private Context context;

    public RepairProblemAdaptor(Context context, int layoutId, List<ProblemWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, ProblemWrapper problemWrapper, int position) {
        holder.setText(R.id.bt_problem, problemWrapper.getDesc());
        holder.getView(R.id.bt_problem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (lastChoosePosition != -1) {
//                    getDatas().get(lastChoosePosition).setChoose(false);
//                }
                boolean lastChooseStatus = getDatas().get(holder.getAdapterPosition()).isChoose();
                getDatas().get(holder.getAdapterPosition()).setChoose(!lastChooseStatus);
//                lastChoosePosition = position;
                notifyDataSetChanged();
                listener.onItemClick();
            }
        });
        toggleButton(holder.getView(R.id.bt_problem), problemWrapper.isChoose());
    }

    private void toggleButton(Button button, boolean isChoose) {
        if (isChoose) {
            button.setTextColor(ContextCompat.getColor(context, R.color.problem_blue));
            button.setBackgroundResource(R.drawable.device_problem_blue);
        } else {
            button.setTextColor(ContextCompat.getColor(context, R.color.problem_grey));
            button.setBackgroundResource(R.drawable.device_problem_grey);
        }
    }

    @Data
    public static class ProblemWrapper {
        // 问题描述
        String desc;
        // 问题id
        Long id;
        // 是否被选择
        boolean isChoose = false;

        public ProblemWrapper(RepairProblem problem) {
            this.desc = problem.getDescription();
            this.id = problem.getId();
            this.isChoose = false;
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

}

