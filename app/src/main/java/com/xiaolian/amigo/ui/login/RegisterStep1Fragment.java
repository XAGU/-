package com.xiaolian.amigo.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolian.amigo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册第一步
 * <p>
 * Created by zcd on 9/19/17.
 */

public class RegisterStep1Fragment extends Fragment {

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @OnClick(R.id.bt_submit)
    void gotoNextStep() {
        ((LoginActivity)getActivity()).registerSetp2();
    }

    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @BindView(R.id.et_verification_code)
    EditText et_verification_code;

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
        et_mobile.addTextChangedListener(new TextWatcher() {
            boolean hint;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    // no text, hint is visible
                    hint = true;
                    et_mobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                } else if(hint) {
                    // no hint, text is visible
                    hint = false;
                    et_mobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
