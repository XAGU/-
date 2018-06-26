package com.xiaolian.amigo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).sendVerificationCode(etMobile.getText().toString());
        }
        etVerificationCode.requestFocus();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_registry_step1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.setEditHintAndSize(getString(R.string.mobile_hint), 14, etMobile);
        ViewUtil.setEditHintAndSize(getString(R.string.verification_code_hint), 14, etVerificationCode);

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
        if (etMobile != null) {
            etMobile.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etMobile, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}
