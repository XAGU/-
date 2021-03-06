package com.xiaolian.amigo.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.washer.WasherContent;
import com.xiaolian.amigo.ui.device.washer.WasherQrCodeActivity;
import com.xiaolian.amigo.ui.order.intf.INormalOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.INormalOrderView;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentCopyDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentListDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawRechargeDetailAdapter;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.H5StartUtils;
import com.xiaolian.amigo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiaolian.amigo.ui.device.washer.ScanActivity.KEY_TYPE;

/**
 * 消费账单
 *
 * @author zcd
 * @date 17/10/23
 */

public class NormalOrderActivity extends OrderBaseActivity implements INormalOrderView {
    private static final int ORDER_ERROR_STATUS = 3;
    @Inject
    INormalOrderPresenter<INormalOrderView> presenter;

    @Inject
    H5StartUtils h5StartUtils ;

    private List<WithdrawRechargeDetailAdapter.Item> items = new ArrayList<>();

    private WithdrawRechargeDetailAdapter adapter;

    private OrderDetailRespDTO data ;

    private RecyclerView recyclerView;
    private LinearLayout llBottom;
    private TextView tvBottomTip;
    private TextView tv_order_title;

    /************** 异常账单内容 ****************/
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

    /******************** 底部线条 **********************/
    View vBottomLine1;
    View vBottomLine2;
    private LinearLayout llTip ;
    private TextView right_oper;

