package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;
import com.xiaolian.amigo.ui.user.EditDormitoryActivity;
import com.xiaolian.amigo.ui.user.FindBathroomPasswordActivity;
import com.xiaolian.amigo.ui.widget.ZoomRecyclerView;
import com.xiaolian.amigo.ui.widget.dialog.BathroomBookingDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BALANCE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_AMOUNT;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_DESC;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_DEVICE_NO;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_EXPIRED_TIME;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_LOCATION;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MAX_MISSABLE_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MIN_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MISSED_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_ORDER_PRECONDITION;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_RESERVEDTIME;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomHeaterActivity.KEY_BATH_ORDER_ID;
import static com.xiaolian.amigo.util.Constant.AVAILABLE;
import static com.xiaolian.amigo.util.Constant.BATH_USING;
import static com.xiaolian.amigo.util.Constant.ERROR;
import static com.xiaolian.amigo.util.Constant.NONE;
import static com.xiaolian.amigo.util.Constant.USING_STATUS;

/**
 * 选择浴室
 *
 * @author zcd
 * @date 18/6/27
 */
public class ChooseBathroomActivity extends BathroomBaseActivity implements IChooseBathroomView {
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_RESIDENCE_ID = "key_residence_id";  // 建筑id
    public static final String KEY_RESIDENCE_TYPE = "key_residence_type";
    public static final String KEY_BUILDING_ID = "key_building_id";
    public static final String KEY_RESIDENCE_NAME = "key_residence_name";
    private static final String TAG = ChooseBathroomActivity.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_help)
    ImageView ivHelp;
    @BindView(R.id.pre_bathroom)
    Button preBathroom;
    private BathroomBookingDialog bathroomBookingDialog;

    private long buildId;    //  楼栋id ；
    private long residenceId;   //  建筑id
    private long residenceType;
    private long id;
    private String residenceName;
    @Inject
    IChooseBathroomPresenter<IChooseBathroomView> presenter;

    private List<ChooseBathroomOuterAdapter.BathGroupWrapper> bathGroups = new ArrayList<>();

    private ZoomRecyclerView recyclerView;
    private ChooseBathroomOuterAdapter outerAdapter;
    //    private ImageView ivHelp;
//    private MetaBallView metaBall;
    private int lastSelectedGroupPosition = -1;
    private int lastSelectedRoomPosition = -1;
    private String deviceNo="";
    /**
     * 当前是否处于选中状态 选中状态显示预约使用 非选中状态显示购买编码
     */
    private boolean isSelected = false;
    /**
     * 是否显示付费使用
     */
    private boolean isShowBuyUse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bathroom);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setUp();
        bindView();
        initRecyclerView();
    }

    private void bindView() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        recyclerView = findViewById(R.id.recyclerView);
//        metaBall = findViewById(R.id.metaBall);
//        metaBall.setOnButtonClickListener(left -> {
//            if (left) {
//                onLeftClick();
//            } else {
//                onRightClick();
//            }
//        });
        setBtnText("预约洗澡 (任意空浴室)" , false );

    }

    /**
     * 获取帮助
     */
    private void help() {
        isSelected = !isSelected;
//            changeToBookingUse();
//            metaBall.translation();
        gotoShowerAddress();
    }

    @OnClick({R.id.iv_help, R.id.tv_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_help:
                help();
                break;
            case R.id.tv_title:
                startEditBathroom();
                break;
        }
    }


    /**
     * 选择洗澡地址
     */
    private void startEditBathroom() {
        startActivity(new Intent(this, EditDormitoryActivity.class));
    }

    private void gotoShowerAddress() {
        startActivity(new Intent(this, ShowerAddressActivity.class));
    }

    private void initRecyclerView() {
        setMockData(bathGroups);
        outerAdapter = new ChooseBathroomOuterAdapter(this, R.layout.item_choose_bathroom_outer, bathGroups,
                (groupPosition, bathroomPosition) -> {
                    onSuccess(groupPosition + " " + bathroomPosition);
                    if (bathGroups.get(groupPosition).getBathGroups().get(bathroomPosition).getStatus()
                            != AVAILABLE) {
                        if (lastSelectedGroupPosition != -1 && lastSelectedRoomPosition != -1) {
                            bathGroups.get(lastSelectedGroupPosition).getBathGroups().get(lastSelectedRoomPosition).setSelected(false);
                        }
                        lastSelectedGroupPosition = -1;
                        lastSelectedRoomPosition = -1;
                        isSelected = false;
                    } else {
                        if (lastSelectedGroupPosition != -1 && lastSelectedRoomPosition != -1) {
                            bathGroups.get(lastSelectedGroupPosition).getBathGroups().get(lastSelectedRoomPosition).setSelected(false);
                        }
                        deviceNo = bathGroups.get(groupPosition).getBathGroups().get(bathroomPosition).getDeviceNo();
                        bathGroups.get(groupPosition).getBathGroups().get(bathroomPosition).setSelected(true);
                        lastSelectedGroupPosition = groupPosition;
                        lastSelectedRoomPosition = bathroomPosition;
                        isSelected = true;
                    }
                    outerAdapter.notifyDataSetChanged();
                    setBtnText(("预约洗澡 ("+ bathGroups.get(groupPosition).getBathGroups().get(bathroomPosition).getId()+"号浴室)") , true);
                });
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        recyclerView.setLayoutParams(lp);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(outerAdapter);
        recyclerView.setOnGestureListener(new ZoomRecyclerView.OnGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector, float scaleFactor) {
                if (scaleFactor > 2
                        && !outerAdapter.isScaled()) {
//                    outerAdapter.notifyDataSetChanged();
                    outerAdapter.setScaled(true);
                } else if (scaleFactor < 2
                        && outerAdapter.isScaled()) {
                    outerAdapter.setScaled(false);
                }
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }
        });
