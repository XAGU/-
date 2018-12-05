package com.xiaolian.amigo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.PasswordEditText;
import com.xiaolian.amigo.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录Fragment
 *
 * @author zcd
 * @date 17/9/19
 */

public class LoginFragment extends Fragment {

    private static final int MOBILE_LENGTH = 11;
    private static final int PASSWORD_MIN_LENGTH = 6;

    @BindView(R.id.et_mobile)
    EditText etMobile;

    @BindView(R.id.et_userpwd)
    PasswordEditText etUserpwd;

    @OnClick(R.id.tv_password_retrieval_link)
    void gotoPasswordRetrievalSetp1() {
        Intent intent = new Intent(getContext(), PasswordRetrievalStep1Activity.class);
        startActivity(intent);
    }

    @BindView(R.id.bt_submit)
    Button btSubmit;

    private InputFilter inputFilter =  new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ") || source.toString().contentEquals("\n")) {
                return "";
            } else {
                return null;
            }
        }
    };

    @OnClick(R.id.bt_submit)
    void login() {
        if (!isMobileNO(etMobile.getText().toString())){
            ((LoginActivity) getActivity()).onError("手机号不合法");
            return;
        }
        if (etUserpwd.getText().toString().trim().length() == 0){
            ((LoginActivity) getActivity()).onError("请输入登录密码");
            return;
        }

        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).login(etMobile.getText().toString(), etUserpwd.getText().toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.setEditHintAndSize(getString(R.string.mobile_hint), 14, etMobile);
        ViewUtil.setEditHintAndSize(getString(R.string.password_hint), 14, etUserpwd);
        TextChange textChange = new TextChange();
        etMobile.setFilters(new InputFilter[]{inputFilter ,new InputFilter.LengthFilter(11)});
        etMobile.addTextChangedListener(textChange);
        etUserpwd.addTextChangedListener(textChange);
        if (getActivity() instanceof LoginActivity) {
            String mobile = ((LoginActivity) getActivity()).getMobile();
            if (!TextUtils.isEmpty(mobile)) {
                etMobile.setText(mobile);
            }
        }
        ViewUtil.setEditPasswordInputFilter(etUserpwd);
    }


    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
           // if (etMobile.length() >= MOBILE_LENGTH && etUserpwd.length() >= PASSWORD_MIN_LENGTH) {
            if (etMobile.getText().toString().trim().length() > 0) {
                btSubmit.setEnabled(true);
            } else {
                btSubmit.setEnabled(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((LoginActivity) getActivity()).showThirdLoginView(true);

        if (((LoginActivity)getActivity()).getStatus() == 0) {
            ((LoginActivity) getActivity()).setThirdLogin(false);
            ((LoginActivity) getActivity()).showLoginAndRegister();
        }

        if (etMobile != null && etUserpwd != null) {
            if (!TextUtils.isEmpty(etMobile.getText())) {
                etUserpwd.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(etUserpwd, InputMethodManager.SHOW_IMPLICIT);
                }
            } else {
                etMobile.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(etMobile, InputMethodManager.SHOW_IMPLICIT);
                }
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
