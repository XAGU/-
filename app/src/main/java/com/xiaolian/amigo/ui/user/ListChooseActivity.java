package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.IntentAction;
import com.xiaolian.amigo.data.enumeration.WithdrawWay;
import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.repair.RepairApplyActivity;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.user.CompleteInfoActivity;
import com.xiaolian.amigo.ui.user.intf.IListChoosePresenter;
import com.xiaolian.amigo.ui.user.intf.IListChooseView;
import com.xiaolian.amigo.ui.wallet.WithdrawalActivity;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_BUILDING_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_NAME;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_TYPE;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_LOCATION;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_MAC_ADDRESS;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_SUPPLIER_ID;

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

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolBarTitle;

    @BindView(R.id.view_line)
    View viewLine;

    private int action;

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
            action = getIntent().getIntExtra(INTENT_KEY_LIST_CHOOSE_ACTION, ACTION_LIST_SCHOOL);
            switch (action) {
                case ACTION_LIST_SCHOOL:
                    tvTitle.setText("选择学校");
                    // page size 为null 加载全部
                    presenter.getSchoolList(null, null, true);
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
                        isEditDormitory = getIntent().getBooleanExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);    //  是否是编辑页面进入
                        residenceBindId = getIntent().getLongExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, -1);   //  type 表示是什么类型， 楼栋 ， 宿舍
                        deviceType = getIntent().getIntExtra(INTENT_KEY_LIST_DEVICE_TYPE, Device.UNKNOWN.getType());
                        activitySrc = getIntent().getStringExtra(INTENT_KEY_LIST_SRC_ACTIVITY);
                        residenceDetail = (UserResidenceDTO) getIntent().getSerializableExtra(INTENT_KEY_LIST_RESIDENCE_DETAIL);
                    }
                    // page size 为null 加载全部
                    if (Constant.MAIN_ACTIVITY_BATHROOM_SRC.equals(activitySrc)
                           || Constant.ADD_BATHROOM_SRC.equals(activitySrc) ){
                        presenter.queryBathResidenceList(null ,null , deviceType);
                    }else {
                        presenter.getBuildList(null, null, deviceType);
                    }
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            if (presenter.isStartBathroom(this.items.get(position))) {
                                Intent intent = new Intent(ListChooseActivity.this, ListChooseActivity.class);
                                intent.putExtra(INTENT_KEY_LIST_CHOOSE_ACTION, ACTION_LIST_FLOOR);
                                intent.putExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, items.get(position).getId());
                                intent.putExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, isEditDormitory);
                                intent.putExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, residenceBindId);
                                intent.putExtra(INTENT_KEY_LIST_DEVICE_TYPE, deviceType);
                                intent.putExtra(INTENT_KEY_LIST_SRC_ACTIVITY, activitySrc);
                                intent.putExtra(INTENT_KEY_LIST_RESIDENCE_DETAIL, residenceDetail);
                                startActivity(intent);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "数组越界");
                        } catch (Exception e) {
                            Log.e(TAG , e.getMessage());
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
                            if (Constant.USER_INFO_ACTIVITY_SRC.equals(activitySrc)) {
                                presenter.getFloorList(null, null, deviceType, parentId);
                            }else{
                                presenter.getBathFloorList(null , null , deviceType , parentId);
                            }
                        }
                    }
                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            if (presenter.isStartBathroom(this.items.get(position))) {
                                Intent intent = new Intent(ListChooseActivity.this, ListChooseActivity.class);
                                intent.putExtra(INTENT_KEY_LIST_CHOOSE_ACTION, ACTION_LIST_DORMITOR);
                                intent.putExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, items.get(position).getId());
                                intent.putExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, isEditDormitory);
                                intent.putExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, residenceBindId);
                                intent.putExtra(INTENT_KEY_LIST_DEVICE_TYPE, deviceType);
                                intent.putExtra(INTENT_KEY_LIST_SRC_ACTIVITY, activitySrc);
                                intent.putExtra(INTENT_KEY_LIST_RESIDENCE_DETAIL, residenceDetail);
                                startActivity(intent);
                            }
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
                        if (TextUtils.equals(activitySrc, Constant.USER_INFO_ACTIVITY_SRC)) {
                                tvTitle.setText("选择宿舍");
                        }else {
                            if (deviceType == Device.HEATER.getType()) {
                                tvTitle.setText("选择洗澡房间");
                            } else {
                                tvTitle.setText("选择位置");
                            }
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
                            }else if (TextUtils.equals(activitySrc ,Constant.USER_INFO_ACTIVITY_SRC)){
                                presenter.getDormitoryList(null, null, deviceType, parentId, false);
                            }else if (TextUtils.equals(activitySrc ,Constant.MAIN_ACTIVITY_BATHROOM_SRC)){
                                presenter.getBathroomList(null, null, deviceType, parentId, false);
                            }else if (TextUtils.equals(activitySrc ,Constant.ADD_BATHROOM_SRC)){
                                presenter.getBathroomList(null, null, deviceType, parentId, false);
                            }
                        }
                    }

                    adapter.setOnItemClickListener((view, position) -> {
                        try {
                            if (presenter.isStartBathroom(this.items.get(position))) {
                                int bathType = -1;
                                if (items.get(position).isAllBaths()) bathType = 2;
                                else bathType = 1;
                                if (Constant.REPAIR_APPLY_ACTIVITY_SRC.equals(activitySrc)) {
                                    ListChooseAdaptor.Item item = items.get(position);
                                    Intent intent = new Intent(ListChooseActivity.this, RepairApplyActivity.class);
                                    intent.putExtra(Constant.LOCATION_ID, (long) item.getId());
                                    intent.putExtra(Constant.DEVICE_TYPE, deviceType);
                                    intent.putExtra(Constant.LOCATION, Device.getDevice(deviceType).getDesc() + Constant.CHINEASE_COLON + item.getExtra());
                                    startActivity(intent);

                                } else if (Constant.EDIT_PROFILE_ACTIVITY_SRC.equals(activitySrc)) {
//                                presenter.bindDormitory(residenceBindId, items.get(position).getId(), isEditDormitory, activitySrc);
                                } else if (Constant.MAIN_ACTIVITY_SRC.equals(activitySrc)) {
//                                presenter.bindDormitory(residenceBindId, items.get(position).getId(), isEditDormitory, activitySrc);
                                } else if (Constant.USER_INFO_ACTIVITY_SRC.equals(activitySrc)) {
                                    presenter.updateUser(items.get(position).getId(), activitySrc);
                                } else if (Constant.COMPLETE_INFO_ACTIVITY_SRC.equals(activitySrc)) {
                                    presenter.updateUser(items.get(position).getId(), activitySrc);
                                } else if (Constant.MAIN_ACTIVITY_BATHROOM_SRC.equals(activitySrc)) {
                                    presenter.recordBath(items.get(position).getId(), bathType, null);
                                } else if (Constant.ADD_BATHROOM_SRC.equals(activitySrc)) {
                                    presenter.recordBath(items.get(position).getId(), bathType, null);
                                } else {
//                                presenter.bindDormitory(residenceBindId, items.get(position).getId(), isEditDormitory);
                                }
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
                    presenter.getSchoolList(null, null, true);
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

        initToolBar();
    }

    private void initToolBar() {
        tvToolBarTitle.setVisibility(View.GONE);
        viewLine.setVisibility(View.GONE);
        tvToolBarTitle.setText(tvTitle.getText().toString());

        appBarLayout.addOnOffsetChangedListener((AppBarLayout appBarLayout, int verticalOffset) -> {
            if (verticalOffset < - (tvToolBarTitle.getHeight() + tvToolBarTitle.getPaddingTop())) {
                tvToolBarTitle.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvToolBarTitle.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });
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
                if (ObjectsCompat.equals(item.getId(), residenceDetail.getBuildingId())) {
                    item.setTick(true);
                }
                if (ObjectsCompat.equals(item.getId(), residenceDetail.getFloorId())) {
                    item.setTick(true);
                }
                if (ObjectsCompat.equals(item.getId(), residenceDetail.getResidenceId())) {
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
    public void backToEditProfileActivity(String residenceName) {
        startActivity(this ,EditProfileActivity.class);
        this.finish();
    }

    public void backToCompeteInfoActivity(String residenceName) {
        startActivity(this ,CompleteInfoActivity.class);
        this.finish();
    }

    @Override
    public void startBathroom(UserResidenceInListDTO dto) {
        startActivity(new Intent(this , ChooseBathroomActivity.class)
        .putExtra(KEY_ID , dto.getId())
        .putExtra(KEY_BUILDING_ID ,dto.getBuildingId())
        .putExtra(KEY_RESIDENCE_ID , dto.getResidenceId())
        .putExtra(KEY_RESIDENCE_TYPE , dto.getResidenceType())
        .putExtra(KEY_RESIDENCE_NAME , dto.getResidenceName()));
        this.finish();
    }

    @Override
    public void startShower(UserResidenceInListDTO dto) {
        startActivity(new Intent(this , HeaterActivity.class)
                .putExtra(INTENT_KEY_LOCATION ,dto.getResidenceName())
                .putExtra(INTENT_KEY_MAC_ADDRESS ,dto.getMacAddress())
                .putExtra(INTENT_KEY_SUPPLIER_ID , dto.getSupplierId()));
        this.finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
    private int mTouchRepeat = 0;
    private boolean mPoint2Down = false;
    private boolean mThreePointDown = false;
    long[] mHits = new long[4];
    private boolean online = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (action == ACTION_LIST_SCHOOL || action == ACTION_LIST_SCHOOL_RESULT) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPoint2Down = false;
                    mThreePointDown = false;
                    mTouchRepeat = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchRepeat++;
                    break;
                case MotionEvent.ACTION_POINTER_2_DOWN:
                    mPoint2Down = true;
                    break;
                case MotionEvent.ACTION_POINTER_3_DOWN:
                    mThreePointDown = true;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    if (mPoint2Down && mTouchRepeat < 10 && !mThreePointDown) {
                        System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                        mHits[mHits.length-1] = SystemClock.uptimeMillis();
                        if (mHits[0] >= (SystemClock.uptimeMillis()-3000)) {
                            items.clear();
                            online = !online;
                            Arrays.fill(mHits, 0);
                            presenter.getSchoolList(null, null, online);
                        }
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
