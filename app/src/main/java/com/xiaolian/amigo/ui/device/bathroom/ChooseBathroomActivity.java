package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO.FloorsBean.GroupsBean;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.user.EditDormitoryActivity;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.ui.widget.AutoBathroom;
import com.xiaolian.amigo.ui.widget.CircleProgressView;
import com.xiaolian.amigo.ui.widget.dialog.BathroomBookingDialog;
import com.xiaolian.amigo.ui.widget.popWindow.ChooseBathroomPop;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.RxHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.xiaolian.amigo.ui.device.DeviceOrderActivity.KEY_USER_STYLE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BALANCE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MAX_MISSABLE_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MIN_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MISSED_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomHeaterActivity.KEY_BATH_ORDER_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BookingActivity.KEY_BATHQUEUE_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BookingActivity.KEY_BOOKING_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BookingActivity.KEY_DEVICE_ACTIVITY_FOR_RESULT;

/**
 * 选择浴室
 *
 * @author zcd
 * @date 18/6/27
 */
public class ChooseBathroomActivity extends BathroomBaseActivity implements IChooseBathroomView, AutoBathroom.BathroomClick, CircleProgressView.FinishListener {
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_RESIDENCE_ID = "key_residence_id";  // 建筑id
    public static final String KEY_RESIDENCE_TYPE = "key_residence_type";
    public static final String KEY_BUILDING_ID = "key_building_id";
    public static final String KEY_RESIDENCE_NAME = "key_residence_name";
    public static final int KEY_BOOKING_RESQCODE = 11;   //  booking 页面返回

