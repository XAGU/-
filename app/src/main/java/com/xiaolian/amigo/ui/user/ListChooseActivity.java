package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.IntentAction;
import com.xiaolian.amigo.data.enumeration.WithdrawWay;
import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.repair.RepairApplyActivity;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.user.intf.IListChoosePresenter;
import com.xiaolian.amigo.ui.user.intf.IListChooseView;
import com.xiaolian.amigo.ui.wallet.WithdrawalActivity;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 列表选择页面
 *
 * @author zcd
 * @date 17/9/15
 */

public class ListChooseActivity extends BaseActivity implements IListChooseView {
    public static final String INTENT_KEY_LIST_CHOOSE_CONTENT = "intent_key_list_choose_content";
    public static final String INTENT_KEY_LIST_CHOOSE_ITEMS = "intent_key_list_choose_items";
    public static final String INTENT_KEY_LIST_CHOOSE_ACTION = "intent_key_list_choose_action";
    public static final String INTENT_KEY_LIST_CHOOSE_PARENT_ID = "intent_key_list_choose_parent_id";
    public static final String INTENT_KEY_LIST_CHOOSE_IS_EDIT = "intent_key_list_choose_is_edit";
    public static final String INTENT_KEY_LIST_SRC_ACTIVITY = "intent_key_list_src_activity";
    public static final String INTENT_KEY_LIST_DEVICE_TYPE = "intent_key_list_device_type";
    public static final String INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID = "intent_key_list_choose_residence_bind_id";
    public static final String INTENT_KEY_LIST_CHOOSE_ITEM_RESULT = "intent_key_list_choose_item_result";
    public static final String INTENT_KEY_LIST_SEX_TYPE = "intent_key_list_sex_type";
    public static final String INTENT_KEY_LIST_RESIDENCE_DETAIL = "intent_key_list_residence_detail";
    public static final int ACTION_LIST_SCHOOL = 1;
    public static final int ACTION_LIST_DORMITOR = 2;
    public static final int ACTION_LIST_FLOOR = 3;
    public static final int ACTION_LIST_BUILDING = 4;
    public static final int ACTION_LIST_SEX = 5;
    public static final int ACTION_LIST_SCHOOL_RESULT = 6;
    /**
     * 选择设备，热水澡 or 饮水机
     */
    public static final int ACTION_LIST_DEVICE = 7;
    public static final int ACTION_LIST_WITHDRAW_WAY = 8;
    private static final String TAG = ListChooseActivity.class.getSimpleName();


    private UserActivityComponent mActivityComponent;

    private List<ListChooseAdaptor.Item> items = new ArrayList<>();

    /**
     * 宿舍是否编辑
     */
    private boolean isEditDormitory;

    /**
     * 宿舍编辑时需要的ID
     */
    private Long residenceBindId;

    private int deviceType;

    private String activitySrc = null;

    private UserResidenceDTO residenceDetail;

    @Inject
    IListChoosePresenter<IListChooseView> presenter;

