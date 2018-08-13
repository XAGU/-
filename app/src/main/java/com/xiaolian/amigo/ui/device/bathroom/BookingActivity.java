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
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BookingQueueProgressDTO;
import com.xiaolian.amigo.ui.device.bathroom.adapter.DeviceInfoAdapter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;
import com.xiaolian.amigo.ui.widget.BathroomOperationStatusView;
import com.xiaolian.amigo.ui.widget.dialog.BathroomBookingDialog;
import com.xiaolian.amigo.ui.widget.dialog.BookingCancelDialog;
import com.xiaolian.amigo.ui.widget.dialog.PrepayDialog;
import com.xiaolian.amigo.util.Constant;
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

    public  static final String KEY_BOOKING_ID = "BOOKING_ID" ;  //  预约id
    public  static final String KEY_BATHQUEUE_ID = "BATHQUEUE_ID" ; // 排队id
    private static final String TAG = BookingActivity.class.getSimpleName();
    private String deviceNo="" ;
    private long orderId ;
    private BookingCancelDialog dialog ;
    @Inject
    IBookingPresenter<IBookingView> presenter;


    private long bookingId ;   //  预约id
    private long bathQueueId ;   //  排队id

    private DeviceInfoAdapter.DeviceInfoWrapper wrapper = null ;  // 剩余时间
    private BathroomBookingDialog bathroomBookingDialog;
    private BathOrderPreconditionRespDTO bathOrderPreconditionRespDTO ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setButtonText();
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData(){
        if (bookingId > 0){
            presenter.query(bookingId+"" , false ,0);
        }else{
            if (bathQueueId > 0){
                presenter.queryQueueId(bathQueueId);
            }
        }
    }

    /**
     * 初始化失约提示,有失约记录时才显示失约提示
     */
    private void initTopTip(){
        if (bookingId > 0 &&missedTimes >0  ){
            setTopTip();
        }else{
            tvBookingTip.setVisibility(GONE);
        }
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
    private List<DeviceInfoAdapter.DeviceInfoWrapper> getListDevceInfoAdapter(BathBookingRespDTO data ,boolean isShowRemainTime){
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = new ArrayList<>();
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
                data.getLocation(), R.color.colorDark2, 14, Typeface.NORMAL, false));
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("有效时间：" , getReservedTime(data.getCreateTime() ,data.getExpiredTime())
                ,R.color.colorDark2 ,14 , Typeface.NORMAL , false));
        if (isShowRemainTime) {
            wrapper  = new DeviceInfoAdapter.DeviceInfoWrapper("剩余时间：", TimeUtils.orderBathroomLastTime(data.getExpiredTime(), " "),
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

        rightOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // todo


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


    @Override
    protected void initIntent() {
        super.initIntent();
        if (getIntent() != null){
            deviceNo = getIntent().getStringExtra(KEY_DEVICE_NO );
            bathOrderPreconditionRespDTO = getIntent().getParcelableExtra(KEY_ORDER_PRECONDITION);

            //

            bookingId = getIntent().getLongExtra(KEY_BOOKING_ID , 0);
            bathQueueId = getIntent().getLongExtra(KEY_BATHQUEUE_ID , 0);

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

    /**
     * 设置tips
     * @param type
     */
    public void setTip(int type){
        if (type == Constant.BOOKING_DEVICE){
            getTvTip1().setText(getString(R.string.booking_user_this));
        }else if (type == Constant.BOOKING_FLOOR){
            getTvTip1().setText(getString(R.string.booking_user_empty));
        }

        getTvTip2().setText(R.string.booking_user_common_tip);

        getTvTip().setText("预约使用说明");
    }

    @Override
    protected void setTips(TextView tip1, TextView tip2, TextView tip3, TextView tip4,
                           TextView tip, RelativeLayout rlTip) {
//        if (!TextUtils.isEmpty(deviceNo) ){
//            tip1.setText(getString(R.string.booking_user_this));
//        }else{
//            tip1.setText(getString(R.string.booking_user_empty));
//        }
//        tip2.setText(getString(R.string.booking_user_common_tip));
////        tip3.setText(getString(R.string.booking_use_tip3));
////        tip4.setText(getString(R.string.booking_use_tip4));
//        tip.setText("预约使用说明");
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
    private void successRecyclerView(BathBookingRespDTO data){
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
    public void bookingSuccess(BathBookingRespDTO data) {
        setTip(data.getType());
        deviceNo = data.getDeviceNo()+"" ;
        statusView.setLeftImageResource(IMG_RES_STATUS_SUCCESS);
        statusView.setStatusText("预约成功");
        presenter.cancelCountDown();
        btStartToUse.setVisibility(View.GONE);
        statusView.showCancelButton(() -> {
            cancelDialog(data.getId());

        });
        successRecyclerView(data);
        statusView.getRightText().setText("取消");
        statusView.getRightText().setTextColor(getResources().getColor(R.color.colorDarkB ));
        presenter.countDownexpiredTime(data.getExpiredTime());
        presenter.query(data.getId()+"" ,true , 10);
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

                // todo
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

                // todo
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
    public void appointMentTimeOut(BathBookingRespDTO respDTO) {
        setTip(respDTO.getType());
        deviceNo = respDTO.getDeviceNo()+"" ;
        statusView.setLeftImageResource(IMG_RES_STATUS_FAIL);
        statusView.setStatusText(getString(R.string.preBookTimeOut));
        statusView.getTip().setText(getString(R.string.tip_timeout));
        llBottom.setVisibility(View.VISIBLE);
        statusView.hideCancelButton();
        btStartToUse.setVisibility(GONE);
        rightOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  todo
            }
        });
        List<DeviceInfoAdapter.DeviceInfoWrapper> deviceInfoWrappers = getListDevceInfoAdapter(respDTO , true);
        referItems(deviceInfoWrappers , true);
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

                //  todo
            }
        });
    }

    @Override
    public void showQueue(BookingQueueProgressDTO data) {
        initTopTip();
        getQueueInfo(data);
    }

    /**
     * 刷新预约排队的信息
     * @param data
     */
    public void getQueueInfo(BookingQueueProgressDTO data ){
        statusView.setStatusText(getString(R.string.preBookingWait));
        statusView.setLeftImageResource(IMG_RES_STATUS_OPERATING);
        statusView.setTip(getString(R.string.preBookingWaitTip));
        llBottomVisible(false);
        List<DeviceInfoAdapter.DeviceInfoWrapper> list = getListWaitItems(data.getLocation() , data.getRemain());
        referItems(list , true);
    }


    public List<DeviceInfoAdapter.DeviceInfoWrapper> getListWaitItems(String location , int remain){
        List<DeviceInfoAdapter.DeviceInfoWrapper> wrapperList = new ArrayList<>();
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置" ,
                location , R.color.colorDark2 , 14 ,Typeface.NORMAL , false));
        wrapperList.add(new DeviceInfoAdapter.DeviceInfoWrapper("还需等待",
                remain +"人" , R.color.refresh_red ,14 , Typeface.NORMAL , false));
        return wrapperList ;
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
    public void gotoUsing(long bathOrderId) {
        presenter.cancelCountDown();
        startActivity(new Intent(this ,BathroomHeaterActivity.class)
        .putExtra(KEY_BATH_ORDER_ID , bathOrderId));
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
