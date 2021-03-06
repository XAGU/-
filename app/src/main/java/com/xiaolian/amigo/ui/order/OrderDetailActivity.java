package com.xiaolian.amigo.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.order.intf.IOrderDetailPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderDetailView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.H5StartUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaolian.amigo.ui.base.WebActivity.INTENT_KEY_URL;
import static com.xiaolian.amigo.util.Log.getContext;

/**
 * 消费账单详情
 *
 * @author caidong
 * @date 17/9/18
 */
public class OrderDetailActivity extends OrderBaseActivity implements IOrderDetailView {
    /**
     * 账单异常
     */
    public static final int ORDER_ERROR_STATUS = 3;
    @Inject
    IOrderDetailPresenter<IOrderDetailView> presenter;

    @Inject
    H5StartUtils h5StartUtils ;
    /**
     * 账单标题
     */
    @BindView(R.id.tv_order_title)
    TextView tvOrderTitle;
    /************** 正常账单内容 ****************/
    @BindView(R.id.ll_order_normal)
    LinearLayout llOrderNormal;
    @BindView(R.id.rl_use_bonus)
    RelativeLayout rlUseBonus;
    /**
     * 代金券抵扣
     */
    @BindView(R.id.tv_bonus_remark)
    TextView tvBonusRemark;
    /**
     * 代金券标题
     */
    @BindView(R.id.tv_bonus_title)
    TextView tvBonusTitle;
    /**
     * 预付金额
     */
    @BindView(R.id.tv_prepay)
    TextView tvPrepay;
    /**
     * 实际扣款
     */
    @BindView(R.id.tv_actual_debit)
    TextView tvActualDebit;
    @BindView(R.id.rl_actual_debit)
    RelativeLayout rlActualDebit;
    /**
     * 实际消费
     */
    @BindView(R.id.tv_consume)
    TextView tvConsume;
    /**
     * 找零金额
     */
    @BindView(R.id.tv_odd)
    TextView tvOdd;


    @BindView(R.id.rl_odd)
    RelativeLayout rlOdd ;
    /************** 异常账单内容 ****************/
    @BindView(R.id.tv_order_error_tip)
    TextView tvOrderErrorTip;
    @BindView(R.id.ll_order_error)
    LinearLayout llOrderError;
    @BindView(R.id.rl_back_bonus)
    RelativeLayout rlBackBonus;
    /**
     * 退还代金券
     */
    @BindView(R.id.tv_back_bonus)
    TextView tvBackBonus;
    /**
     * 退还金额
     */
    @BindView(R.id.tv_back_amount)
    TextView tvBackAmount;

    /************** 基础信息 *****************/
    /**
     * 使用时间
     */
    @BindView(R.id.tv_time)
    TextView tvTime;

    /**
     * 结束时间
     */
    @BindView(R.id.tv_time_end)
    TextView tvTimeEnd;

    /**
     * 使用方式
     */
    @BindView(R.id.rl_user_style)
    RelativeLayout rlUserStyle ;

    @BindView(R.id.rl_prepay)
    RelativeLayout rlPrepay ;

    /**
     * 使用时长
     */
    @BindView(R.id.tv_used_time)
    TextView tvUsedTime;
    @BindView(R.id.rl_used_time)
    RelativeLayout rlUsedTime;
    /**
     * 设备位置
     */
    @BindView(R.id.tv_device_location)
    TextView tvDeviceLocation;
    /**
     * 订单号
     */
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    /**
     * 是否最低消费
     */
    @BindView(R.id.tv_order_no_use_tip)
    TextView tvOrderNoUseTip;

    /******************** 底部线条 **********************/
    @BindView(R.id.v_bottom_line1)
    View vBottomLine1;
    @BindView(R.id.v_bottom_line2)
    View vBottomLine2;
    @BindView(R.id.finish_time_rl)
    RelativeLayout finishTimeRl;

    @BindView(R.id.tv_code)
    TextView tvCode ;

    @BindView(R.id.rl_code)
    RelativeLayout rlCode ;


    @BindView(R.id.ll_bottom)
    LinearLayout llBottom ;

    @BindView(R.id.left_oper)
    TextView leftOper ;

    @BindView(R.id.right_oper)
    TextView rightOper ;

    @BindView(R.id.ll_line)
    View llLine ;

