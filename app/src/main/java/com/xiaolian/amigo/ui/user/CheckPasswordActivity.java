package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.ICheckPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.ICheckPasswordView;
import com.xiaolian.amigo.util.CommonUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 校验密码
 * <p>
 * Created by zcd on 9/27/17.
 */

public class CheckPasswordActivity extends UserBaseActivity implements ICheckPasswordView{

    private static final int REQUEST_CODE_EDIT_MOBILE = 0x0110;

    @Inject
    ICheckPasswordPresenter<ICheckPasswordView> presenter;

    @BindView(R.id.et_password)
    EditText et_password;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(CheckPasswordActivity.this);

        CommonUtil.showSoftInput(this, et_password);
    }

    @Override
    protected int setTitle() {
        return R.string.edit_mobile;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_check_password;
    }


    @OnClick(R.id.bt_submit)
    void checkPassword() {
        if (TextUtils.isEmpty(et_password.getText())) {
            onError("请输入");
        } else {
            presenter.checkPassword(et_password.getText().toString());
        }
    }

    @Override
    public void gotoChangeMobile() {
        Intent intent;
        intent = new Intent(getApplicationContext(), EditMobileActivity.class);
        startActivityForResult(intent, REQUEST_CODE_EDIT_MOBILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_EDIT_MOBILE) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