    ListChooseAdaptor adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.v_divide)
    View vDivide;

    @BindView(R.id.v_divide_top)
    View vDivideTop;

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityComponent = DaggerUserActivityComponent.builder()
                .userActivityModule(new UserActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

        setContentView(R.layout.activity_list_choose);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(ListChooseActivity.this);

        adapter = new ListChooseAdaptor(items);
        // TODO ItemDecoration不显示最后一条divide
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        if (getIntent() != null) {
            switch (getIntent().getExtras().getInt(INTENT_KEY_LIST_CHOOSE_ACTION)) {
                case ACTION_LIST_SCHOOL:
                    tvTitle.setText("选择学校");
                    // page size 为null 加载全部
                    presenter.getSchoolList(null, null);
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            presenter.updateSchool(items.get(position).getId());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.wtf(TAG, e);
                        }
                    });
                    break;
                case ACTION_LIST_SEX:
                    tvTitle.setText("选择性别");
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            presenter.updateSex(
                                    TextUtils.equals(items.get(position).getContent(), "男") ? 1 : 2);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.wtf(TAG, e);
                        }
                    });
                    if (getIntent() != null) {
                        int sexType = getIntent().getIntExtra(INTENT_KEY_LIST_SEX_TYPE, -1);
                        if (sexType == 1) {
                            this.items.add(new ListChooseAdaptor.Item("男", true));
                            this.items.add(new ListChooseAdaptor.Item("女", false));
                        } else if (sexType == 2) {
                            this.items.add(new ListChooseAdaptor.Item("男", false));
                            this.items.add(new ListChooseAdaptor.Item("女", true));
                        } else {
                            this.items.add(new ListChooseAdaptor.Item("男", false));
                            this.items.add(new ListChooseAdaptor.Item("女", false));
                        }
                    }
                    vDivide.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    break;
                case ACTION_LIST_BUILDING:
                    tvTitle.setText("选择楼栋");
                    if (getIntent() != null) {
                        isEditDormitory = getIntent().getBooleanExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
                        residenceBindId = getIntent().getLongExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, -1);
                        deviceType = getIntent().getIntExtra(INTENT_KEY_LIST_DEVICE_TYPE, Device.UNKNOWN.getType());
                        activitySrc = getIntent().getStringExtra(INTENT_KEY_LIST_SRC_ACTIVITY);
                        residenceDetail = (UserResidenceDTO) getIntent().getSerializableExtra(INTENT_KEY_LIST_RESIDENCE_DETAIL);
                    }
                    // page size 为null 加载全部
                    presenter.getBuildList(null, null, deviceType);
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            Intent intent = new Intent(ListChooseActivity.this, ListChooseActivity.class);
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_ACTION, ACTION_LIST_FLOOR);
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, items.get(position).getId());
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, isEditDormitory);
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, residenceBindId);
                            intent.putExtra(INTENT_KEY_LIST_DEVICE_TYPE, deviceType);
                            intent.putExtra(INTENT_KEY_LIST_SRC_ACTIVITY, activitySrc);
                            intent.putExtra(INTENT_KEY_LIST_RESIDENCE_DETAIL, residenceDetail);
                            startActivity(intent);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.wtf(TAG, e);
                        }
                    });
                    break;
                case ACTION_LIST_FLOOR:
                    tvTitle.setText("选择楼层");
                    if (getIntent() != null) {
                        isEditDormitory = getIntent().getBooleanExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
                        residenceBindId = getIntent().getLongExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, -1);
                        deviceType = getIntent().getIntExtra(INTENT_KEY_LIST_DEVICE_TYPE, Device.UNKNOWN.getType());
                        activitySrc = getIntent().getStringExtra(INTENT_KEY_LIST_SRC_ACTIVITY);
                        residenceDetail = (UserResidenceDTO) getIntent().getSerializableExtra(INTENT_KEY_LIST_RESIDENCE_DETAIL);
                        Long parentId = getIntent().getLongExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, -1);
                        if (parentId != -1) {
                            // page size 为null 加载全部
                            presenter.getFloorList(null, null, deviceType, parentId);
                        }
                    }
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            Intent intent = new Intent(ListChooseActivity.this, ListChooseActivity.class);
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_ACTION, ACTION_LIST_DORMITOR);
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, items.get(position).getId());
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, isEditDormitory);
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, residenceBindId);
                            intent.putExtra(INTENT_KEY_LIST_DEVICE_TYPE, deviceType);
                            intent.putExtra(INTENT_KEY_LIST_SRC_ACTIVITY, activitySrc);
                            intent.putExtra(INTENT_KEY_LIST_RESIDENCE_DETAIL, residenceDetail);
                            startActivity(intent);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.wtf(TAG, e);
                        }
                    });
                    break;
                case ACTION_LIST_DORMITOR:
                    if (getIntent() != null) {
                        isEditDormitory = getIntent().getBooleanExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
                        residenceBindId = getIntent().getLongExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, -1);
                        deviceType = getIntent().getIntExtra(INTENT_KEY_LIST_DEVICE_TYPE, Device.UNKNOWN.getType());
                        activitySrc = getIntent().getStringExtra(INTENT_KEY_LIST_SRC_ACTIVITY);
                        residenceDetail = (UserResidenceDTO) getIntent().getSerializableExtra(INTENT_KEY_LIST_RESIDENCE_DETAIL);
                        if (deviceType == Device.HEATER.getType()) {
                            tvTitle.setText("选择宿舍");
                        } else {
                            tvTitle.setText("选择位置");
                        }
                        adapter = new ListChooseAdaptor(items, true);
                        recyclerView.setAdapter(adapter);
                        Long parentId = getIntent().getLongExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, -1);
                        if (parentId != -1) {
                            // page size 为null 加载全部
                            if (TextUtils.isEmpty(activitySrc)) {
                                presenter.getDormitoryList(null, null, deviceType, parentId, false);
                            } else if (TextUtils.equals(activitySrc, Constant.REPAIR_APPLY_ACTIVITY_SRC)) {
                                presenter.getDormitoryList(null, null, deviceType, parentId, true);
                            } else if (TextUtils.equals(activitySrc, Constant.EDIT_PROFILE_ACTIVITY_SRC)) {
                                presenter.getDormitoryList(null, null, deviceType, parentId, false);
                            } else if (TextUtils.equals(activitySrc, Constant.MAIN_ACTIVITY_SRC)) {
                                presenter.getDormitoryList(null, null, deviceType, parentId, false);
                            }
                        }
                    }

                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            if (Constant.REPAIR_APPLY_ACTIVITY_SRC.equals(activitySrc)) {
                                ListChooseAdaptor.Item item = items.get(position);
                                Intent intent = new Intent(ListChooseActivity.this, RepairApplyActivity.class);
                                intent.putExtra(Constant.LOCATION_ID, (long) item.getId());
                                intent.putExtra(Constant.DEVICE_TYPE, deviceType);
                                intent.putExtra(Constant.LOCATION, Device.getDevice(deviceType).getDesc() + Constant.CHINEASE_COLON + item.getExtra());
                                startActivity(intent);
                            } else if (Constant.EDIT_PROFILE_ACTIVITY_SRC.equals(activitySrc)) {
                                presenter.bindDormitory(residenceBindId, items.get(position).getId(), isEditDormitory, activitySrc);
                            } else if (Constant.MAIN_ACTIVITY_SRC.equals(activitySrc)) {
                                presenter.bindDormitory(residenceBindId, items.get(position).getId(), isEditDormitory, activitySrc);
                            } else {
                                presenter.bindDormitory(residenceBindId, items.get(position).getId(), isEditDormitory);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.wtf(TAG, e);
                        }
                    });
                    break;
                case ACTION_LIST_SCHOOL_RESULT:
                    tvTitle.setText("选择学校");
                    presenter.getSchoolList(null, null);
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            Intent intent = new Intent();
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_ITEM_RESULT, items.get(position));
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.wtf(TAG, e);
                        }
                    });
                    break;
                case ACTION_LIST_DEVICE:
                    tvTitle.setText("设备类型");
                    this.items.add(new ListChooseAdaptor.Item(Device.HEATER.getDesc(), Device.HEATER.getType()));
                    this.items.add(new ListChooseAdaptor.Item(Device.DISPENSER.getDesc(), Device.DISPENSER.getType()));
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            ListChooseAdaptor.Item item = items.get(position);
                            Intent intent = new Intent(ListChooseActivity.this, ListChooseActivity.class);
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_ACTION, ACTION_LIST_BUILDING);
                            intent.putExtra(INTENT_KEY_LIST_DEVICE_TYPE, item.getDeviceType());
                            intent.putExtra(INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
                            startActivity(intent);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.wtf(TAG, e);
                        }
                    });
                    vDivide.setVisibility(View.VISIBLE);
                    break;
                case ACTION_LIST_WITHDRAW_WAY:
                    tvTitle.setText("选择提现方式");
                    for (WithdrawWay way : WithdrawWay.values()) {
                        this.items.add(new ListChooseAdaptor.Item(way.getDesc(), way.getType()));
                    }
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            ListChooseAdaptor.Item item = items.get(position);
                            Intent intent = new Intent(ListChooseActivity.this, WithdrawalActivity.class);
                            intent.putExtra(INTENT_KEY_LIST_CHOOSE_ITEM_RESULT, item.getContent());
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.wtf(TAG, e);
                        }
                    });
                    vDivide.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    public UserActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void backToDormitory() {
        Intent intent = new Intent(this, EditDormitoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void backToRepairApply(String location) {
        Intent intent = new Intent(this, RepairApplyActivity.class);
        intent.putExtra(Constant.LOCATION, location);
        startActivity(intent);
    }

    @Override
    public void addMore(List<ListChooseAdaptor.Item> items) {
        if (residenceDetail != null) {
            for (ListChooseAdaptor.Item item : items) {
                if (CommonUtil.equals(item.getId(), residenceDetail.getBuildingId())) {
                    item.setTick(true);
                }
                if (CommonUtil.equals(item.getId(), residenceDetail.getFloorId())) {
                    item.setTick(true);
                }
                if (CommonUtil.equals(item.getId(), residenceDetail.getResidenceId())) {
                    item.setTick(true);
                }
            }
        }
        vDivide.setVisibility(View.VISIBLE);
        this.items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void backToMain(String activitySrc) {
        if (Constant.MAIN_ACTIVITY_SRC.equals(activitySrc)) {
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra(Constant.INTENT_ACTION, IntentAction.ACTION_GOTO_HEATER.getType()));
        } else {
            backToMain();
        }
    }

    @Override
    public void backToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void backToEditProfile() {
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    @Override
    public void backToEditDormitory() {
        startActivity(new Intent(this, EditDormitoryActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.equals(activitySrc, Constant.EDIT_PROFILE_ACTIVITY_SRC)) {
            backToEditProfile();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showEmptyView() {
        items.clear();
        vDivideTop.setVisibility(View.GONE);
        rlEmpty.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideEmptyView() {
        rlEmpty.setVisibility(View.GONE);
        vDivideTop.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