//        presenter.getBathroomList(buildId);
        showBathroomDialog();
        presenter.precondition();
    }

//    private void changeBottomUseWay() {
//        if (!isSelected) {
//            metaBall.changeToBuyCodeWay();
//        } else {
//            metaBall.changeToBookingWay();
//        }
//    }


    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            buildId = getIntent().getLongExtra(KEY_BUILDING_ID, -1);
            residenceId = getIntent().getLongExtra(KEY_RESIDENCE_ID, -1);
            residenceType = getIntent().getLongExtra(KEY_RESIDENCE_TYPE, -1);
            id = getIntent().getLongExtra(KEY_ID, -1);
            residenceName = getIntent().getStringExtra(KEY_RESIDENCE_NAME);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        if (bathroomBookingDialog != null) {
            bathroomBookingDialog.onDettechView();
        }
    }


    //    private void changeToBookingUse() {
//        if (!isSelected) {
//            llRight.setVisibility(View.VISIBLE);
//            tvMissedBookingTime.setVisibility(View.INVISIBLE);
//            tvLeft.setText("购买编码");
//            ivLeft.setImageResource(R.drawable.ic_bathroom_buy_code);
//            tvRight.setText("扫一扫");
//            ivRight.setImageResource(R.drawable.ic_bathroom_scan);
//        } else {
//            tvMissedBookingTime.setVisibility(View.VISIBLE);
//            tvLeft.setText("预约使用");
//            ivLeft.setImageResource(R.drawable.ic_bathroom_booking);
//            if (isShowBuyUse) {
//                llRight.setVisibility(View.VISIBLE);
//                tvRight.setText("付费使用");
//                ivRight.setImageResource(R.drawable.ic_bathroom_pay_use);
//            } else {
//                llRight.setVisibility(View.GONE);
//            }
//        }
//    }

//    private void onLeftClick() {
//        if (isSelected) {
//            presenter.preBooking(deviceNo);
//        } else {
//            startActivity(new Intent(this, BuyCodeActivity.class));
//        }
//    }

