package com.xiaolian.amigo.ui.repair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.EvaluateStatus;
import com.xiaolian.amigo.data.enumeration.RepairStatus;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProgressAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.ui.widget.dialog.IOSAlertDialog;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 报修详情
 *
 * @author caidong
 * @date 17/9/18
 */
public class RepairDetailActivity extends RepairBaseActivity implements IRepairDetailView {
    private static final int FIRST_IMAGE_INDEX = 0;
    private static final int SECOND_IMAGE_INDEX = 1;
    private static final int THIRD_IMAGE_INDEX = 2;

    @Inject
    IRepairDetailPresenter<IRepairDetailView> presenter;
    @BindView(R.id.rv_repair_progresses)
    RecyclerView rvRepairProgresses;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_images)
    LinearLayout llImages;
    @BindView(R.id.iv_first)
    ImageView ivFirst;
    @BindView(R.id.iv_second)
    ImageView ivSecond;
    @BindView(R.id.iv_third)
    ImageView ivThird;
    @BindView(R.id.left_oper)
    TextView leftOper;
    @BindView(R.id.right_oper)
    TextView rightOper;
    @BindView(R.id.ll_extra)
    LinearLayout llExtra;
    @BindView(R.id.tv_extra_title)
    TextView tvExtraTitle;
    @BindView(R.id.tv_extra_content1)
    TextView tvExtraContent1;
    @BindView(R.id.tv_extra_content2)
    TextView tvExtraContent2;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    List<RepairProgressAdaptor.ProgressWrapper> progresses = new ArrayList<>();
    Long detailId;

    RepairProgressAdaptor adapter;
    RecyclerView.LayoutManager manager;

    private ArrayList<String> images = new ArrayList<>();

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        adapter = new RepairProgressAdaptor(progresses);
        adapter.setOnCancelRepairListener(() ->
                new IOSAlertDialog(this).builder()
                .setMsg("确认取消报修？")
                .setPositiveButton("确认", v -> presenter.cancelRepair())
                .setNegativeClickListener("取消", IOSAlertDialog::dismiss).show());
        rvRepairProgresses.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST, "deductLast"));
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRepairProgresses.setLayoutManager(manager);
        rvRepairProgresses.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.requestRepailDetail(detailId);
    }

    @Override
    protected int setTitle() {
        return R.string.repair_detail;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_repair_detail;
    }

    @Override
    public void addMoreProgresses(List<RepairProgressAdaptor.ProgressWrapper> progresses) {
        Collections.reverse(progresses);
        this.progresses.clear();
        this.progresses.addAll(progresses);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void render(RepairDetailRespDTO detail) {
        llBottom.setVisibility(View.VISIBLE);
        images.clear();
        tvType.setText(Device.getDevice(detail.getDeviceType()).getDesc());
        tvLocation.setText(detail.getLocation());
        tvContent.setText(detail.getContent());
        int imageSize = ScreenUtils.dpToPxInt(this, 57);

        List<String> images = detail.getImages();
        if (null != images) {
            this.images.addAll(images);
            // 获取图片数量
            int num = images.size();
            RequestManager manager = Glide.with(this);
            // 渲染第一张图
            if (num > FIRST_IMAGE_INDEX) {
                llImages.setVisibility(View.VISIBLE);
                ivFirst.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(0)
                        + String.format(Locale.getDefault(), Constant.OSS_IMAGE_RESIZE,
                        imageSize))
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(ivFirst);
            }
            // 渲染第二张图
            if (num > SECOND_IMAGE_INDEX) {
                ivSecond.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(1)
                        + String.format(Locale.getDefault(), Constant.OSS_IMAGE_RESIZE,
                        imageSize))
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(ivSecond);
            }
            // 渲染第三张图
            if (num > THIRD_IMAGE_INDEX) {
                ivThird.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(2)
                        + String.format(Locale.getDefault(), Constant.OSS_IMAGE_RESIZE,
                        imageSize))
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(ivThird);
            }
        }

        // 设置操作按钮中的显示文案
        RepairStatus status = RepairStatus.getStatus(detail.getSteps().get(detail.getSteps().size() - 1).getStatus());
        if (status == RepairStatus.REPAIR_CANCEL) {
            llBottom.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.VISIBLE);
        }
        String[] opers = status.getNextOperations();
        leftOper.setText(opers[0]);
        rightOper.setText(opers[1]);
        leftOper.setOnClickListener(v -> {
            switch (status) {
                case REPAIR_DONE:
                    Intent intent = new Intent(RepairDetailActivity.this, RepairEvaluationActivity.class);
                    intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_ID, detail.getId());
                    intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_REPAIR_MAN_NAME, detail.getRepairmanName());
                    intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_DEVICE_LOCATION, detail.getLocation());
                    intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_DEVICE_TYPE, detail.getDeviceType());
                    intent.putExtra(RepairEvaluationActivity.INTENT_KEY_REPAIR_EVALUATION_TIME, CommonUtil.stampToDate(detail.getSteps().get(0).getTime()));
                    startActivity(intent);
                    break;
                case AUDIT_FAIL:
                    startActivity(new Intent(RepairDetailActivity.this, WebActivity.class)
                            .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
                    break;
                case AUDIT_PENDING:
                case REPAIR_PENDING:
                case REPAIRING:
                    presenter.remind(detail.getId());
                    break;
                default:
                    break;
            }
        });
        rightOper.setOnClickListener(v -> {
            switch (status) {
                case REPAIR_DONE:
                case REPAIR_PENDING:
                case REPAIRING:
                case AUDIT_PENDING:
                    startActivity(new Intent(RepairDetailActivity.this, WebActivity.class)
                            .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
                    break;
                case AUDIT_FAIL:
                    CommonUtil.call(RepairDetailActivity.this, detail.getCsMobile());
                    break;
                default:
                    break;
            }
        });
        if (EvaluateStatus.getStatus(detail.getRated()) == EvaluateStatus.EVALUATE_DONE) {
            leftOper.setEnabled(false);
            leftOper.setTextColor(ContextCompat.getColor(this, R.color.colorDark9));
            llExtra.setVisibility(View.VISIBLE);
            tvExtraTitle.setText(getString(R.string.evaluation_info));
            tvExtraContent1.setVisibility(View.VISIBLE);
            tvExtraContent1.setText(getString(R.string.score, detail.getScore()));
            tvExtraContent2.setText(detail.getComment());
        }
        if (status == RepairStatus.AUDIT_FAIL) {
            llExtra.setVisibility(View.VISIBLE);
            tvExtraTitle.setText(getString(R.string.customer_response));
            tvExtraContent2.setText(detail.getReply());
        }
    }

    @Override
    public void backToList() {
        EventBus.getDefault().post(RepairEvent.REFRESH_REPAIR_LIST);
        super.onBackPressed();
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.EXTRA_KEY);
        detailId = bundle.getLong(Constant.BUNDLE_ID);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        presenter.clearObservers();
        progresses.clear();
        super.onDestroy();
    }

    @OnClick(R.id.iv_first)
    void onFirstImageClick() {
        if (images != null) {
            Intent intent = new Intent(this, AlbumItemActivity.class);
            intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, getResources().getInteger(R.integer.first));
            intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
            startActivity(intent);
        }
    }

    @OnClick(R.id.iv_second)
    void onSecondImageClick() {
        if (images != null) {
            Intent intent = new Intent(this, AlbumItemActivity.class);
            intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, getResources().getInteger(R.integer.second));
            intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
            startActivity(intent);
        }
    }

    @OnClick(R.id.iv_third)
    void onThirdImageClick() {
        if (images != null) {
            Intent intent = new Intent(this, AlbumItemActivity.class);
            intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, getResources().getInteger(R.integer.third));
            intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
            startActivity(intent);
        }
    }

}
