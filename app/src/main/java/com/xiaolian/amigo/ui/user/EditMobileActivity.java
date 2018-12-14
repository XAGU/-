package com.xiaolian.amigo.ui.user;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.ClearableEditText;
import com.xiaolian.amigo.ui.user.intf.IEditMobilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobileView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.CountDownButtonHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改手机号
 *
 * @author zcd
 * @date 17/9/15
 */

public class EditMobileActivity extends UserBaseActivity implements IEditMobileView {

    @Inject
    IEditMobilePresenter<IEditMobileView> presenter;

    @BindView(R.id.bt_verify_code)
    Button btVerifyCode;

    @BindView(R.id.et_mobile)
    ClearableEditText etMobile;

    @BindView(R.id.et_verify_code)
    ClearableEditText etVerifyCode;

    @BindView(R.id.bt_submit)
    Button btSubmit;

    @OnClick(R.id.bt_verify_code)
    void onVerifyCodeButtonClick() {
        btVerifyCode.setEnabled(false);
        if (verify(etMobile.getText().toString())) {
            presenter.getVerifyCode(etMobile.getText().toString() , btVerifyCode);
        }
    }

    @OnClick(R.id.bt_submit)
    void onSubmitButtonClick() {
        btSubmit.setEnabled(false);
        presenter.updateMobile(etMobile.getText().toString().trim(), etVerifyCode.getText().toString() , btSubmit);
    }

    private boolean verify(String s) {
        return true;
    }


    CountDownButtonHelper cdb;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(EditMobileActivity.this);

        cdb = new CountDownButtonHelper(btVerifyCode, "获取验证码", 60, 1);
        cdb.setOnFinishListener(() -> {
            btVerifyCode.setEnabled(true);
            btVerifyCode.setText("获取验证码");
            int color = ContextCompat.getColor(getApplicationContext(), R.color.colorFullRed);
            btVerifyCode.setTextColor(color);
            btVerifyCode.setBackgroundResource(R.drawable.bg_rect_red_stroke);
        });
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etMobile.getText())
                        && !TextUtils.isEmpty(etVerifyCode.getText())) {
                    btSubmit.setEnabled(true);
                } else {
                    btSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        CommonUtil.showSoftInput(this, etMobile);
    }

    @Override
    protected int setTitle() {
        return R.string.edit_mobile;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_edit_mobile;
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cdb.cancel();
    }

    @Override
    public void startTimer() {
        int color = ContextCompat.getColor(getApplicationContext(), R.color.colorDarkB);
        btVerifyCode.setTextColor(color);
        btVerifyCode.setBackgroundResource(R.drawable.bg_rect_gray_stroke);
        cdb.start();
    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

}
