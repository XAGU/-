package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.ui.device.bathroom.adapter.DeviceInfoAdapter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;
import com.xiaolian.amigo.ui.widget.BathroomOperationStatusView;
import com.xiaolian.amigo.ui.widget.dialog.BathroomBookingDialog;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.DimentionUtils;

import java.text.DecimalFormat;

import javax.inject.Inject;

import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BALANCE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_AMOUNT;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_DESC;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_EXPIRED_TIME;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_LOCATION;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MAX_MISSABLE_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MIN_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MISSED_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_PREPAY;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_CANCEL;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_OPERATING;
import static com.xiaolian.amigo.ui.widget.BathroomOperationStatusView.IMG_RES_STATUS_SUCCESS;

/**
 * 预约使用
 *
 * @author zcd
 * @date 18/6/29
 */
public class BookingActivity extends UseWayActivity implements IBookingView {

    @Inject
    IBookingPresenter<IBookingView> presenter;

    private BathroomBookingDialog bathroomBookingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        initIntent();
        setTopTip();
        setList();
        setButtonText();
        statusView.setLeftImageResource(IMG_RES_STATUS_OPERATING);
        statusView.setStatusText("预约中");
        statusView.hideCancelButton();
        btStartToUse.setOnClickListener(v -> presenter.pay(prepayAmount, bonusId));
        presenter.bathroomBookingCountDown();
    }

    private void setTopTip() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString tipSpan1 = new SpannableString("每月失约" + maxMissAbleTimes
                + "次将无法预约，已失约");
        tipSpan1.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, this)), 0, tipSpan1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipSpan1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorDark6)),
                0, tipSpan1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tipSpan1);
        SpannableString missedTimesSpan = new SpannableString(String.valueOf(missedTimes));
        missedTimesSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, this)), 0, missedTimesSpan.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        missedTimesSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorFullRed)),
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
    }

    private void setList() {
        items.clear();
        items.add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
                location, R.color.colorDark2, 14, Typeface.NORMAL, false));
        items.add(new DeviceInfoAdapter.DeviceInfoWrapper("预留时间：",
                reservedTime, R.color.colorDark2, 14, Typeface.NORMAL, false));

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
        textView.setText("预约记录");
        textView.setOnClickListener(v -> onSubtitleClick());
    }

    @Override
    protected void setSubTitle(TextView textView) {
        textView.setText("预约记录");
        textView.setOnClickListener(v -> onSubtitleClick());
    }

    @Override
    protected void setTips(TextView tip1, TextView tip2, TextView tip3, TextView tip4,
                           TextView tip, RelativeLayout rlTip) {
        tip1.setText(getString(R.string.booking_use_tip1));
        tip2.setText(getString(R.string.booking_use_tip2));
        tip3.setText(getString(R.string.booking_use_tip3));
        tip4.setText(getString(R.string.booking_use_tip4));
        tip.setText("预约使用说明");
    }

    @Override
    protected String getButtonText() {
        return "，确认预约";
    }

    private void showBookingDialog() {
        if (bathroomBookingDialog == null) {
            bathroomBookingDialog = new BathroomBookingDialog(this);
        }
        bathroomBookingDialog.show();
    }

    private void onSubtitleClick() {
        startActivity(new Intent(this, BookingRecordActivity.class));
//        showBookingDialog();
    }

    @Override
    public void bookingSuccess(BathOrderRespDTO data) {
        statusView.setLeftImageResource(IMG_RES_STATUS_SUCCESS);
        statusView.setStatusText("预约成功");
        statusView.showCancelButton(() -> presenter.cancel());
    }

    @Override
    public void bookingCancel() {
        statusView.setLeftImageResource(IMG_RES_STATUS_CANCEL);
        statusView.setStatusText("预约取消预约");
        statusView.hideCancelButton();
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
}