    public static final int KEY_BOOKING_RESULTCODE = 12;  //
    private static final String TAG = ChooseBathroomActivity.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_help)
    ImageView ivHelp;
    @BindView(R.id.pre_bathroom)
    Button preBathroom;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.auto_bathroom)
    AutoBathroom autoBathroom;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.id_content)
    RelativeLayout idContent;
    @BindView(R.id.refer)
    TextView refer;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    private BathroomBookingDialog bathroomBookingDialog;

    private long buildId;    //  楼栋id ；
    private long residenceId;   //  建筑id
    private long residenceType;
    private long id;
    private String residenceName;


    private boolean isShowDialog = true;  //  是否显示动画dialog
    /**
     * 预付金额
     */
    protected Double prepay;
    /**
     * 最小预付金额
     */
    protected Double minPrepay;
    /**
     * 用户余额
     */
    protected Double balance;
    /**
     * 失约次数 只有预约才会返回
     */
    protected Integer missedTimes;
    /**
     * 总共可失约次数 只有预约才会返回
     */
    protected Integer maxMissAbleTimes;


    @Inject
    IChooseBathroomPresenter<IChooseBathroomView> presenter;


    private String deviceNo = "";


    /**
     * 当前是否处于选中状态 选中状态显示预约使用 非选中状态显示购买编码
     */
    private boolean isSelected = false;


    /**
     * 显示选择房间信息的popwindow
     */
    private ChooseBathroomPop pop;


    /**
     * 需要充值
     */
    private boolean needCharge = false;


    private boolean isCanJump = false;

    /**
     * 排队Id
     */
    private long queueId = 0;

    /**
     * 预约I
     */
    private long bookingId = 0;

    /**
     * 订单Id
     */
    private long bathOrderId = 0;

    /**
     * 订单详情Id
     */
    private BathOrderCurrentRespDTO bathOrderCurrentRespDTO = null;

    private List<BathBuildingRespDTO.FloorsBean> floorsBeans = new ArrayList<>();

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
        initPop();
    }


    public void initPop() {
        if (pop == null) {
            pop = new ChooseBathroomPop(this);
            pop.setPopButtonClickListener(new ChooseBathroomPop.PopButtonClickListener() {
                @Override
                public void click(BuildingTrafficDTO.FloorsBean floorsBean) {
                    if (floorsBean != null) {
                        if (!TextUtils.isEmpty(floorsBean.getDeviceNo())) {
                            presenter.booking(Long.parseLong(floorsBean.getDeviceNo()), 0);
                        } else {
                            presenter.booking(0, floorsBean.getId());
                        }
                    }
                }
            });
        }
    }


    private void bindView() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        setBtnText("预约洗澡 (任意空浴室)", false);
        isShowDialog = true;
        autoBathroom.setBathroomClick(this);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * 获取帮助
     */
    private void help() {
        startActivity(new Intent(getApplicationContext(), WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
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


    private void initRecyclerView() {

//        test();
//        showBathroomDialog();
    }

    private void test() {
        List<GroupsBean> groupsBeans = new ArrayList<>();

        List<GroupsBean.BathRoomsBean> bathRoomsBeans = new ArrayList<>();

        List<GroupsBean.BathRoomsBean> bathRoomsBeans1 = new ArrayList<>();

        GroupsBean.BathRoomsBean bathRoomsBean1 = new GroupsBean.BathRoomsBean();
        bathRoomsBean1.setId(11111);
        bathRoomsBean1.setXaxis(1);
        bathRoomsBean1.setYaxis(1);
        bathRoomsBean1.setStatus(2);
        bathRoomsBeans.add(bathRoomsBean1);

        GroupsBean.BathRoomsBean bathRoomsBean2 = new GroupsBean.BathRoomsBean();
        bathRoomsBean2.setId(2222);
        bathRoomsBean2.setXaxis(1);
        bathRoomsBean2.setYaxis(2);
        bathRoomsBean2.setStatus(2);
        bathRoomsBeans.add(bathRoomsBean2);

        GroupsBean.BathRoomsBean bathRoomsBean7 = new GroupsBean.BathRoomsBean();
        bathRoomsBean7.setId(2222);
        bathRoomsBean7.setXaxis(1);
        bathRoomsBean7.setYaxis(2);
        bathRoomsBean7.setStatus(1);
        bathRoomsBeans.add(bathRoomsBean7);

        GroupsBean.BathRoomsBean bathRoomsBean4 = new GroupsBean.BathRoomsBean();
        bathRoomsBean4.setId(33333);
        bathRoomsBean4.setXaxis(2);
        bathRoomsBean4.setYaxis(2);
        bathRoomsBean4.setStatus(1);
        bathRoomsBeans.add(bathRoomsBean4);


        GroupsBean.BathRoomsBean bathRoomsBean3 = new GroupsBean.BathRoomsBean();
        bathRoomsBean3.setId(2222);
        bathRoomsBean3.setXaxis(1);
        bathRoomsBean3.setYaxis(1);
        bathRoomsBean3.setStatus(2);
        bathRoomsBeans1.add(bathRoomsBean3);

        GroupsBean.BathRoomsBean bathRoomsBean5 = new GroupsBean.BathRoomsBean();
        bathRoomsBean5.setId(2222);
        bathRoomsBean5.setXaxis(1);
        bathRoomsBean5.setYaxis(5);
        bathRoomsBean5.setStatus(1);
        bathRoomsBeans1.add(bathRoomsBean5);

        GroupsBean.BathRoomsBean bathRoomsBean6 = new GroupsBean.BathRoomsBean();
        bathRoomsBean6.setId(2222);
        bathRoomsBean6.setXaxis(5);
        bathRoomsBean6.setYaxis(2);
        bathRoomsBean6.setStatus(4);
        bathRoomsBeans1.add(bathRoomsBean6);


        GroupsBean groupsBean = new GroupsBean();
        groupsBean.setBathRooms(bathRoomsBeans);
        groupsBean.setDisplayName("A层1组101房间");
        groupsBeans.add(groupsBean);


        GroupsBean groupsBean1 = new GroupsBean();
        groupsBean1.setBathRooms(bathRoomsBeans1);
        groupsBean1.setDisplayName("A层2组102房间");
        groupsBeans.add(groupsBean1);


        BathBuildingRespDTO.FloorsBean floorsBean2 = new BathBuildingRespDTO.FloorsBean();
        floorsBean2.setGroups(groupsBeans);
        floorsBeans.add(floorsBean2);
        autoBathroom.setData(floorsBeans);
    }


    /**
     * 显示有设备提示的弹窗
     *
     * @param deviceName
     * @param deviceNo
     */
    private void showPop(String deviceName, String deviceNo) {
        BuildingTrafficDTO.FloorsBean floorsBean = new BuildingTrafficDTO.FloorsBean();
        floorsBean.setName(deviceName);
        floorsBean.setDeviceNo(deviceNo);

        //  避免预约指定房间出现空闲几人的显示
        floorsBean.setWaitCount(-1);
        floorsBean.setAvailableCount(-1);
        showPopForDevice(floorsBean);
        preBathroom.setClickable(true);
        this.deviceNo = deviceNo + "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCanJump = true;
        bookingId = 0;
        queueId = 0;
        bathOrderId = 0;
        bathOrderCurrentRespDTO = null;
        presenter.setIsResume(true);
        presenter.getBathroomList(buildId);
        presenter.precondition(isShowDialog);
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            buildId = getIntent().getLongExtra(KEY_BUILDING_ID, -1);
            residenceId = getIntent().getLongExtra(KEY_RESIDENCE_ID, -1);
            residenceType = getIntent().getLongExtra(KEY_RESIDENCE_TYPE, -1L);
            id = getIntent().getLongExtra(KEY_ID, -1);
            residenceName = getIntent().getStringExtra(KEY_RESIDENCE_NAME);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCanJump = false;
        presenter.setIsResume(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        if (bathroomBookingDialog != null) {
            bathroomBookingDialog.onDettechView();
        }
        presenter.clearTime();
    }


    private void showPopForDevice(BuildingTrafficDTO.FloorsBean floorsBean) {
        if (pop == null) {
            pop = new ChooseBathroomPop(this);
            pop.setPopButtonClickListener(new ChooseBathroomPop.PopButtonClickListener() {
                @Override
                public void click(BuildingTrafficDTO.FloorsBean floorsBean) {
                    if (floorsBean != null) {
                        if (!TextUtils.isEmpty(floorsBean.getDeviceNo())) {
                            presenter.booking(Long.parseLong(floorsBean.getDeviceNo()), 0);
                        } else {
                            presenter.booking(0, floorsBean.getId());
                        }
                    }
                }
            });
        }

        pop.setData(floorsBean);
        showPopNoDevice();
    }


    /**
     * 显示pop
     */
    private void showPop() {
        presenter.buildingTraffic(buildId);
    }

    private void showPopNoDevice() {
        presenter.setIsResume(false);
        pop.showUp(preBathroom);
        lightOff();
        /**
         * 消失时屏幕变亮
         */
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

                layoutParams.alpha = 1.0f;

                getWindow().setAttributes(layoutParams);

                presenter.setIsResume(true);
                deviceNo = "";
                if (autoBathroom != null) {
                    autoBathroom.setBathroomAvable();
                    autoBathroom.invalidate();
                }
            }
        });
        preBathroom.setClickable(true);
    }


    /**
     * 显示时屏幕变暗
     */
    private void lightOff() {

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        layoutParams.alpha = 0.7f;

        getWindow().setAttributes(layoutParams);

    }


    @Override
    public void refreshBathroom(BathBuildingRespDTO respDTO) {
        idContent.setVisibility(View.VISIBLE);
        rlError.setVisibility(View.GONE);
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_down);
        drawable.setBounds(0,0,drawable.getMinimumWidth() ,drawable.getMinimumHeight());
        tvTitle.setCompoundDrawablesRelative(null , null ,drawable ,null);
        if (floorsBeans == null) {
            floorsBeans = respDTO.getFloors();
        } else {
            floorsBeans.clear();
            floorsBeans.addAll(respDTO.getFloors());
        }
        autoBathroom.setData(floorsBeans);
    }


    
    @Override
    public void showBathroomDialog(String content) {
        if (bathroomBookingDialog == null) {
            bathroomBookingDialog = new BathroomBookingDialog(this);
        }
        bathroomBookingDialog.circleProgressView.setFinishListener(this);
        bathroomBookingDialog.setTitleContent(content);
        bathroomBookingDialog.show();
    }

    @Override
    public void hideBathroomDialog(boolean isSuccess) {
        if (isSuccess) {
            if (bathroomBookingDialog != null && bathroomBookingDialog.isShowing()) {
                bathroomBookingDialog.setFinish();
            }
        } else {
            if (bathroomBookingDialog != null && bathroomBookingDialog.isShowing()) {
                bathroomBookingDialog.dismiss();
            }
        }
    }

    @Override
    public void setTvTitle(String name) {
        tvTitle.setText(name);
    }


    @Override
    public void setBtnText(String text, boolean isSelected) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(14, true), 4, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        preBathroom.setText(spannable);
        this.isSelected = isSelected;
        if (isSelected) preBathroom.setBackgroundResource(R.drawable.green_button);
        else preBathroom.setBackgroundResource(R.drawable.red_button);
    }

    @Override
    public void trafficInfo(List<BuildingTrafficDTO.FloorsBean> floorsBeans) {
        if (pop == null) {
            pop = new ChooseBathroomPop(this);
            pop.setPopButtonClickListener(new ChooseBathroomPop.PopButtonClickListener() {
                @Override
                public void click(BuildingTrafficDTO.FloorsBean floorsBean) {
                    if (floorsBean != null) {
                        if (!TextUtils.isEmpty(floorsBean.getDeviceNo())) {
                            presenter.booking(Long.parseLong(floorsBean.getDeviceNo()), 0);
                        } else {
                            presenter.booking(0, floorsBean.getId());
                        }
                    }
                }
            });
        }
        pop.setData(floorsBeans);
        showPopNoDevice();
    }


    @Override
    public void startQueue(long id) {
        this.queueId = id;
        Log.e(TAG, "startQueue: "   + id);
        bathroomBookingDialog.setFinish();
//        if (isCanJump) {
//            startActivityForResult(new Intent(this, BookingActivity.class)
//                    .putExtra(KEY_BATHQUEUE_ID, id)
//                    .putExtra(KEY_BALANCE, balance)
//                    .putExtra(KEY_MIN_PREPAY, minPrepay)
//                    .putExtra(KEY_PREPAY, prepay)
//                    .putExtra(KEY_MISSED_TIMES, missedTimes)
//                    .putExtra(KEY_MAX_MISSABLE_TIMES, maxMissAbleTimes), KEY_BOOKING_RESQCODE);
//        }
    }

    @Override
    public void startBooking(long bathOrderId) {
        this.bookingId = bathOrderId;
        Log.e(TAG, "startBooking: ");
        bathroomBookingDialog.setFinish();
    }


    @Override
    public void saveBookingInfo(BathOrderPreconditionRespDTO data) {
        balance = data.getPrepayInfo().getBalance();
        minPrepay = data.getPrepayInfo().getMinPrepay();
        prepay = data.getPrepayInfo().getPrepay();
        maxMissAbleTimes = data.getMaxMissAbleTimes();
        missedTimes = data.getMissedTimes();
        setPreBtnText(data.getPrepayInfo());
    }


    private void setPreBtnText(BathOrderPreconditionRespDTO.PrepayInfoBean prepayInfo) {
        if (prepayInfo.getBalance() < prepayInfo.getMinPrepay()) {
            preBathroom.setText("余额不足，请前往充值");
            needCharge = true;
        } else {
            preBathroom.setText(getString(R.string.preBathWithAnyEmptyBath));
            needCharge = false;
        }

    }

    @Override
    public void startUsing(long bathOrderId) {
        this.bathOrderId = bathOrderId;
        if (bathroomBookingDialog != null) bathroomBookingDialog.setFinish();
        Log.e(TAG, "startUsing: ");
    }

    @Override
    public void startOrderInfo(BathOrderCurrentRespDTO data) {
        if (bathroomBookingDialog != null) this.bathOrderCurrentRespDTO = data;
        Log.e(TAG, "startOrderInfo: ");
    }

    @Override
    public void showError() {
        rlError.setVisibility(View.VISIBLE);
        idContent.setVisibility(View.GONE);
        setTvTitle("迷失");
        tvTitle.setCompoundDrawables(null , null , null , null);
        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getBathroomList(buildId);
            }
        });

    }


    @OnClick(R.id.pre_bathroom)
    public void preBathRoom() {
        preBathroom.setClickable(false);
        if (needCharge) {
            setBtnText("余额不足，请前往充值" , false);
            startActivity(new Intent(getApplicationContext(), RechargeActivity.class));
            preBathroom.setClickable(true);
        } else {
            if (TextUtils.isEmpty(deviceNo)) {
                showPop();

            } else {
                autoShowDevice(deviceNo);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case KEY_BOOKING_RESQCODE:
                if (resultCode == KEY_BOOKING_RESULTCODE) {
                    autoChoseBathroom(data);
                }
                break;
        }
    }

    /**
     * 重新预约时，自动帮用户选择
     */
    private void autoChoseBathroom(Intent data) {
        isShowDialog = false;
        String deviceNo = data.getStringExtra(KEY_DEVICE_ACTIVITY_FOR_RESULT);
        if (TextUtils.isEmpty(deviceNo)) {
            showPop();
        } else {
            autoShowDevice(deviceNo);
//            showPopForDevice();
        }

    }

    private void autoShowDevice(String deviceNo) {
        if (floorsBeans != null && floorsBeans.size() > 0) {

            for (BathBuildingRespDTO.FloorsBean floorsBean : floorsBeans) {
                for (GroupsBean groupsBean : floorsBean.getGroups()) {

                    for (GroupsBean.BathRoomsBean bathRoomsBean : groupsBean.getBathRooms()) {
                        if (deviceNo.equals(bathRoomsBean.getDeviceNo()+"")) {
                            this.deviceNo = bathRoomsBean.getDeviceNo() + "";
                            showPop(bathRoomsBean.getName(), bathRoomsBean.getDeviceNo() + "");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void BathroomClick(GroupsBean.BathRoomsBean bathRoomsBean) {
        if (null == bathRoomsBean) {
            this.deviceNo = "";
        } else {
            showPop(bathRoomsBean.getName(), bathRoomsBean.getDeviceNo() + "");

        }

    }

    @Override
    public void onScale(float scale) {

    }

    @Override
    public void finishDialog() {
        if (bathroomBookingDialog != null && bathroomBookingDialog.isShowing()) bathroomBookingDialog.dismiss();
        if (bookingId != 0) {
            startActivityForResult(new Intent(this, BookingActivity.class)
                    .putExtra(KEY_BOOKING_ID, bookingId)
                    .putExtra(KEY_BALANCE, balance)
                    .putExtra(KEY_MIN_PREPAY, minPrepay)
                    .putExtra(KEY_PREPAY, prepay)
                    .putExtra(KEY_MISSED_TIMES, missedTimes)
                    .putExtra(KEY_MAX_MISSABLE_TIMES, maxMissAbleTimes), KEY_BOOKING_RESQCODE
            );
        } else if (queueId != 0) {
            if (isCanJump) {
                startActivityForResult(new Intent(this, BookingActivity.class)
                        .putExtra(KEY_BATHQUEUE_ID, queueId)
                        .putExtra(KEY_BALANCE, balance)
                        .putExtra(KEY_MIN_PREPAY, minPrepay)
                        .putExtra(KEY_PREPAY, prepay)
                        .putExtra(KEY_MISSED_TIMES, missedTimes)
                        .putExtra(KEY_MAX_MISSABLE_TIMES, maxMissAbleTimes), KEY_BOOKING_RESQCODE);
            }
        } else if (bathOrderId != 0) {
            if (isCanJump) {
                startActivity(new Intent(this, BathroomHeaterActivity.class)
                        .putExtra(KEY_BATH_ORDER_ID, bathOrderId));
            }
        } else if (bathOrderCurrentRespDTO != null) {

            String userMethod = "";
            if (bathOrderCurrentRespDTO.getLocation().equals("任意空浴室")) {
                userMethod = "预约任意空浴室";
            } else {
                userMethod = "预约指定浴室";
            }
            Intent intent = new Intent(this, BathOrderActivity.class);
            intent.putExtra(Constant.BUNDLE_ID, bathOrderCurrentRespDTO.getTradeOrderId());
            intent.putExtra(KEY_USER_STYLE, userMethod);
            if (isCanJump) {
                startActivity(intent);
                isCanJump = false ;
            }

        }
    }

}
