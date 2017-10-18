package com.xiaolian.amigo.ui.repair;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.repair.RepairProblem;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProblemAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyView;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
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

    List<RepairProblemAdaptor.ProblemWrapper> problems = new ArrayList<>();

    RepairProblemAdaptor adapter;

    List<String> images = new ArrayList<>();

    int deviceType;
    long residenceId;
    String location;
    private boolean allValidated = false;
    private List<TextView> viewList;
    private int selectedProblem;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            deviceType = intent.getIntExtra(Constant.DEVICE_TYPE, Device.HEATER.getType());
            residenceId = intent.getLongExtra(Constant.LOCATION_ID, 0L);
            location = intent.getStringExtra(Constant.LOCATION);
            tv_location.setText(location);
        }
    }

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        viewList = new ArrayList<TextView>(){
            {
                add(et_tel);
                add(et_content);
            }
        };

        adapter = new RepairProblemAdaptor(this, R.layout.item_problem, problems);
        adapter.setOnItemClickListener(new RepairProblemAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick() {
                toggleBtnStatus();
            }
        });
        rv_problems.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        rv_problems.setLayoutManager(new GridLayoutManager(this, 3));
        rv_problems.setAdapter(adapter);
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
            deviceType = intent.getIntExtra(Constant.DEVICE_TYPE, Device.HEATER.getType());
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
                    Glide.with(this).load(imageUri)
                            .asBitmap()
                            .placeholder(R.drawable.ic_picture_error)
                            .error(R.drawable.ic_picture_error)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_first);
                    iv_first.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_second.setVisibility(View.VISIBLE);
                    presenter.onUpload(imageUri);
                });
                break;
            }
            case R.id.iv_second: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri)
                            .asBitmap()
                            .placeholder(R.drawable.ic_picture_error)
                            .error(R.drawable.ic_picture_error)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_second);
                    iv_second.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_third.setVisibility(View.VISIBLE);
                    presenter.onUpload(imageUri);
                });
                break;
            }
            case R.id.iv_third: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri)
                            .asBitmap()
                            .placeholder(R.drawable.ic_picture_error)
                            .error(R.drawable.ic_picture_error)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_third);
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
        selectedProblem = 0;
        if (null != problems) {
            for (RepairProblemAdaptor.ProblemWrapper problem : problems) {
                selectedProblem += (problem.isChoose() ? 1 : 0);
            }
        }
//        bt_submit.setEnabled(!TextUtils.isEmpty(et_tel.getText())
//                && !TextUtils.isEmpty(et_content.getText())
//                && !TextUtils.isEmpty(tv_location.getText())
//                && selectedProblem != 0);

        allValidated = !TextUtils.isEmpty(location)
                && selectedProblem > 0
                && !TextUtils.isEmpty(et_tel.getText())
                && !TextUtils.isEmpty(et_content.getText());
        bt_submit.setBackgroundResource(allValidated ?
                R.drawable.button_enable : R.drawable.button_disable);
    }

    @Override
    public void backToRepairNav() {
        startActivity(this, RepairNavActivity.class);
    }

    @Override
    public void addImage(String url) {
        this.images.add(Constant.IMAGE_PREFIX + url);
    }

    @OnClick(R.id.bt_submit)
    void submit() {
        if (!allValidated) {
            if (TextUtils.isEmpty(location)) {
                onError("请选择设备信息");
                return;
            }
            if (selectedProblem == 0) {
                onError("请选择设备问题");
                return;
            }
            for (TextView view : viewList) {
                if (TextUtils.isEmpty(view.getText())) {
                    onError(view.getHint().toString());
                    return;
                }
            }
        }
        List<Long> problemIds = new ArrayList<>();
        for (RepairProblemAdaptor.ProblemWrapper wrapper : problems) {
            if (wrapper.isChoose()) {
                problemIds.add(wrapper.getId());
            }
        }
        presenter.onSubmit(problemIds, images, et_content.getText().toString(), et_tel.getText().toString(), deviceType, residenceId);
    }

    @Override
    public void refreshProblems(List<RepairProblem> repairProblems) {
        if (null != repairProblems) {
            List<RepairProblemAdaptor.ProblemWrapper> problemWrappers = new ArrayList<>();
            for (RepairProblem problem : repairProblems) {
                problemWrappers.add(new RepairProblemAdaptor.ProblemWrapper(problem));
            }
            problems.addAll(problemWrappers);
            adapter.notifyDataSetChanged();
        }
    }
}