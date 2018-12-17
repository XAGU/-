package com.xiaolian.amigo.ui.user;

import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IChangePhonePresenter;
import com.xiaolian.amigo.ui.user.intf.IChangePhoneView;
import com.xiaolian.amigo.ui.widget.ClearableEditText;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.ui.widget.dialog.BookingCancelDialog;
import com.xiaolian.amigo.ui.widget.dialog.PrepayDialog;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.CountDownButtonHelper;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePhoneActivity extends UserBaseActivity implements IChangePhoneView  ,PrepayDialog.OnOkClickListener
 , PrepayDialog.OnCancelClickListener {

    @BindView(R.id.bt_submit)
    Button submitPhone;

    @BindView(R.id.bt_send_verification_code)
    Button btVerifyCode;

    @BindView(R.id.et_mobile)
    EditText etPhone;

    @BindView(R.id.et_verification_code)
    EditText etCode;

    @Inject
    IChangePhonePresenter<IChangePhoneView> presenter;

    CountDownButtonHelper cdb;

    BookingCancelDialog bookingCancelDialog ;
    @Override
    protected void initView() {
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setUnBinder(ButterKnife.bind(this));

        showVerifyCode(true);
        cdb = new CountDownButtonHelper(btVerifyCode, "获取验证码",
                Constant.VERIFY_CODE_TIME, 1);
        cdb.setOnFinishListener(() -> {
            if (null != btVerifyCode) {
                btVerifyCode.setText("获取验证码");
                showVerifyCode(true);
            }
        });
        initDialog();
        submitPhone.setOnClickListener((view)->{
            if (bookingCancelDialog != null) bookingCancelDialog.show();
        });

        btVerifyCode.setOnClickListener((view)->{
            btVerifyCode.setEnabled(false);
            if (etPhone.getText().toString().length() != 11 || !isMobileNO(etPhone.getText().toString())){
                onError("请输入正确的电话号码");
                btVerifyCode.setEnabled(true);
                return;
            }
            presenter.getVerification(etPhone.getText().toString() , btVerifyCode);
        });
    }

    private void initDialog(){
        if (bookingCancelDialog == null){
            bookingCancelDialog = new BookingCancelDialog(this);
            bookingCancelDialog.setTvTitle("确认修改手机号码？");
            bookingCancelDialog.setTvTip("修改手机号需重新登录");
            bookingCancelDialog.setOnCancelClickListener(this);
            bookingCancelDialog.setOnOkClickListener(this);
        }
    }

    @Override
    protected int setTitle() {
        return R.string.change_phone_number;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_change_phone;
    }


    private  boolean isMobileNO(String mobileNums) {
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    @Override
    public void startTimer() {
        showVerifyCode(false);
        cdb.start();
    }

    @Override
    public void finishView() {
        this.finish();
    }


    private void showVerifyCode(boolean enable){
        int color;

        if(enable){
            color = ContextCompat.getColor(this, R.color.colorFullRed);
            btVerifyCode.setBackgroundResource(R.drawable.bg_rect_red_stroke);
        }else{
            color = ContextCompat.getColor(this, R.color.colorDarkB);
            btVerifyCode.setBackgroundResource(R.drawable.bg_rect_gray_stroke);
        }

        btVerifyCode.setTextColor(color);
        btVerifyCode.setEnabled(enable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cdb.cancel();
    }

    @Override
    public void onOkClick(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onCancelClick(Dialog dialog) {
        presenter.changePhoneNumber(etPhone.getText().toString(),etCode.getText().toString() , submitPhone);
        dialog.dismiss();
    }
}
