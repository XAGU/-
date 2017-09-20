package com.xiaolian.amigo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.component.PasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录Fragment
 * <p>
 * Created by zcd on 9/19/17.
 */

public class LoginFragment extends Fragment {

    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @BindView(R.id.et_userpwd)
    PasswordEditText et_userpwd;

    @OnClick(R.id.tv_password_retrieval_link)
    void gotoPasswordRetrievalSetp1() {
        Intent intent = new Intent(getContext(), PasswordRetrievalStep1Activity.class);
        startActivity(intent);
    }

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @OnClick(R.id.bt_submit)
    void login() {
        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity)getActivity()).login(et_mobile.getText().toString(), et_userpwd.getText().toString());
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
        String mobileHint = getString(R.string.mobile_hint);
        SpannableString mobileSpan = new SpannableString(mobileHint);
        mobileSpan.setSpan(new AbsoluteSizeSpan(14, true), 0, mobileHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_mobile.setHint(mobileSpan);
        String passwordHint = getString(R.string.password_hint);
        SpannableString passwordSpan = new SpannableString(passwordHint);
        passwordSpan.setSpan(new AbsoluteSizeSpan(14, true), 0, passwordHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_userpwd.setHint(passwordSpan);
        TextChange textChange = new TextChange();
        et_mobile.addTextChangedListener(textChange);
        et_userpwd.addTextChangedListener(textChange);
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
            if (et_mobile.length() == 11 && et_userpwd.length() > 0) {
                bt_submit.setEnabled(true);
            } else {
                bt_submit.setEnabled(false);
            }
        }
    }
}
