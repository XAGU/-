package com.xiaolian.amigo.ui.repair;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.repair.adaptor.RepairEvaluationLabelAdaptor;
import com.xiaolian.amigo.ui.repair.adaptor.RepairEvaluationStarAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairEvaluationPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairEvaluationView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设备报修评价
 * <p>
 * Created by zcd on 9/21/17.
 */

public class RepairEvaluationActivity extends RepairBaseActivity implements IRepairEvaluationView {
    public static final String INTENT_KEY_REPAIR_EVALUATION_ID = "intent_key_repair_evaluation_id";
    public static final String INTENT_KEY_REPAIR_EVALUATION_REPAIR_MAN_NAME = "intent_key_repair_evaluation_repair_man_name";

    @Inject
    IRepairEvaluationPresenter<IRepairEvaluationView> presenter;

    @BindView(R.id.rv_evaluations)
    RecyclerView rv_evaluations;

    @BindView(R.id.rv_stars)
    RecyclerView rv_stars;

    @BindView(R.id.tv_repair_man)
    TextView tv_repair_man;

    private Long repairId;

    List<RepairEvaluationLabelAdaptor.Label> labels = new ArrayList<RepairEvaluationLabelAdaptor.Label>() {
        {
            add(new RepairEvaluationLabelAdaptor.Label("主动联系"));
            add(new RepairEvaluationLabelAdaptor.Label("态度很好"));
            add(new RepairEvaluationLabelAdaptor.Label("准时维修"));
        }
    };
    List<RepairEvaluationStarAdaptor.Star> stars = new ArrayList<RepairEvaluationStarAdaptor.Star>() {
        {
            add(new RepairEvaluationStarAdaptor.Star(false));
            add(new RepairEvaluationStarAdaptor.Star(false));
            add(new RepairEvaluationStarAdaptor.Star(false));
            add(new RepairEvaluationStarAdaptor.Star(false));
            add(new RepairEvaluationStarAdaptor.Star(false));
        }
    };
    RepairEvaluationLabelAdaptor labelAdaptor;
    RepairEvaluationStarAdaptor starAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        if (getIntent() != null) {
            repairId = getIntent().getLongExtra(INTENT_KEY_REPAIR_EVALUATION_ID, -1);
            String name = getIntent().getStringExtra(INTENT_KEY_REPAIR_EVALUATION_REPAIR_MAN_NAME);
            if (name != null) {
                tv_repair_man.setText(name);
            }
        }

        labelAdaptor = new RepairEvaluationLabelAdaptor(this, R.layout.item_evaluation_label, labels);
        rv_evaluations.setLayoutManager(new GridLayoutManager(this, 3));
        rv_evaluations.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        rv_evaluations.setAdapter(labelAdaptor);

        labelAdaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (labels.get(position).isChecked()) {
                    labels.get(position).setChecked(false);
                } else {
                    labels.get(position).setChecked(true);
                }
                labelAdaptor.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        starAdaptor = new RepairEvaluationStarAdaptor(this, R.layout.item_evaluation_star, stars);
        rv_stars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_stars.addItemDecoration(new GridSpacesItemDecoration(5, ScreenUtils.dpToPxInt(this, 10), false));
        rv_stars.setAdapter(starAdaptor);

        starAdaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (RepairEvaluationStarAdaptor.Star s : stars) {
                    s.setStared(false);
                }
                for (int i = 0; i <= position; i++) {
                    stars.get(i).setStared(true);
                }
                starAdaptor.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    @Override
    protected void setUp() {

    }

    @Override
    public void finishView() {
        finish();
    }
}
