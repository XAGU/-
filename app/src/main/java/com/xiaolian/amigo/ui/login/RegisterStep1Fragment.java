package com.xiaolian.amigo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.CountDownButtonHelper;
import com.xiaolian.amigo.util.ViewUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 注册第一步
 *
 * @author zcd
 * @date 17/9/19
 */

public class RegisterStep1Fragment extends Fragment {
    private static final String GET_VERIFICATION_CODE = "获取验证码";
    private static final int MOBILE_LENGTH = 1;

    @BindView(R.id.bt_submit)
    Button btSubmit;


    @OnClick(R.id.bt_submit)
    void checkVerification() {
        //在第三方登录的时候验证电话号码是否被绑定
        if (((LoginActivity) getActivity()).isThirdLogin()){
            ((LoginActivity) getActivity()).ThirdLoginPhoneBind(etMobile.getText().toString(),
                    etVerificationCode.getText().toString());
            Log.e("Test", "checkVerification: --" );
            return;
        }

        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).setMobileAndCode(etMobile.getText().toString(),
                    etVerificationCode.getText().toString());
            ((LoginActivity) getActivity()).checkVerificationCode(etMobile.getText().toString(),
                    etVerificationCode.getText().toString());
        }
    }


    @BindView(R.id.et_mobile)
    EditText etMobile;

    @BindView(R.id.bt_send_verification_code)
    Button btSendVerificationCode;

    @BindView(R.id.iv_agreement_icon)
    ImageView ivAgreementIcon;
    private boolean agreementChecked = true;

    @OnClick(R.id.bt_send_verification_code)
    void sendVerificationCode() {
        if (!isMobileNO(etMobile.getText().toString().trim())){
            ((LoginActivity) getActivity()).onError("手机号不合法");
            return;
        }

        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).sendVerificationCode(etMobile.getText().toString());
        }
    }

    @OnClick(R.id.tv_agreement)
    void onAgreementClick() {
        startActivity(new Intent(getActivity(), WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_AGREEMENT));
    }

    @OnClick(R.id.iv_agreement_icon)
    void onAgreementIconClick() {
        if (agreementChecked) {
            agreementChecked = false;
            ivAgreementIcon.setImageResource(R.drawable.ic_agreement_uncheck);
        } else {
            agreementChecked = true;
            ivAgreementIcon.setImageResource(R.drawable.ic_agreement_check);
        }
        toggleButton();
    }

    @BindView(R.id.et_verification_code)
    EditText etVerificationCode;

    CountDownButtonHelper cdb;

    InputFilter inputFilter ;  //空格监听
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_registry_step1, container, false);
        ButterKnife.bind(this, view);
        initInputFilter();
        btSubmit.setEnabled(true);
        return view;
    }


    private void initInputFilter(){
        inputFilter = new InputFilter(){

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.setEditHintAndSize(getString(R.string.mobile_hint), 14, etMobile);
        ViewUtil.setEditHintAndSize(getString(R.string.verification_code_hint), 14, etVerificationCode);
        etMobile.setFilters(new InputFilter[]{inputFilter , new InputFilter.LengthFilter(11)});
        etVerificationCode.setFilters(new InputFilter[]{inputFilter , new InputFilter.LengthFilter(6)});
        cdb = new CountDownButtonHelper(btSendVerificationCode, GET_VERIFICATION_CODE,
                Constant.VERIFY_CODE_TIME, 1);
        cdb.setOnFinishListener(() -> {
            if (getContext() != null) {
                btSendVerificationCode.setEnabled(true);
                btSendVerificationCode.setText(GET_VERIFICATION_CODE);
                int color = ContextCompat.getColor(getContext(), R.color.colorFullRed);
                btSendVerificationCode.setTextColor(color);
                btSendVerificationCode.setBackgroundResource(R.drawable.bg_rect_red_stroke);
            }
        });

        int color = ContextCompat.getColor(getContext(), R.color.colorDarkB);
        btSendVerificationCode.setTextColor(color);
        btSendVerificationCode.setBackgroundResource(R.drawable.bg_rect_gray_stroke);
        btSendVerificationCode.setEnabled(false);
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                toggleButton();
                if (etMobile.length() >= MOBILE_LENGTH) {
                    btSendVerificationCode.setEnabled(true);
                    btSendVerificationCode.setText(GET_VERIFICATION_CODE);
                    int color = ContextCompat.getColor(getContext(), R.color.colorFullRed);
                    btSendVerificationCode.setTextColor(color);
                    btSendVerificationCode.setBackgroundResource(R.drawable.bg_rect_red_stroke);
                } else {
                    btSendVerificationCode.setText(GET_VERIFICATION_CODE);
                    int color = ContextCompat.getColor(getContext(), R.color.colorDarkB);
                    btSendVerificationCode.setTextColor(color);
                    btSendVerificationCode.setBackgroundResource(R.drawable.bg_rect_gray_stroke);
                    btSendVerificationCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toggleButton();
    }



    public void startTimer() {
        int color = ContextCompat.getColor(getContext(), R.color.colorDarkB);
        btSendVerificationCode.setTextColor(color);
        btSendVerificationCode.setBackgroundResource(R.drawable.bg_rect_gray_stroke);
        cdb.start();
        //让输入验证码Editview获取焦点
        etVerificationCode.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etVerificationCode, 0);
            }
        },100);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (cdb != null) {
            cdb.cancel();
        }
    }

    @OnTextChanged(R.id.et_verification_code)
    public void onVerifyCodeEdit() {
        toggleButton();
    }

    private void toggleButton() {
        boolean valid = !TextUtils.isEmpty(etVerificationCode.getText())
                && !TextUtils.isEmpty(etMobile.getText())
                && etMobile.getText().length() >= MOBILE_LENGTH
                && agreementChecked;
        btSubmit.setEnabled(valid);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("Test", "onResume: register" );
        ((LoginActivity) getActivity()).showThirdLoginView(false);
        if (etMobile != null) {
            etMobile.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etMobile, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
    private  boolean isMobileNO(String mobileNums) {
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
}
