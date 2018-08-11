package com.xiaolian.amigo.ui.device.bathroom;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.ui.device.bathroom.adapter.DeviceInfoAdapter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;
import com.xiaolian.amigo.ui.widget.BathroomOperationStatusView;
import com.xiaolian.amigo.ui.widget.dialog.BathroomBookingDialog;
import com.xiaolian.amigo.ui.widget.dialog.BookingCancelDialog;
import com.xiaolian.amigo.ui.widget.dialog.PrepayDialog;
import com.xiaolian.amigo.util.DimentionUtils;
import com.xiaolian.amigo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.view.View.GONE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_DEVICE_NO;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_ORDER_PRECONDITION;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomHeaterActivity.KEY_BATH_ORDER_ID;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_CANCEL;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_FAIL;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_OPERATING;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_SUCCESS;
import static com.xiaolian.amigo.util.Constant.ACCEPTED;
import static com.xiaolian.amigo.util.Constant.EXPIRED;

/**
 * 预约使用
 * @author zcd
 * @date 18/6/29
 */
public class BookingActivity extends UseWayActivity implements IBookingView {
    private static final String TAG = BookingActivity.class.getSimpleName();
    private String deviceNo="" ;
    private long orderId ;
    private BookingCancelDialog dialog ;
    @Inject
    IBookingPresenter<IBookingView> presenter;