    Unbinder unbinder ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_order);
        unbinder = ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        bindView();
        setUp();
        initView();
    }

    private void bindView() {
        findViewById(R.id.left_oper).setOnClickListener(v -> onLeftOper());
        findViewById(R.id.right_oper).setOnClickListener(v -> onRightOper());
        findViewById(R.id.tv_complaint).setOnClickListener(v -> complaint());
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        vBottomLine1 = findViewById(R.id.v_bottom_line1);
        vBottomLine2 = findViewById(R.id.v_bottom_line2);
        recyclerView = findViewById(R.id.recyclerView);
        llBottom = findViewById(R.id.ll_bottom);
        tvBottomTip = findViewById(R.id.tv_bottom_tip);
        right_oper = findViewById(R.id.right_oper);
        llTip = findViewById(R.id.ll_tip);
        tv_order_title = findViewById(R.id.tv_order_title);
    }

    private void complaint() {
        presenter.checkComplaint();
    }

    private void initView() {
        adapter = new WithdrawRechargeDetailAdapter(this, items);
        TitleContentCopyDelegate contentCopyDelegate = new TitleContentCopyDelegate();
        contentCopyDelegate.setCopyListener((string -> {
            CommonUtil.copy(string, getApplicationContext());
            onSuccess(R.string.copy_success);
        }));
        adapter.addItemViewDelegate(contentCopyDelegate);
        adapter.addItemViewDelegate(new TitleContentDelegate());
        adapter.addItemViewDelegate(new TitleContentListDelegate(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        presenter.requestOrder();
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            tv_order_title.setText(getIntent().getStringExtra(OrderConstant.KEY_ORDER_TITLE));
            presenter.setOrderId(getIntent().getLongExtra(OrderConstant.KEY_ORDER_ID, Constant.INVALID_ID));
        }
    }

    @Override
    public void toComplaint() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, presenter.getComplaintParam()));
    }

    public void onLeftOper() {
        h5StartUtils.startH5Service();
    }

    public void onRightOper() {
        startActivity(new Intent(this, WasherQrCodeActivity.class)
                .putExtra(WasherContent.KEY_PRICE, presenter.getOrder().getConsume())
                .putExtra(WasherContent.KEY_MODE_DESC, presenter.getOrder().getModeDesc())
                .putExtra(WasherContent.KEY_QR_CODE_URL, presenter.getOrder().getQrCode())
                .putExtra(KEY_TYPE ,data.getDeviceType())
                .setAction("normalOrder"));
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void renderView(OrderDetailRespDTO data) {
        this.data = data ;
        items.add(new WithdrawRechargeDetailAdapter.Item("下单时间：",
                TimeUtils.millis2String(data.getCreateTime()), WithdrawRechargeDetailAdapter.TITLE_CONTENT_TYPE));
        if (data.getFinishTime() != null) {
            items.add(new WithdrawRechargeDetailAdapter.Item("结束时间：",
                    TimeUtils.millis2String(data.getFinishTime()), WithdrawRechargeDetailAdapter.TITLE_CONTENT_TYPE));
        }
        items.add(new WithdrawRechargeDetailAdapter.Item("设备信息：",
                String.format("%s %s",
                        Device.getDevice(data.getDeviceType()).getDesc(),
                        data.getLocation()), WithdrawRechargeDetailAdapter.TITLE_CONTENT_TYPE));
        items.add(new WithdrawRechargeDetailAdapter.Item("订单号：",
                data.getOrderNo(), WithdrawRechargeDetailAdapter.TITLE_CONTENT_COPY_TYPE));
        if ((Device.getDevice(data.getDeviceType()) == Device.WASHER   && data.getConsume() != null)) {
            items.add(new WithdrawRechargeDetailAdapter.Item("洗衣模式：",
                    String.format("%s %s元", data.getModeDesc(), data.getConsume().replace("¥", "")), WithdrawRechargeDetailAdapter.TITLE_CONTENT_TYPE));
        }

        if ((Device.getDevice(data.getDeviceType()) == Device.DRYER2 && data.getConsume() != null)){
            items.add(new WithdrawRechargeDetailAdapter.Item("烘干模式：" ,
                    String.format("%s %s元" ,data.getModeDesc() , data.getConsume().replace("¥" ,"")),WithdrawRechargeDetailAdapter.TITLE_CONTENT_TYPE));
        }
        List<TitleContentListDelegate.ListItem> listItems = new ArrayList<>();
        if (ObjectsCompat.equals(data.getStatus(), ORDER_ERROR_STATUS)) {
//            llTip.setVisibility(View.VISIBLE);
            llOrderError.setVisibility(View.VISIBLE);
            // 异常账单
            // 是否有代金券
            if (TextUtils.isEmpty(data.getBonus())) {
                // 没有代金券
                rlBackBonus.setVisibility(View.GONE);
            } else {
                // 有代金券
                rlBackBonus.setVisibility(View.VISIBLE);
                tvBackBonus.setText(data.getBonus());
            }
            if ( data.getHasPrepay()){
                tvBackAmount.setText(data.getPrepay());
            }else{
                tvBackAmount.setText(data.getActualDebit());
            }
            adapter.notifyDataSetChanged();
            setBottomLayout(data);

        } else {
            // 正常账单
            // 是否有代金券
//            llTip.setVisibility(View.GONE);
            llOrderError.setVerticalGravity(View.GONE);
            if (TextUtils.isEmpty(data.getBonus())) {
                // 没有代金券
                listItems.add(new TitleContentListDelegate.ListItem("实际消费：", data.getConsume(), "#ff5555"));
            } else {
                // 有代金券
                listItems.add(new TitleContentListDelegate.ListItem("代金券抵扣：", "-" + data.getBonus(), "#222222"));
                listItems.add(new TitleContentListDelegate.ListItem("实际扣款：", data.getActualDebit(), "#ff5555"));
            }
            items.add(new TitleContentListDelegate.TitleContentListItem(listItems, WithdrawRechargeDetailAdapter.TITLE_CONTENT_LIST_TYPE));
            adapter.notifyDataSetChanged();
            if ((data.getDeviceType() == Device.DRYER2.getType()
            || data.getDeviceType() == Device.WASHER.getType()) && data.isNetWashing()) llBottom.setVisibility(View.GONE);
            else setBottomLayout(data);
        }

    }

    private void setBottomLayout(OrderDetailRespDTO data) {
        Log.e(TAG, "setBottomLayout: " + data.isNetWashing()  );
        llBottom.setVisibility(View.VISIBLE);
        vBottomLine1.setBackgroundColor(Color.parseColor(OrderDetailActivity.getLineColorByDeviceType(data.getDeviceType())));
        vBottomLine1.setVisibility(View.VISIBLE);
        vBottomLine2.setBackgroundColor(Color.parseColor(OrderDetailActivity.getLineColorByDeviceType(data.getDeviceType())));
        vBottomLine2.setVisibility(View.VISIBLE);

        // 洗衣机
        if (Device.getDevice(data.getDeviceType()) == Device.WASHER || Device.getDevice(data.getDeviceType()) == Device.DRYER2 ) {
            if (ObjectsCompat.equals(data.getStatus(), ORDER_ERROR_STATUS)) {
                // 异常订单

                tvBottomTip.setVisibility(View.VISIBLE);
                // 设置服务器返回的文案
                if (!TextUtils.isEmpty(data.getZeroConsumeCopy())) {
                    tvBottomTip.setText(data.getZeroConsumeCopy());
                } else {
                    tvBottomTip.setText(getString(R.string.washer_order_error_tip));
                }
                tvBottomTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                tvBottomTip.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));

                /**
                 * 退款的不显示底部
                 */
                llBottom.setVisibility(View.GONE);
            } else {
                if (data.isNetWashing()) {
                    llBottom.setVisibility(View.GONE);
                }
            }
        } else {
            if (ObjectsCompat.equals(data.getStatus(), ORDER_ERROR_STATUS)) {
                // 异常订单
                tvBottomTip.setVisibility(View.VISIBLE);
                // 设置服务器返回的文案
                if (!TextUtils.isEmpty(data.getZeroConsumeCopy())) {
                    tvBottomTip.setText(data.getZeroConsumeCopy());
                } else {
                    tvBottomTip.setText(getString(R.string.order_error_tip));
                }
                tvBottomTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                tvBottomTip.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
            } else {
                // 正常订单
                if (TextUtils.isEmpty(data.getBonus()) && data.getLowest() != null && data.getLowest()) {
                    tvBottomTip.setVisibility(View.VISIBLE);
                    tvBottomTip.setText(getString(R.string.order_no_use_tip));
                    tvBottomTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    tvBottomTip.setTextColor(ContextCompat.getColor(this, R.color.colorDark9));
                }
            }
        }
    }
}
