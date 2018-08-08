package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.BathroomOperationStatus;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.device.bathroom.adapter.DeviceInfoAdapter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodeView;
import com.xiaolian.amigo.ui.order.OrderDetailActivity;
import com.xiaolian.amigo.ui.widget.BathroomOperationStatusView;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_ORDER_PRECONDITION;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_CANCEL;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_FAIL;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_OPERATING;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_SUCCESS;
import static com.xiaolian.amigo.util.PayUtil.CANCEL;

/**
 * 购买编码
 * @author zcd
 * @date 18/6/29
 */
public class BuyCodeActivity extends UseWayActivity implements IBuyCodeView {
    private final static String TAG = BuyCodeActivity.class.getSimpleName();

    private Long bathOrderId ;
    private TextView timeText ;
    private BathOrderPreconditionRespDTO bathOrderPreconditionRespDTO ;
    @Inject
    IBuyCodePresenter<IBuyCodeView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        initIntent();
        if (bathOrderPreconditionRespDTO != null){
            referUiForLastOrderStatus();
        }else {
            presenter.preBuyVoucher();
        }
    }

    /**
     * 根据上一个订单状态刷新界面
     */
    private void referUiForLastOrderStatus(){
        if (bathOrderPreconditionRespDTO == null){
            return ;
        }
        bathOrderId = bathOrderPreconditionRespDTO.getBathOrderId();
        BathroomOperationStatus bathroomOperationStatus = BathroomOperationStatus.getBathroomOperationStatus(bathOrderPreconditionRespDTO.getStatus());
        switch (bathroomOperationStatus){
//            case CANCEL:
//                cancelForOldOrder(true);
//                break;
//            case PAYED:
//                buySuccessForOldOrder(true);
//                break;
//            case EXPIRED:
//                expiredForOldOrder(true);
//                break;
        }
    }


    /**
     * 从未完成订单跳转的，过期消费卷的状态
     */
    private void expiredForOldOrder(boolean isNeedChange){
        showOrderHideBtnUser();
        statusView.setStatusText("洗澡卷失效");
        statusView.setLeftImageResource(IMG_RES_STATUS_FAIL);
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = getListDevceInfoAdapter(true);
        referItems(wrapperList ,isNeedChange);
    }

    /**
     * 购买成功界面
     */
    private void buySuccessForOldOrder(boolean isNeedChange){
        buySuccessUi();
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = getListDevceInfoAdapter(true);
        referItems(wrapperList ,isNeedChange);
    }


    /**
     * 从上一个未完成订单进入的取消界面
     */
    private void cancelForOldOrder(boolean isNeedChange){
        cancelUiAllOutofRecycleView();
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = getListDevceInfoAdapter(false);
        referItems(wrapperList ,isNeedChange);
    }


    /**
     * 从未完成订单跳转的item界面
     * @Param isShowRemainTime  是否显示剩余时间
     * @return
     */
    private List<DeviceInfoAdapter.DeviceInfoWrapper> getListDevceInfoAdapter(boolean isShowRemainTime){
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = new ArrayList<>();
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
                bathOrderPreconditionRespDTO.getLocation(), R.color.colorDark2, 14, Typeface.NORMAL, false));
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("有效时间：" , getReservedTime(bathOrderPreconditionRespDTO.getCreateTime() ,bathOrderPreconditionRespDTO.getExpiredTime())
                ,R.color.colorDark2 ,14 , Typeface.NORMAL , false));
        if (isShowRemainTime) {
            wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("剩余时间：", TimeUtils.orderBathroomLastTime(bathOrderPreconditionRespDTO.getExpiredTime(), " "),
                    R.color.colorFullRed, 14, Typeface.NORMAL, false));
        }
        return wrapperList ;
    }




    @Override
    protected void setToolbarTitle(TextView textView) {
        textView.setText("洗澡卷");
    }

    @Override
    protected void setTitle(TextView textView) {
        textView.setText("洗澡卷");
    }

    @Override
    protected void setToolbarSubTitle(TextView textView) {
        textView.setText("购买记录");
        textView.setOnClickListener(v -> onSubtitleClick());
    }


    @Override
    protected void setSubTitle(TextView textView) {
        textView.setText("购买记录");
        textView.setOnClickListener(v -> onSubtitleClick());
    }


    @Override
    protected void setTips(TextView tip1, TextView tip2, TextView tip3, TextView tip4,
                           TextView tip, RelativeLayout rlTip) {
        tip1.setText(getString(R.string.buy_code_tip1));
        tip2.setText(getString(R.string.buy_code_tip2));
        tip3.setText(getString(R.string.buy_code_tip3));
        tip4.setText(getString(R.string.buy_code_tip4));
        tip.setText("编码使用说明");
    }

    @Override
    protected String getButtonText() {
        return "，确认购买";
    }

    private void onSubtitleClick() {
        startActivity(new Intent(this, BuyRecordActivity.class));
    }

    /**
     * 购买中的状态，只有购买才会有，还没有订单，所以不会有上一个订单的状态为这个状态
     * @param data
     */
    @Override
    public void preBuy(BathPreBookingRespDTO data) {
//        if (data.getBonus() != null) bonusId = data.getBonus().getId();
        prepayAmount = data.getPrepayInfo().getPrepay();
        initValue(data);
        llBottomVisible(false);
        statusView.setStatusText("购买中");
        statusView.setLeftImageResource(IMG_RES_STATUS_OPERATING);
        statusView.hideCancelButton();
        btStartToUse.setVisibility(View.VISIBLE);
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = new ArrayList<>();
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：" ,data.getLocation()
        ,R.color.colorDark2 , 14 , Typeface.NORMAL ,false));