    private DeviceInfoAdapter.DeviceInfoWrapper wrapper = null ;  // 剩余时间
    private BathroomBookingDialog bathroomBookingDialog;
    private BathOrderPreconditionRespDTO bathOrderPreconditionRespDTO ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        if (bathOrderPreconditionRespDTO != null){
            referUiForLastOrderStatus();

        }else{
            initTopTip();
            referBooking();
        }



    }

    /**
     * 初始化失约提示,有失约记录时才显示失约提示
     */
    private void initTopTip(){
        if (!TextUtils.isEmpty(deviceNo) &&missedTimes >0  ){
            setTopTip();
        }else{
            tvBookingTip.setVisibility(GONE);
        }
    }

    /**
     * 从未完成订单跳转的，过期消费卷的状态
     */
    private void expiredForOldOrder(boolean isNeedChange){
        showOrderHideBtnUser();
        statusView.setStatusText("预约超时");
        setTopTip();
        statusView.hideCancelButton();
        statusView.setLeftImageResource(IMG_RES_STATUS_FAIL);
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = getListDevceInfoAdapter(true);
        referItems(wrapperList ,isNeedChange);
    }


    /**
     * 刷新RecyclerView
     */
    private void referItems(List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList , boolean isNeedChange){
        items.clear();
        items.addAll(wrapperList);
        if (isNeedChange) adapter.notifyDataSetChanged();
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
            wrapper  = new DeviceInfoAdapter.DeviceInfoWrapper("剩余时间：", TimeUtils.orderBathroomLastTime(bathOrderPreconditionRespDTO.getExpiredTime(), " "),
                    R.color.colorFullRed, 14, Typeface.NORMAL, false) ;
            wrapperList.add(wrapper);
        }
        return wrapperList ;
    }

    /**
     * 隐藏预付按钮显示消费账单按钮
     */
    private void showOrderHideBtnUser(){
        btStartToUse.setVisibility(View.GONE);
        llBottomVisible(true);
        setllBottomClickListener();
    }

    /**
     * 设置消费账单和重新购买的监听
     */
    private void setllBottomClickListener(){
//        leftOper.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 跳转至订单详情
//                Intent intent = new Intent(BookingActivity.this, OrderDetailActivity.class);
//                intent.putExtra(Constant.EXTRA_KEY, orderId);
//                startActivity(intent);
//            }
//        });

        rightOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.preBooking(deviceNo);

            }
        });
    }


    /**
     * 购买成功UI处理
     */
    private void buySuccessUi() {
        statusView.setStatusText("预约成功");
        llBottomVisible(false);
        statusView.setLeftImageResource(IMG_RES_STATUS_SUCCESS);
        statusView.showCancelButton(new BathroomOperationStatusView.OnCancelClickListener(){

            @Override
            public void onCancelClick() {
                presenter.cancel(orderId);
                presenter.cancelCountDown();
            }
        });
        initTopTip();
        btStartToUse.setVisibility(View.GONE);
    }

    /**
     * 购买成功界面
     */
    private void buySuccessForOldOrder(boolean isNeedChange){
        setTopTip();
        buySuccessUi();
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = getListDevceInfoAdapter(true);
        referItems(wrapperList ,isNeedChange);
        presenter.countDownexpiredTime(bathOrderPreconditionRespDTO.getExpiredTime());
        presenter.query(bathOrderPreconditionRespDTO.getBathBookingId()+"" , true , 10);
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
     * 取消的界面设置，除了recyclerView
     */
    private void cancelUiAllOutofRecycleView() {
        statusView.setStatusText("取消购买");
        statusView.hideCancelButton();
        statusView.setLeftImageResource(IMG_RES_STATUS_CANCEL);
        showOrderHideBtnUser();
    }


    /**
     * 根据上一个订单状态刷新界面
     */
    private void referUiForLastOrderStatus(){
        if (bathOrderPreconditionRespDTO == null){
            return ;
        }

        if (bathOrderPreconditionRespDTO.getBathBookingId() != 0) {
            orderId = bathOrderPreconditionRespDTO.getBathBookingId();
        }else{
            if (bathOrderPreconditionRespDTO.getBathOrderId() != 0)
            orderId = bathOrderPreconditionRespDTO.getBathOrderId();
        }
        forOldOrderToSetTipTxt(bathOrderPreconditionRespDTO);
        switch (bathOrderPreconditionRespDTO.getStatus()){
//            case USING:
//                cancelForOldOrder(true);
//                break;
            case ACCEPTED:
                buySuccessForOldOrder(true);
                break;
            case EXPIRED:
                expiredForOldOrder(true);
                break;
        }
    }

    /**
     * 预约中状态
     */
    private void referBooking() {
        llBottomVisible(false);
        setList();
        setButtonText();
        statusView.setLeftImageResource(IMG_RES_STATUS_OPERATING);
        statusView.setStatusText("预约中");
        statusView.hideCancelButton();
        btStartToUse.setVisibility(GONE);
//        btStartToUse.setClickable(true);
//        btStartToUse.setOnClickListener(v -> {
//            showBookingDialog();
//            presenter.booking(deviceNo);
//        });
//        presenter.bathroomBookingCountDown();
//        btStartToUse.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        if (getIntent() != null){
            deviceNo = getIntent().getStringExtra(KEY_DEVICE_NO );
            bathOrderPreconditionRespDTO = getIntent().getParcelableExtra(KEY_ORDER_PRECONDITION);
        }
    }

    private void setTopTip() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString tipSpan1 = new SpannableString("指定浴室每月失约" + maxMissAbleTimes
                + "次后将无法预约，已");
        tipSpan1.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, this)), 0, tipSpan1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipSpan1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorDark6)),
                0, tipSpan1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tipSpan1);
        SpannableString missedTimesSpan = new SpannableString(String.valueOf(missedTimes));
        missedTimesSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, this)), 0, missedTimesSpan.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        missedTimesSpan.setSpan(new ForegroundColorSpan(getResources().getColor( R.color.colorFullRed)),
                0, missedTimesSpan.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(missedTimesSpan);
        SpannableString tipSpan2 = new SpannableString("次");
        tipSpan2.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, this)), 0, tipSpan2.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipSpan2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorDark6)),
                0, tipSpan2.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tipSpan2);
        tvBookingTip.setText(builder);
        tvBookingTip.setVisibility(View.VISIBLE);
    }

    private void setList() {
        items.clear();
        items.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
                location, R.color.colorDark2, 14, Typeface.NORMAL, false));
        items.add(new DeviceInfoAdapter.DeviceInfoWrapper("预留时间：",
                reservedTime +"分钟", R.color.colorDark2, 14, Typeface.NORMAL, false));

    }


    @Override
    protected void setToolbarTitle(TextView textView) {
        textView.setText("预约使用");
    }

    @Override
    protected void setTitle(TextView textView) {
        textView.setText("预约使用");
    }

    @Override
    protected void setToolbarSubTitle(TextView textView) {
        textView.setText("");
//        textView.setOnClickListener(v -> onSubtitleClick());
    }

    @Override
    protected void setSubTitle(TextView textView) {
        textView.setText("");
//        textView.setOnClickListener(v -> onSubtitleClick());
    }

    @Override
    protected void setTips(TextView tip1, TextView tip2, TextView tip3, TextView tip4,
                           TextView tip, RelativeLayout rlTip) {
        if (!TextUtils.isEmpty(deviceNo) || bathOrderPreconditionRespDTO != null && bathOrderPreconditionRespDTO.getDeviceNo() != 0){
            tip1.setText(getString(R.string.booking_user_this));
        }else{
            tip1.setText(getString(R.string.booking_user_empty));
        }
        tip2.setText(getString(R.string.booking_user_common_tip));
//        tip3.setText(getString(R.string.booking_use_tip3));
//        tip4.setText(getString(R.string.booking_use_tip4));
        tip.setText("预约使用说明");
    }

    @Override
    protected String getButtonText() {
        return "确认预约";
    }

    private void showBookingDialog() {
        if (bathroomBookingDialog == null) {
            bathroomBookingDialog = new BathroomBookingDialog(this);
        }
        bathroomBookingDialog.show();
    }

    @Override
    public void bookingSuccess(BathOrderRespDTO data) {
        orderId = data.getBathOrderId();
        statusView.setLeftImageResource(IMG_RES_STATUS_SUCCESS);
        statusView.setStatusText("预约成功");
        presenter.cancelCountDown();
        btStartToUse.setVisibility(View.GONE);
        statusView.showCancelButton(() -> {
//            presenter.cancel(data.getBathBookingId());
            cancelDialog(data.getBathBookingId());
//            presenter.cancelCountDown();
        });
        successRecyclerView(data);
        statusView.getRightText().setText("取消");
        statusView.getRightText().setTextColor(getResources().getColor(R.color.colorDarkB ));
        presenter.countDownexpiredTime(data.getExpiredTime());
        presenter.query(data.getBathBookingId()+"" ,true , 10);
    }

    /**
     * 取消预约弹窗
     * @param id
     */
    private void cancelDialog(long id){
        if (dialog == null){
            dialog = new BookingCancelDialog(this);
            dialog.setOnCancelClickListener(new PrepayDialog.OnCancelClickListener() {
                @Override
                public void onCancelClick(Dialog dialog) {
                    presenter.cancel(id);
                }
            });
            dialog.setOnOkClickListener(new PrepayDialog.OnOkClickListener() {
                @Override
                public void onOkClick(Dialog dialog) {
                    dialog.cancel();
                }
            });
        }
        dialog.show();
    }
    /**
     * 预约成功界面刷新
     * @param data
     */
    private void successRecyclerView(BathOrderRespDTO data){
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = new ArrayList<>();
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
                data.getLocation(), R.color.colorDark2, 14, Typeface.NORMAL, false));
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("预留时间：" , getReservedTime(data.getCreateTime() ,data.getExpiredTime())
                ,R.color.colorDark2 ,14 , Typeface.NORMAL , false));
        wrapper = new DeviceInfoAdapter.DeviceInfoWrapper("剩余时间：" ,TimeUtils.orderBathroomLastTime(data.getExpiredTime() ," "),
                R.color.colorFullRed , 14 , Typeface.NORMAL , false);
        wrapperList.add(wrapper);
        referRecyclerView(wrapperList);
    }

    /**
     * 刷新recyclerView 界面
     * @param
     */
    private void referRecyclerView( List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList) {
        items.clear();
        items.addAll(wrapperList);
        adapter.notifyDataSetChanged();
    }

    private String getReservedTime(long startTime , long endTime){
        return TimeUtils.covertTimeToString(startTime) +"~"+TimeUtils.covertTimeToString(endTime);
    }

    @Override
    public void bookingCancel() {
        statusView.setLeftImageResource(IMG_RES_STATUS_CANCEL);
        statusView.setStatusText("取消预约");
        statusView.hideCancelButton();
        CancelBookingView();
        rightOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.preBooking(deviceNo+"");
            }
        });
    }

    /**
     * 取消预约
     */
    private void CancelBookingView(){
        if (items!= null && items.size() > 0 && adapter != null){
            items.remove(items.size() -1 );
            adapter.notifyDataSetChanged();
            presenter.cancelCountDown();
        }
        llBottomVisible(true);
        rightOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.preBooking(deviceNo+"");
            }
        });

    }

    @Override
    public void setBookingCountDownTime(String time) {
        TextView textView = statusView.getRightText();
        if (textView != null){
            textView.setVisibility(View.VISIBLE);
            textView.setTextColor(getResources().getColor(R.color.colorFullRed));
            textView.setText(time);
        }
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void reschedule(BathPreBookingRespDTO respDTO) {
//          if (respDTO.getBonus() != null){
//              bonusId = respDTO.getBonus().getId();
//              bonusDesc = respDTO.getBonus().getDescription();
//              bonusAmount = respDTO.getBonus().getAmount();
//          }
          balance = respDTO.getPrepayInfo().getBalance();
          minPrepay = respDTO.getPrepayInfo().getMinPrepay();
          prepay = respDTO.getPrepayInfo().getPrepay();
          location = respDTO.getLocation();
//          expiredTime = respDTO.getExpiredTime();
          missedTimes = respDTO.getMissedTimes();
          maxMissAbleTimes = respDTO.getMaxMissAbleTimes();
          reservedTime = respDTO.getReservedMinute();
          referBooking();
    }

    @Override
    public void appointMentTimeOut() {
        setTopTip();
        statusView.setLeftImageResource(IMG_RES_STATUS_FAIL);
        statusView.setStatusText(getString(R.string.preBookTimeOut));
        statusView.getTip().setText(getString(R.string.tip_timeout));
        llBottom.setVisibility(View.VISIBLE);
        statusView.hideCancelButton();
        btStartToUse.setVisibility(GONE);
        rightOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.preBooking(deviceNo+"");
            }
        });
    }

    @Override
    public void showWaitLoading() {
        showBookingDialog();
    }

    @Override
    public void hideWaitLoading() {
        if (bathroomBookingDialog != null && bathroomBookingDialog.isShowing()){
            bathroomBookingDialog.dismiss();
        }
    }

    @Deprecated
    @Override
    public void SendDeviceTimeOut() {

    }

    @Override
    public void countTimeLeft(String text) {
        if (wrapper != null && items != null && items.size() > 0 && adapter != null){
            int currentPosition = items.indexOf(wrapper);
            if (currentPosition != -1){
                wrapper.setRightText(text);
                items.set(currentPosition ,wrapper);
                adapter.notifyItemChanged(currentPosition);
            }
        }
    }

    @Override
    public void gotoUsing(BathOrderRespDTO dto) {
        presenter.cancelCountDown();
        startActivity(new Intent(this ,BathroomHeaterActivity.class)
        .putExtra(KEY_BATH_ORDER_ID , dto.getBathOrderId()));
    }

    @Override
    public void preBookingWait() {
        statusView.setStatusText(getString(R.string.preBookingWait));
        statusView.setLeftImageResource(IMG_RES_STATUS_OPERATING);
        statusView.setTip(getString(R.string.preBookingWaitTip));
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = new ArrayList<>();
//        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
//                bathOrderPreconditionRespDTO.getLocation(), R.color.colorDark2, 14, Typeface.NORMAL, false));
//        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("有效时间：" , getReservedTime(bathOrderPreconditionRespDTO.getCreateTime() ,bathOrderPreconditionRespDTO.getExpiredTime())
//                ,R.color.colorDark2 ,14 , Typeface.NORMAL , false));
//        if (isShowRemainTime) {
//            wrapper  = new DeviceInfoAdapter.DeviceInfoWrapper("剩余时间：", TimeUtils.orderBathroomLastTime(bathOrderPreconditionRespDTO.getExpiredTime(), " "),
//                    R.color.colorFullRed, 14, Typeface.NORMAL, false) ;
//            wrapperList.add(wrapper);
//        }
//        return wrapperList ;
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置" ,
                "佩奇公寓3栋" , R.color.colorDark2 , 14 ,Typeface.NORMAL , false));
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("还需等待",
                "15人" , R.color.refresh_red ,14 , Typeface.NORMAL , false));
        referItems(wrapperList ,true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bathroomBookingDialog != null){
            bathroomBookingDialog.onDettechView();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG, "onBackPressed: " );
    }
}