//    private void onRightClick() {
//        if (isSelected) {
//            startActivity(new Intent(this, PayUseActivity.class));
//        } else {
////            startActivity(new Intent(this, BathroomScanActivity.class));
//            startActivity(new Intent(this, ScanUseActivity.class));
//        }
//    }

    private void setMockData(List<ChooseBathroomOuterAdapter.BathGroupWrapper> bathGroups) {
        List<ChooseBathroomAdapter.BathroomWrapper> inner1 = new ArrayList<>();
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("102", AVAILABLE));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("103", NONE));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("104", BATH_USING));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ERROR));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ERROR));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ERROR));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ERROR));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ERROR));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner1, "一层A"));

        List<ChooseBathroomAdapter.BathroomWrapper> inner2 = new ArrayList<>();
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner2, "一层B"));

        List<ChooseBathroomAdapter.BathroomWrapper> inner3 = new ArrayList<>();
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner3, "一层A"));

        List<ChooseBathroomAdapter.BathroomWrapper> inner4 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));


        List<ChooseBathroomAdapter.BathroomWrapper> inner5 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));


        List<ChooseBathroomAdapter.BathroomWrapper> inner6 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));


        List<ChooseBathroomAdapter.BathroomWrapper> inner7 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));


        List<ChooseBathroomAdapter.BathroomWrapper> inner8 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));


        List<ChooseBathroomAdapter.BathroomWrapper> inner9 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));


        List<ChooseBathroomAdapter.BathroomWrapper> inner10 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));


        List<ChooseBathroomAdapter.BathroomWrapper> inner11 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", BATH_USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));


    }

    @Override
    public void refreshBathroom(List<ChooseBathroomOuterAdapter.BathGroupWrapper> wrappers,
                                List<Integer> methods, Integer missTimes) {
        bathGroups.clear();
        bathGroups.addAll(wrappers);
        outerAdapter.notifyDataSetChanged();
        for (Integer method : methods) {
            setBathroomMethod(method);
        }
    }


    public void setBathroomMethod(int methods) {

//        if (methods == BOOKING.getCode()) {
//            metaBall.getLlLeft().setVisibility(View.VISIBLE);
//        } else if (methods == BUY_CODE.getCode()) {
//            metaBall.getLlRight().setVisibility(View.VISIBLE);
//        }

    }


    @Override
    public void gotoBookingView(Double balance,
                                Long bonusId, String bonusDesc, Double bonusAmount,
                                Long expiredTime, String location, Integer maxMissAbleTimes,
                                Double minPrepay, Integer missedTimes, Double prepay, String reservedTime) {
        startActivity(new Intent(this, BookingActivity.class)
                .putExtra(KEY_BALANCE, balance)
                .putExtra(KEY_BONUS_ID, bonusId)
                .putExtra(KEY_BONUS_DESC, bonusDesc)
                .putExtra(KEY_BONUS_AMOUNT, bonusAmount)
                .putExtra(KEY_EXPIRED_TIME, expiredTime)
                .putExtra(KEY_LOCATION, location)
                .putExtra(KEY_MAX_MISSABLE_TIMES, maxMissAbleTimes)
                .putExtra(KEY_MIN_PREPAY, minPrepay)
                .putExtra(KEY_MISSED_TIMES, missedTimes)
                .putExtra(KEY_PREPAY, prepay)
                .putExtra(KEY_RESERVEDTIME, reservedTime)
                .putExtra(KEY_DEVICE_NO, deviceNo)
        );
    }



    @Override
    public void startPreconditionView(BathOrderPreconditionRespDTO respDTO) {
        hideBathroomDialog();
        startActivityForStatus(respDTO);
//        startActivity(new Intent(this ,BookingActivity.class).putExtra(KEY_ORDER_PRECONDITION ,respDTO));
    }

    /**
     * 根据订单status跳转正确的界面
     *
     * @param respDTO
     */
    private void startActivityForStatus(BathOrderPreconditionRespDTO respDTO) {
                if (respDTO.getStatus() == USING_STATUS){
                    startActivity(new Intent(this , BathroomHeaterActivity.class)
                    .putExtra(KEY_BATH_ORDER_ID ,respDTO.getBathOrderId()));
                }else {
                    startActivity(new Intent(this, BookingActivity.class).putExtra(KEY_ORDER_PRECONDITION, respDTO));
                }

    }

    @Override
    public void showBathroomDialog() {
        if (bathroomBookingDialog == null) {
            bathroomBookingDialog = new BathroomBookingDialog(this);
        }
        bathroomBookingDialog.show();
    }


    @Override
    public void hideBathroomDialog() {
        if (bathroomBookingDialog != null) {
            bathroomBookingDialog.cancel();
            bathroomBookingDialog.onDettechView();
        }
    }

    @Override
    public void setTvTitle(String name) {
        tvTitle.setText(name);
    }


    @Override
    public void setBtnText(String text , boolean isSelected) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(14, true), 4, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        preBathroom.setText(spannable);
        this.isSelected = isSelected ;
        if (isSelected) preBathroom.setBackgroundResource(R.drawable.green_button);
        else preBathroom.setBackgroundResource(R.drawable.red_button);
    }

    @Override
    public void gotoUsing(BathOrderPreconditionRespDTO respDTO) {

    }


    @OnClick(R.id.pre_bathroom)
    public void preBathRoom(){
        if (presenter.getBathroomPassword()) {
            if (this.isSelected) {
                presenter.preBooking(deviceNo);
            } else {
                presenter.preBooking("");
            }
        }else{
            startActivity(this , FindBathroomPasswordActivity.class);
        }

    }

}
