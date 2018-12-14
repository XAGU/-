package com.xiaolian.amigo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.ChooseSchoolActivity;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.widget.school.mode.CityBean;
import com.xiaolian.amigo.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.app.Activity.RESULT_OK;

/**
 * 账户注册第二步
 *
 * @author zcd
 * @date 17/9/19
 */

public class RegisterStep2Fragment extends Fragment {

    private static final int REQUEST_CODE_CHOOSE_SCHOOL = 0x1101;

    @BindView(R.id.et_userpwd)
    EditText etUserpwd;

    @BindView(R.id.tv_school)
    TextView tvSchool;

    @BindView(R.id.bt_submit)
    Button btSubmit;

    @OnClick(R.id.bt_submit)
    void register() {
        btSubmit.setEnabled(false);
        if (etUserpwd.getText().toString().trim().length() < 6) {
            ((LoginActivity) getActivity()).onError("请输入至少6位数的密码");
            btSubmit.setEnabled(true);
            return;
        }
        //为绑定第三方账号注册电话号码
        if (((LoginActivity) getActivity()).isThirdLogin()){
            ((LoginActivity) getActivity()).registerForThirdAccount(etUserpwd.getText().toString(),
                    schoolId , btSubmit);
            return;
        }

        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).register(etUserpwd.getText().toString(),
                    schoolId , btSubmit);
        }
    }

    private Long schoolId;

    @OnClick(R.id.tv_school)
    void chooseSchool() {
        Intent intent = new Intent(getActivity(), ChooseSchoolActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_SCHOOL_RESULT);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_SCHOOL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_registry, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_least_six_password), 14, etUserpwd);
        ViewUtil.setEditHintAndSize(getString(R.string.school_hint), 14, tvSchool);
        ViewUtil.setEditPasswordInputFilter(etUserpwd);
        ((LoginActivity) getActivity()).showThirdLoginView(false);

        if (etUserpwd != null) {
            etUserpwd.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etUserpwd, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_SCHOOL) {
                if (data.getExtras() != null) {
                    CityBean item = data.getExtras().getParcelable(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ITEM_RESULT);
                    if (item != null) {
                        tvSchool.setText(item.getCity());
                        schoolId = item.getId();
                    }
                }
            }
            toggleButton();
        }
    }

    @OnTextChanged(R.id.et_userpwd)
    public void onTextEdit() {
        toggleButton();
    }

    private void toggleButton() {
        boolean valid = !TextUtils.isEmpty(tvSchool.getText())
                && !TextUtils.isEmpty(etUserpwd.getText())
                && etUserpwd.getText().length() >= 1;
        btSubmit.setEnabled(valid);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            ((LoginActivity) getActivity()).showThirdLoginView(false);

            if (etUserpwd != null) {
                etUserpwd.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etUserpwd, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}
