package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xiaolian.amigo.ui.user.FindBathroomPasswordActivity;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.ui.widget.AutoBathroom;
import com.xiaolian.amigo.ui.widget.CircleProgressView;
import com.xiaolian.amigo.ui.widget.dialog.BathroomBookingDialog;
import com.xiaolian.amigo.ui.widget.popWindow.ChooseBathroomPop;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.DimentionUtils;
import com.xiaolian.amigo.util.H5StartUtils;
import com.xiaolian.amigo.util.RxHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.xiaolian.amigo.ui.device.DeviceOrderActivity.KEY_USER_STYLE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BALANCE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_FLOOR;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MAX_MISSABLE_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MIN_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MISSED_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomHeaterActivity.KEY_BATH_ORDER_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BookingActivity.KEY_BATHQUEUE_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BookingActivity.KEY_BOOKING_ID;
import static com.xiaolian.amigo.util.Constant.FROM_LOCATION;

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


    @Inject
    H5StartUtils h5StartUtils ;


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_help)
    ImageView ivHelp;
    @BindView(R.id.pre_bathroom1)
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
    @BindView(R.id.ll_selected)
    LinearLayout llSelected;
    private BathroomBookingDialog bathroomBookingDialog;

    private long buildId;    //  楼栋id ；
    private long residenceId;   //  建筑id
    private int residenceType;
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
            pop.setPopButtonClickListener(floorsBean -> {
                Log.e(TAG, "click: "  );
                if (floorsBean != null) {
                    if (needCharge){
                        onError("余额不足，请前往充值");
                        startActivity(new Intent(getApplicationContext(), RechargeActivity.class)
                        .putExtra(FROM_LOCATION ,"公共浴室"));
                    }else {
                        if (!TextUtils.isEmpty(floorsBean.getDeviceNo())) {
                            if (  missedTimes < maxMissAbleTimes) {
                                presenter.booking(Long.parseLong(floorsBean.getDeviceNo()), 0);
                            }else{
                                onError("你已失约"+ maxMissAbleTimes+"次，本月将无法预约指定浴室");
                            }
                        } else {
                            presenter.booking(0, floorsBean.getId());
                        }
                    }
                }
            });
        }

    }

    private void bindView() {
        setBtnText("预约洗澡 (任意空浴室)", false);
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        isShowDialog = true;
        autoBathroom.setBathroomClick(this);
    }


    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed: " );
        startActivity(new Intent(this, MainActivity.class));
    }


    /**
     * 获取帮助
     */
    private void help() {
        h5StartUtils.startH5Service();
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
        if (autoBathroom != null && presenter != null)
            autoBathroom.setPresenter((ChooseBathroomPresenter<IChooseBathroomView>) presenter);
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

        this.deviceNo = deviceNo + "";
    }


    boolean isBookingBack = false ;
    @Override
    protected void onResume() {
        super.onResume();
        isCanJump = true;
        bookingId = 0;
        queueId = 0;
        bathOrderId = 0;
        isNeedSwipe = false ;
        bathOrderCurrentRespDTO = null;
        presenter.onResume();
        presenter.setIsResume(true);
        presenter.getBathroomList(buildId);
        presenter.precondition(isShowDialog, true);
        if (preBathroom != null) preBathroom.setEnabled(true);
        isBookingBack = false ;
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            buildId = getIntent().getLongExtra(KEY_BUILDING_ID, -1L);
            residenceId = getIntent().getLongExtra(KEY_RESIDENCE_ID, -1L);
            residenceType = getIntent().getIntExtra(KEY_RESIDENCE_TYPE, -1);
            id = getIntent().getLongExtra(KEY_ID, -1L);
            residenceName = getIntent().getStringExtra(KEY_RESIDENCE_NAME);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCanJump = false;
        presenter.setIsResume(false);
        presenter.onPause();
        if (bathroomBookingDialog != null && bathroomBookingDialog.isShowing()){
            bathroomBookingDialog.dismiss();
        }

        if (pop != null && pop.isShowing()){
            pop.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        if (bathroomBookingDialog != null) {
            bathroomBookingDialog.onDettechView();
        }
        bathroomBookingDialog = null ;
        presenter.clearTime();
        isNeedSwipe = true ;
    }


    private void showPopForDevice(BuildingTrafficDTO.FloorsBean floorsBean) {
        if (pop == null) {
            return ;
        }
        pop.setData(floorsBean );

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
        preBathroom.setEnabled(false);
        pop.showUp(preBathroom);
        lightOff();

        /**
         * 消失时屏幕变亮
         */
        pop.setOnDismissListener(() -> {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

            layoutParams.alpha = 1.0f;

            getWindow().setAttributes(layoutParams);

            presenter.setIsResume(true);
            deviceNo = "";
            if (autoBathroom != null) {
                autoBathroom.setBathroomAvable();
                autoBathroom.invalidate();
            }
            Log.e(TAG, "onDismiss: ");
            preBathroom.setEnabled(true);

        });
    }


    /**
     * 显示时屏幕变暗
     */
    private void lightOff() {

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        layoutParams.alpha = 0.7f;

        getWindow().setAttributes(layoutParams);

    }

    boolean isQueen  ;


    @Override
    public void refreshBathroom(BathBuildingRespDTO respDTO) {
        boolean isPublicBathroom = false;
        isQueen = false ;
        viewLine.setVisibility(View.VISIBLE);
        idContent.setVisibility(View.VISIBLE);
        rlError.setVisibility(View.GONE);
        if (respDTO.getMethods() != null && respDTO.getMethods().size() > 0) {
            for (Integer method : respDTO.getMethods()) {
                if (method == 1) isPublicBathroom = true;

                if (method == 3) isQueen = true ;
            }
        }

        if (isPublicBathroom){
            llSelected.setVisibility(View.VISIBLE);
        }else{
            llSelected.setVisibility(View.GONE);
        }
            if (floorsBeans == null) {
                floorsBeans = respDTO.getFloors();
            } else {
                floorsBeans.clear();
                floorsBeans.addAll(respDTO.getFloors());
            }
        autoBathroom.setData(floorsBeans , residenceId);
        autoBathroom.setIsSelect(isPublicBathroom);
        if (pop != null) pop.setQueen(isQueen);
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
        tvTitle.setText(residenceName);
    }


    @Override
    public void setBtnText(String text, boolean isSelected) {
        Spannable spannable = new SpannableString(text);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        spannable.setSpan(new AbsoluteSizeSpan(DimentionUtils.convertSpToPixels(16, this)), 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(DimentionUtils.convertSpToPixels(12, this)), 4, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(spannable);
        if (preBathroom != null) {
            preBathroom.setText(builder);
            preBathroom.setEnabled(true);
        }

    }

    @Override
    public void trafficInfo(List<BuildingTrafficDTO.FloorsBean> floorsBeans) {
        if (pop == null) {
            return  ;
        }
        pop.setData(floorsBeans);
        showPopNoDevice();
    }


    @Override
    public void startQueue(long id) {
        this.queueId = id;
        this.isFloor = false ;
        bathroomBookingDialog.setFinish();
    }

    boolean isFloor;

    @Override
    public void startBooking(long bathOrderId, boolean isFloor) {
        this.bookingId = bathOrderId;
        this.isFloor = isFloor;
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
//            preBathroom.setText("余额不足，请前往充值");
            needCharge = true;
            setBtnText("预约洗澡(任意空浴室)", false);
        } else {
            setBtnText("预约洗澡(任意空浴室)", false);
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
        viewLine.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);
        idContent.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(residenceName))  setTvTitle(residenceName);
//        setTvTitle("迷失");
        tvTitle.setCompoundDrawables(null, null, null, null);
        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getBathroomList(buildId);
            }
        });

    }

    @Override
    public void setPrebathroomEnable(boolean isEnable) {
        preBathroom.setEnabled(isEnable);
    }

    @Override
    public void startToFindBathroomFindPassword() {
        startActivity(new Intent(this , FindBathroomPasswordActivity.class));
    }

    @Override
    public void setBtnEnable() {
        pop.setBtnEnable();
    }


    @OnClick(R.id.pre_bathroom1)
    public void preBathRoom() {
        Log.e(TAG, "preBathRoom: ");
        preBathroom.setEnabled(false);
        if (needCharge) {
//            setBtnText("余额不足，请前往充值" , false);
            onError("余额不足，请前往充值");
            startActivity(new Intent(getApplicationContext(), RechargeActivity.class)
            .putExtra(FROM_LOCATION ,"公共浴室"));
        } else {
            if (TextUtils.isEmpty(deviceNo)) {

                showPop();

            } else {
//                autoShowDevice(deviceNo);
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case KEY_BOOKING_RESQCODE:
                if (resultCode == KEY_BOOKING_RESULTCODE) {
                    isBookingBack = true ;
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
        if (presenter.getBookMehtod() == Constant.BOOKING_FLOOR ){
            showPop();
        } else {
        }

    }


    @Override
    public void BathroomClick(GroupsBean.BathRoomsBean bathRoomsBean) {
        if (null == bathRoomsBean) {
            this.deviceNo = "";
        } else {
            showPop(bathRoomsBean.getRoomName(), bathRoomsBean.getDeviceNo() + "");
        }
    }

    @Override
    public void onScale(float scale) {

    }

    @Override
    public void finishDialog() {
        if (bathroomBookingDialog != null && bathroomBookingDialog.isShowing())
            bathroomBookingDialog.dismiss();
        if (bookingId != 0) {
            Log.e(TAG, "finishDialog:  bookingId >>>>>" + bookingId );
            startActivityForResult(new Intent(this, BookingActivity.class)
                    .putExtra(KEY_BOOKING_ID, bookingId)
                    .putExtra(KEY_BALANCE, balance)
                    .putExtra(KEY_MIN_PREPAY, minPrepay)
                    .putExtra(KEY_PREPAY, prepay)
                    .putExtra(KEY_FLOOR, this.isFloor)
                    .putExtra(KEY_MISSED_TIMES, missedTimes)
                    .putExtra(KEY_MAX_MISSABLE_TIMES, maxMissAbleTimes), KEY_BOOKING_RESQCODE
            );
        } else if (queueId != 0) {
            if (isCanJump) {
                Log.e(TAG, "finishDialog: queueId >>>>>> " + queueId );
                startActivityForResult(new Intent(this, BookingActivity.class)
                        .putExtra(KEY_BATHQUEUE_ID, queueId)
                        .putExtra(KEY_FLOOR ,this.isFloor)
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
            if (presenter.getBookMehtod() == Constant.BOOKING_FLOOR) {
                userMethod = "预约任意空浴室";
            } else {
                userMethod = "预约指定浴室";
            }
            Intent intent = new Intent(this, BathOrderActivity.class);
            intent.putExtra(Constant.BUNDLE_ID, bathOrderCurrentRespDTO.getTradeOrderId());
            intent.putExtra(KEY_USER_STYLE, userMethod);
            if (isCanJump) {
                startActivity(intent);
                isCanJump = false;
            }

        }
        setBtnEnable();
    }




    @Override
    public void finish() {
        super.finish();
        onBackPressed();
    }
}
