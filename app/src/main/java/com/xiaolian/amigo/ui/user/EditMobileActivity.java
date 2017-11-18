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
 * @author zcd
 */

public class EditMobileActivity extends UserBaseActivity implements IEditMobileView {

    @Inject
    IEditMobilePresenter<IEditMobileView> presenter;

    @BindView(R.id.bt_verify_code)
    Button bt_verify_code;

    @BindView(R.id.et_mobile)
    ClearableEditText et_mobile;

    @BindView(R.id.et_verify_code)
    ClearableEditText et_verify_code;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @OnClick(R.id.bt_verify_code)
    void onVerifyCodeButtonClick() {
        if (verify(et_mobile.getText().toString())) {
            presenter.getVerifyCode(et_mobile.getText().toString());
        }
    }

    @OnClick(R.id.bt_submit)
    void onSubmitButtonClick() {
        presenter.updateMobile(et_mobile.getText().toString().trim(), et_verify_code.getText().toString());
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

        cdb = new CountDownButtonHelper(bt_verify_code, "获取验证码", 60, 1);
        cdb.setOnFinishListener(() -> {
            bt_verify_code.setEnabled(true);
            bt_verify_code.setText("获取验证码");
            int color = ContextCompat.getColor(getApplicationContext(), R.color.colorFullRed);
            bt_verify_code.setTextColor(color);
            bt_verify_code.setBackgroundResource(R.drawable.bg_rect_red_stroke);
        });
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(et_mobile.getText())
                        && !TextUtils.isEmpty(et_verify_code.getText())) {
                    bt_submit.setEnabled(true);
                } else {
                    bt_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        CommonUtil.showSoftInput(this, et_mobile);
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
        bt_verify_code.setTextColor(color);
        bt_verify_code.setBackgroundResource(R.drawable.bg_rect_gray_stroke);
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
