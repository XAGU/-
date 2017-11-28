package com.xiaolian.amigo.ui.repair;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.repair.RepairProblem;
import com.xiaolian.amigo.ui.repair.adaptor.ImageAddAdapter;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProblemAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

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

    private static final int REQUEST_IMAGE = 0x3301;
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

    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    @BindView(R.id.rl_main)
    RelativeLayout rl_main;

    @BindView(R.id.ll_problems)
    LinearLayout ll_problems;
    @BindView(R.id.v_divide)
    View v_divide;

    @BindView(R.id.rv_image)
    RecyclerView rv_image;
    private ImageAddAdapter imageAddAdapter;
    List<ImageAddAdapter.ImageItem> addImages = new ArrayList<>();

    List<RepairProblemAdaptor.ProblemWrapper> problems = new ArrayList<>();

    RepairProblemAdaptor adapter;

    List<String> images = new ArrayList<>();

    int deviceType = Device.UNKNOWN.getType();
    long residenceId;
    String location;
    private boolean allValidated = false;
    private List<TextView> viewList;
    private int selectedProblem;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            deviceType = intent.getIntExtra(Constant.DEVICE_TYPE, Device.UNKNOWN.getType());
            residenceId = intent.getLongExtra(Constant.LOCATION_ID, 0L);
            location = intent.getStringExtra(Constant.LOCATION);
            tv_location.setText(location);
            if (Device.UNKNOWN.getType() != deviceType) {
                // 获取报修问题列表
                presenter.requestRepairProblems(deviceType);
            }
        }
    }

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        tv_location.requestFocus();

        viewList = new ArrayList<TextView>(){
            {
                add(et_tel);
                add(et_content);
            }
        };

        adapter = new RepairProblemAdaptor(this, R.layout.item_problem, problems);
        adapter.setOnItemClickListener(this::toggleBtnStatus);
        rv_problems.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        rv_problems.setLayoutManager(new GridLayoutManager(this, 3));
        rv_problems.setAdapter(adapter);
        render();
        initImageAdd();
    }

    private void initImageAdd() {
        addImages.add(new ImageAddAdapter.ImageItem());
        imageAddAdapter = new ImageAddAdapter(this, R.layout.item_image_add, addImages);
        imageAddAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (images.isEmpty() || (images.size() < 3 && position == images.size())) {
                    getImage(imageUri -> {
                        presenter.onUpload(RepairApplyActivity.this, imageUri, position);
                    });
                } else {
                    Intent intent = new Intent(RepairApplyActivity.this, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.INTENT_POSITION, position);
                    intent.putExtra(AlbumItemActivity.EXTRA_TYPE_SINGLE, images.get(position));
                    intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_DELETEABLE);
                    startActivityForResult(intent, REQUEST_IMAGE);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rv_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_image.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        rv_image.setAdapter(imageAddAdapter);
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
            deviceType = intent.getIntExtra(Constant.DEVICE_TYPE, Device.UNKNOWN.getType());
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
        if (!TextUtils.isEmpty(presenter.getMobile())) {
            et_tel.setText(presenter.getMobile());
        }
        if (Device.UNKNOWN.getType() != deviceType) {
            // 获取报修问题列表
            presenter.requestRepairProblems(deviceType);
        }
    }

    @OnClick(R.id.rl_device)
    void selectDevice() {
//        Map<String, Integer> extraMap = new HashMap<String, Integer>() {
//            {
//                put(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_DEVICE);
//            }
//        };
//        startActivity(this, ListChooseActivity.class, extraMap);
        startActivity(this, ChooseRepairActivity.class);
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
                    presenter.onUpload(RepairApplyActivity.this, imageUri, 0);
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
                    presenter.onUpload(RepairApplyActivity.this, imageUri, 1);
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
                    presenter.onUpload(RepairApplyActivity.this, imageUri, 2);
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

        allValidated = !TextUtils.isEmpty(location)
//                && selectedProblem > 0 // 不校验常见问题
                && !TextUtils.isEmpty(et_tel.getText())
                && !TextUtils.isEmpty(et_content.getText());
        bt_submit.setBackgroundResource(allValidated ?
                R.drawable.button_enable : R.drawable.button_disable);
    }

    @Override
    public void backToRepairNav() {
        presenter.setLastRepairTime(System.currentTimeMillis());
        startActivity(this, RepairNavActivity.class);
    }

    @Override
    public void addImage(String url, int position) {
        if (this.images.size() > position) {
            this.images.remove(position);
            this.images.add(position, url);
        } else {
            this.images.add(url);
        }
        refreshAddImage();
    }

    private void refreshAddImage() {
        addImages.clear();
        for (String image : images) {
            addImages.add(new ImageAddAdapter.ImageItem(image));
        }
        if (images.size() < 3) {
            addImages.add(new ImageAddAdapter.ImageItem());
        }
        imageAddAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_submit)
    void submit() {
        if (!allValidated) {
            if (TextUtils.isEmpty(location)) {
                onError(getString(R.string.please_choose_device_info));
                return;
            }
            if (selectedProblem == 0) {
                onError(getString(R.string.please_choose_device_problem));
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
        if (repairProblems == null || repairProblems.isEmpty()) {
            return;
        }
        renderProblems();
        problems.clear();
        List<RepairProblemAdaptor.ProblemWrapper> problemWrappers = new ArrayList<>();
        for (RepairProblem problem : repairProblems) {
            problemWrappers.add(new RepairProblemAdaptor.ProblemWrapper(problem));
        }
        problems.addAll(problemWrappers);
        adapter.notifyDataSetChanged();
    }

    private void renderProblems() {
        v_divide.setVisibility(View.VISIBLE);
        ll_problems.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            int position = data.getIntExtra(AlbumItemActivity.INTENT_POSITION, -1);
            if (position != -1) {
                images.remove(position);
                refreshAddImage();
            }
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}