package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;
import com.xiaolian.amigo.ui.widget.dialog.BathroomBookingDialog;
import com.xiaolian.amigo.util.DimentionUtils;

import javax.inject.Inject;

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
        setButtonText();
    }

    private void setButtonText() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(getString(R.string.prepay));
        String buttonText = "10" + getString(R.string.yuan);
        SpannableString buttonSpan = new SpannableString(buttonText);
        buttonSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(buttonSpan);
        builder.append("，确认预约");

        btStartToUse.setText(builder);
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

    private void showBookingDialog() {
        if (bathroomBookingDialog == null) {
            bathroomBookingDialog = new BathroomBookingDialog(this);
        }
        bathroomBookingDialog.show();
    }

    private void onSubtitleClick() {
//        startActivity(new Intent(this, BookingRecordActivity.class));
        showBookingDialog();
    }
}
