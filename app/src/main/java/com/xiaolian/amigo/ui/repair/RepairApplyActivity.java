package com.xiaolian.amigo.ui.repair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.repair.RepairProblem;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProblemAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyView;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 报修申请
 * <p>
 * Created by caidong on 2017/9/12.
 */
public class RepairApplyActivity extends RepairBaseActivity implements IRepairApplyView {

    @Inject
    IRepairApplyPresenter<IRepairApplyView> presenter;
    @BindView(R.id.rv_problems)
    RecyclerView rv_problems;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.iv_first)
    ImageView iv_first;
    @BindView(R.id.iv_second)
    ImageView iv_second;
    @BindView(R.id.iv_third)
    ImageView iv_third;
    @BindView(R.id.et_tel)
    TextView et_tel;
    @BindView(R.id.et_content)
    TextView et_content;
    @BindView(R.id.bt_submit)
    Button bt_submit;

    List<RepairProblemAdaptor.ProblemWrapper> problems = new ArrayList<RepairProblemAdaptor.ProblemWrapper>() {
        {
            add(new RepairProblemAdaptor.ProblemWrapper(new RepairProblem(1L, "问题一"), new RepairProblem(2L, "问题二"), new RepairProblem(3L, "问题三")));
            add(new RepairProblemAdaptor.ProblemWrapper(new RepairProblem(4L, "问题无"), new RepairProblem(5L, "问题二"), null));
        }
    };

    RepairProblemAdaptor adapter;
    RecyclerView.LayoutManager manager;

    List<String> images = new ArrayList<>();

    int deviceType;
    long residenceId;
    String location;

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        adapter = new RepairProblemAdaptor(problems);
        rv_problems.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_problems.setLayoutManager(manager);
        rv_problems.setAdapter(adapter);

        adapter.setOnClickListener(v -> toggleBtnStatus());

        render();
        // 获取报修问题列表
        presenter.requestRepairProblems();
    }

    @Override
    protected int setTitle() {
        return R.string.repair_apply;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_repair_apply;
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        if (null != intent) {
            deviceType = intent.getIntExtra(Constant.DEVICE_TYPE, Device.HEARTER.getType());
            residenceId = intent.getLongExtra(Constant.LOCATION_ID, 0L);
            location = intent.getStringExtra(Constant.LOCATION);
        }
    }

    @Override
    public void render() {
        // 填充设备位置
        if (null != location) {
            tv_location.setText(location);
            toggleBtnStatus();
        }
    }

    @OnClick(R.id.rl_device)
    void selectDevice() {
        Map<String, Integer> extraMap = new HashMap<String, Integer>() {
            {
                put(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_DEVICE);
            }
        };
        startActivity(this, ListChooseActivity.class, extraMap);
    }

    @OnClick({R.id.iv_first, R.id.iv_second, R.id.iv_third})
    void chooseImage(ImageView view) {
        switch (view.getId()) {
            case R.id.iv_first: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri).into(iv_first);
                    iv_first.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_second.setVisibility(View.VISIBLE);
                    presenter.onUpload(imageUri);
                });
                break;
            }
            case R.id.iv_second: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri).into(iv_second);
                    iv_second.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_third.setVisibility(View.VISIBLE);
                    presenter.onUpload(imageUri);
                });
                break;
            }
            case R.id.iv_third: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri).into(iv_third);
                    iv_third.setScaleType(ImageView.ScaleType.FIT_XY);
                    presenter.onUpload(imageUri);
                });
                break;
            }
        }
    }

    // 用户键入内容，如联系电话和报修内容
    @OnTextChanged({R.id.et_tel, R.id.et_content})
    void input() {
        toggleBtnStatus();
    }

    @Override
    public void toggleBtnStatus() {
        int selectedProblem = 0;
        if (null != problems) {
            for (RepairProblemAdaptor.ProblemWrapper problem : problems) {
                selectedProblem += problem.getIds().size();
            }
        }
        bt_submit.setEnabled(!TextUtils.isEmpty(et_tel.getText())
                && !TextUtils.isEmpty(et_content.getText()) && !TextUtils.isEmpty(tv_location.getText()) && selectedProblem != 0);
    }

    @Override
    public void backToRepairNav() {
        startActivity(this, RepairNavActivity.class);
    }

    @Override
    public void addImage(String url) {
        this.images.add(url);
    }

    @OnClick(R.id.bt_submit)
    void submit() {
        List<Long> problemIds = new ArrayList<>();
        for (RepairProblemAdaptor.ProblemWrapper wrapper : problems) {
            problemIds.addAll(wrapper.getIds());
        }
        presenter.onSubmit(problemIds, images, et_content.getText().toString(), et_tel.getText().toString(), deviceType, residenceId);
    }

    @Override
    public void refreshProblems(List<RepairProblem> repairProblems) {
        if (null != repairProblems) {
            int size = repairProblems.size();
            int num = size % Constant.COLUMN_NUM == 0 ? size / Constant.COLUMN_NUM : size / Constant.COLUMN_NUM + 1;
            for (int i = 0; i < num; i++) {
                RepairProblem first = null, second = null, third = null;
                if (i * Constant.COLUMN_NUM < size) {
                    first = repairProblems.get(i * Constant.COLUMN_NUM);
                }
                if (i * Constant.COLUMN_NUM + 1 < size) {
                    second = repairProblems.get(i * Constant.COLUMN_NUM);
                }
                if (i * Constant.COLUMN_NUM + 2 < size) {
                    third = repairProblems.get(i * Constant.COLUMN_NUM);
                }
                problems.add(new RepairProblemAdaptor.ProblemWrapper(first, second, third));
            }
        }
    }
}