//        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("有效时间：" , data.getReservedTime(),R.color.colorDark2 ,
//                14 , Typeface.NORMAL ,false));
        referItems(wrapperList ,false);

        setButtonText();
        btStartToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.pay(prepayAmount , bonusId);
            }
        });
    }


    @Override
    protected void initIntent() {
//        super.initIntent();
        if (getIntent() != null) {
            bathOrderPreconditionRespDTO = getIntent().getParcelableExtra(KEY_ORDER_PRECONDITION);
        }
    }

    /**
     * 设置必要值
     * @param data
     */
    private void initValue(BathPreBookingRespDTO data){
            minPrepay = data.getPrepayInfo().getMinPrepay();
            prepay = data.getPrepayInfo().getPrepay();
            balance = data.getPrepayInfo().getBalance();
    }
    @Override
    public void bookingSuccess(BathOrderRespDTO respDTO) {
        bathOrderId = respDTO.getBathOrderId();
        buySuccessUi();
        successRecyclerView(respDTO);
    }

    /**
     * 购买成功UI处理
     */
    private void buySuccessUi() {
        statusView.setStatusText("购买成功");
        llBottomVisible(false);
        statusView.setLeftImageResource(IMG_RES_STATUS_SUCCESS);
        statusView.showCancelButton(new BathroomOperationStatusView.OnCancelClickListener(){

            @Override
            public void onCancelClick() {
                presenter.cancel(bathOrderId);
            }
        });
        btStartToUse.setVisibility(View.GONE);
    }

    @Override
    public void bookingCancel(BooleanRespDTO respDTO) {
        cancelUiAllOutofRecycleView();
        items.remove(items.size() -1 );
        adapter.notifyDataSetChanged();
    }

    /**
     * 取消的界面设置，除了recyclerView
     */
    private void cancelUiAllOutofRecycleView() {
        statusView.setStatusText("取消购买");
        statusView.hideCancelButton();
        statusView.setLeftImageResource(IMG_RES_STATUS_CANCEL);
        showOrderHideBtnUser();
    }


    /**
     * 隐藏预付按钮显示消费账单按钮
     */
    private void showOrderHideBtnUser(){
        btStartToUse.setVisibility(View.GONE);
        llBottomVisible(true);
        setllBottomClickListener();
    }

    @Override
    public void setBookingCountDownTime(String time) {
        Log.e(TAG, "setBookingCountDownTime: " + time +"    " + (timeText == null));
        if (timeText != null) timeText.setText(time);
    }


    /**
     * 设置消费账单和重新购买的监听
     */
    private void setllBottomClickListener(){
        leftOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转至订单详情
                Intent intent = new Intent(BuyCodeActivity.this, OrderDetailActivity.class);
                intent.putExtra(Constant.EXTRA_KEY, bathOrderId);
                startActivity(intent);
            }
        });

        rightOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.preBuyVoucher();
            }
        });
    }


    private void successRecyclerView(String location , long expireTime  ){

    }

    /**
     * 预约成功界面刷新
     * @param data
     */
    private void successRecyclerView(BathOrderRespDTO data){
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = new ArrayList<>();
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
                data.getLocation(), R.color.colorDark2, 14, Typeface.NORMAL, false));
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("有效时间：" , getReservedTime(data.getCreateTime() ,data.getExpiredTime())
                ,R.color.colorDark2 ,14 , Typeface.NORMAL , false));
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("剩余时间：" ,TimeUtils.orderBathroomLastTime(data.getExpiredTime() ," "),
                R.color.colorFullRed , 14 , Typeface.NORMAL , false));
        adapter.setCountTime(new DeviceInfoAdapter.CountTimeText() {
            @Override
            public void countTime(TextView textView) {
                Log.e(TAG, "countTime: " );
                timeText = textView ;
            }
        });
        referItems(wrapperList , true);
        presenter.BathroomCountDown((int) data.getExpiredTime());
    }


    private String getReservedTime(long startTime , long endTime){
        return TimeUtils.covertTimeToString(startTime) +"~"+TimeUtils.covertTimeToString(endTime);
    }

    /**
     * 刷新RecyclerView
     */
    private void referItems(List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList , boolean isNeedChange){
        items.clear();
        items.addAll(wrapperList);
        if (isNeedChange) adapter.notifyDataSetChanged();
    }
}