    private Long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        setUp();
        presenter.onAttach(this);
        presenter.getOrder(orderId);
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        if (getIntent().getStringExtra(OrderConstant.KEY_ORDER_TITLE) != null) {
            tvOrderTitle.setText(getIntent().getStringExtra(OrderConstant.KEY_ORDER_TITLE));
        }
        this.orderId = intent.getLongExtra(Constant.EXTRA_KEY, -1);
    }

    /**
     * 投诉
     */
    @OnClick(R.id.tv_complaint)
    public void complaint() {
        presenter.checkComplaint();
    }

    /**
     * 复制
     */
    @OnClick(R.id.tv_copy)
    public void copy() {
        CommonUtil.copy(tvOrderNo.getText().toString(), getApplicationContext());
        onSuccess(R.string.copy_success);
    }
    
    @Override
    public void showNoUseTip() {
        tvOrderNoUseTip.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOnErrorTip(OrderDetailRespDTO order) {
        tvOrderErrorTip.setVisibility(View.VISIBLE);
        // 异常账单
        // 设置服务器返回的文案
        if (!TextUtils.isEmpty(order.getZeroConsumeCopy())) {
            tvOrderErrorTip.setText(order.getZeroConsumeCopy());
        }
    }

    @OnClick(R.id.tv_order_no_use_tip)
    public void toNoUseHelp() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_NO_USE_HELP));
    }

    @Override
    public void toComplaint() {
        OrderDetailRespDTO order = presenter.getOrder();
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT
                        + "?accessToken=" + presenter.getAccessToken()
                        + "&refreshToken=" + presenter.getRefreshToken()
                        + "&orderId=" + order.getId()
                        + "&orderNo=" + order.getOrderNo()
                        + "&orderType="
                        + ComplaintType.getComplaintTypeByDeviceType(
                        Device.getDevice(order.getDeviceType())).getType()));
    }

    @Override
    public void renderView(OrderDetailRespDTO order) {
        // 设置基础信息
        tvTime.setText(CommonUtil.stampToDate(order.getCreateTime()));
        if (order.getFinishTime() != null) {
            tvTimeEnd.setText(CommonUtil.stampToDate(order.getFinishTime()));
            finishTimeRl.setVisibility(View.VISIBLE);
        }else{
            finishTimeRl.setVisibility(View.GONE);
        }

        tvDeviceLocation.setText(String.format("%s %s",
                Device.getDevice(order.getDeviceType()).getDesc(), order.getLocation()));
        vBottomLine1.setBackgroundColor(Color.parseColor(getLineColorByDeviceType(order.getDeviceType())));
        vBottomLine1.setVisibility(View.VISIBLE);
        vBottomLine2.setBackgroundColor(Color.parseColor(getLineColorByDeviceType(order.getDeviceType())));
        vBottomLine2.setVisibility(View.VISIBLE);
        if (Device.getDevice(order.getDeviceType()) == Device.DRYER) {
            rlUsedTime.setVisibility(View.VISIBLE);
            tvUsedTime.setText(order.getUseTime());
        }
        tvOrderNo.setText(order.getOrderNo());
        if (ObjectsCompat.equals(order.getStatus(), ORDER_ERROR_STATUS)) {

            llOrderNormal.setVisibility(View.GONE);
            llOrderError.setVisibility(View.VISIBLE);
            // 是否有代金券
            if (TextUtils.isEmpty(order.getBonus())) {
                // 没有代金券
                rlBackBonus.setVisibility(View.GONE);
                tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                tvActualDebit.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                rlActualDebit.setVisibility(View.GONE);
            } else {
                // 有代金券
                rlBackBonus.setVisibility(View.VISIBLE);
                tvBackBonus.setText(order.getBonus());
                tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tvActualDebit.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
            }
            if ( order.getHasPrepay()){
                tvBackAmount.setText(order.getPrepay());
            }else{
                tvBackAmount.setText(order.getActualDebit());
            }

        } else {
            // 正常账单
            tvOrderErrorTip.setVisibility(View.GONE);
            llOrderNormal.setVisibility(View.VISIBLE);
            llOrderError.setVisibility(View.GONE);
            // 是否有代金券
            if (TextUtils.isEmpty(order.getBonus())) {
                // 没有代金券
                rlUseBonus.setVisibility(View.GONE);
                tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                tvActualDebit.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                rlActualDebit.setVisibility(View.GONE);
            } else {
                // 有代金券
                rlUseBonus.setVisibility(View.VISIBLE);
                tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tvActualDebit.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                // 实际消费为0
                if (TextUtils.equals(order.getConsume(), "¥0")) {
                    rlActualDebit.setVisibility(View.GONE);
                    tvBonusTitle.setText("退还代金券：");
                    tvBonusRemark.setText(order.getBonus());
                    tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                } else {
                    tvBonusRemark.setText(getString(R.string.minus, order.getBonus()));
                }
            }
            tvConsume.setText(order.getConsume());
            tvPrepay.setText(order.getPrepay());
            tvOdd.setText(order.getOdd());
            tvActualDebit.setText(getString(R.string.minus, order.getActualDebit()));


            if (order.getDeviceType() == Device.DRYER.getType() && !ObjectsCompat.equals(order.getStatus(), ORDER_ERROR_STATUS) && order.getScanCode() != null && order.getScanCode()){
                showCode(order.getQrCode());
                /**
                 * 隐藏预付信息
                 */
//                onlyShowBottomRight();
//                rightOper.setText("常见问题");
//                rightOper.setOnClickListener(v -> {
//                    startServiceH5();
//                });
            } else {
                hideCode();
            }

            if ( !order.getHasPrepay()){
                rlPrepay.setVisibility(View.GONE);
                rlOdd.setVisibility(View.GONE);
            }
        }
    }

    //  跳服务中心h5页面
    public void startServiceH5() {
        h5StartUtils.startH5Service();
    }

    /**
     * 账单底部按钮仅仅只显示一种的情况
     */
    private void onlyShowBottomRight(){
        llBottom.setVisibility(View.VISIBLE);
        leftOper.setVisibility(View.GONE);
        rightOper.setVisibility(View.VISIBLE);
        llLine.setVisibility(View.GONE);
    }

    /**
     * 账单详情底部按钮显示两种的情况
     */
    private void showBottomAll(){
        llBottom.setVisibility(View.VISIBLE);
        leftOper.setVisibility(View.GONE);
        rightOper.setVisibility(View.GONE);
        llLine.setVisibility(View.VISIBLE);
    }

    private void showCode(String code){
        tvCode.setText(code);
        rlCode.setVisibility(View.VISIBLE);
    }

    private void hideCode(){
        tvCode.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    public static String getLineColorByDeviceType(Integer deviceType) {
        if (deviceType == null) {
            return "#21FF4E80";
        }
        switch (Device.getDevice(deviceType)) {
            case DISPENSER:
                return "#3322D4CB";
            case WASHER:
                return "#21499BFF";
            case HEATER:
                return "#21FF4E80";
            case DRYER:
                return "#33F8B646";
            case DRYER2:
                return "#E3E3FF";
        }
        return "#21FF4E80";
    }

}
