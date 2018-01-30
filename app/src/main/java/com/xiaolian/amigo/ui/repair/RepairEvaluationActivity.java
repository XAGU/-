package com.xiaolian.amigo.ui.repair;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.repair.adaptor.RepairEvaluationLabelAdaptor;
import com.xiaolian.amigo.ui.repair.adaptor.RepairEvaluationStarAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairEvaluationPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairEvaluationView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 设备报修评价
 *
 * @author zcd
 * @date 17/9/21
 */

public class RepairEvaluationActivity extends RepairBaseActivity implements IRepairEvaluationView {
    /**
     * 维修id
     */
    public static final String INTENT_KEY_REPAIR_EVALUATION_ID = "intent_key_repair_evaluation_id";
    /**
     * 维修员
     */
    public static final String INTENT_KEY_REPAIR_EVALUATION_REPAIR_MAN_NAME = "intent_key_repair_evaluation_repair_man_name";
    /**
     * 设备位置
     */
    public static final String INTENT_KEY_REPAIR_EVALUATION_DEVICE_LOCATION = "intent_key_repair_evaluation_device_name";
    /**
     * 设备类型
     */
    public static final String INTENT_KEY_REPAIR_EVALUATION_DEVICE_TYPE = "intent_key_repair_evaluation_device_type";
    /**
     * 报修时间
     */
    public static final String INTENT_KEY_REPAIR_EVALUATION_TIME = "intent_key_repair_evaluation_time";

    @Inject
    IRepairEvaluationPresenter<IRepairEvaluationView> presenter;

    @BindView(R.id.rv_evaluations)
    RecyclerView rvEvaluations;

    @BindView(R.id.rv_stars)
    RecyclerView rvStars;

    @BindView(R.id.tv_repair_man)
    TextView tvRepairMan;

    @BindView(R.id.bt_submit)
    Button btSubmit;

    @BindView(R.id.et_content)
    EditText etContent;

    @BindView(R.id.tv_location)
    TextView tvLocation;

    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;

    @BindView(R.id.tv_time)
    TextView tvTime;

    private Long repairId;
    private Integer rating;
    private List<Integer> ratingOptions = new ArrayList<>();

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
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        if (getIntent() != null) {
            repairId = getIntent().getLongExtra(INTENT_KEY_REPAIR_EVALUATION_ID, -1);
            String name = getIntent().getStringExtra(INTENT_KEY_REPAIR_EVALUATION_REPAIR_MAN_NAME);
            if (name != null) {
                tvRepairMan.setText(name);
            }
            String location = getIntent().getStringExtra(INTENT_KEY_REPAIR_EVALUATION_DEVICE_LOCATION);
            if (location != null) {
                tvLocation.setText(location);
            }
            Device device = Device.getDevice(getIntent().getIntExtra(INTENT_KEY_REPAIR_EVALUATION_DEVICE_TYPE, -1));
            if (device != null) {
                tvDeviceType.setText(String.format("%s%s", device.getDesc(), Constant.CHINEASE_COLON));
            }
            String time = getIntent().getStringExtra(INTENT_KEY_REPAIR_EVALUATION_TIME);
            if (time != null) {
                tvTime.setText(time);
            }

        }

        labelAdaptor = new RepairEvaluationLabelAdaptor(this, R.layout.item_evaluation_label, labels);
        rvEvaluations.setLayoutManager(new GridLayoutManager(this, 3));
        rvEvaluations.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        rvEvaluations.setAdapter(labelAdaptor);

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
        rvStars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvStars.addItemDecoration(new GridSpacesItemDecoration(5, ScreenUtils.dpToPxInt(this, 10), false));
        rvStars.setAdapter(starAdaptor);

        starAdaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (RepairEvaluationStarAdaptor.Star s : stars) {
                    s.setStared(false);
                }
                for (int i = 0; i <= position; i++) {
                    stars.get(i).setStared(true);
                }
                rating = position + 1;
                toggleBtnStatus();
                starAdaptor.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected int setTitle() {
        return R.string.repair_evaluation;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_evaluation;
    }

    @Override
    protected void setUp() {
    }

    @OnClick(R.id.bt_submit)
    void evaluation() {
        ratingOptions.clear();
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).isChecked()) {
                ratingOptions.add(i + 1);
            }
        }
        presenter.rateRepair(etContent.getText().toString(), rating, ratingOptions, repairId);
    }

    public void toggleBtnStatus() {
        btSubmit.setEnabled(!TextUtils.isEmpty(etContent.getText())
                && rating != null);
    }

    @OnTextChanged(R.id.et_content)
    void onTextChange() {
        toggleBtnStatus();
    }

    @Override
    public void finishView() {
        EventBus.getDefault().post(RepairEvent.REFRESH_REPAIR_LIST);
        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
