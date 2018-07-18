package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IChangeBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IChangeBathroomPasswordView;
import com.xiaolian.amigo.ui.widget.ClearableEditText;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@Deprecated
public class ChangeBathroomPasswordActivity extends UserBaseActivity implements IChangeBathroomPasswordView {

    @Inject
    IChangeBathroomPasswordPresenter<IChangeBathroomPasswordView> presenter ;

    @BindView(R.id.et_old_password)
    ClearableEditText etOldPassword;
    @BindView(R.id.et_new_password)
    ClearableEditText etNewPassword;
    @BindView(R.id.et_new_password_again)
    ClearableEditText etNewPasswordAgain;
    @BindView(R.id.bt_submit)
    Button btSubmit;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setMainBackground(R.color.colorBackgroundGray);

        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_old_password) ,14 ,etOldPassword);
        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_new_password) , 14 , etNewPassword);
        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_new_password_again) , 14 , etNewPasswordAgain);

        ViewUtil.setEditPasswordInputFilter(etOldPassword);
        ViewUtil.setEditPasswordInputFilter(etNewPasswordAgain);
        ViewUtil.setEditPasswordInputFilter(etNewPassword);

        CommonUtil.showSoftInput(this ,etOldPassword);
        setToolbarSubTitle(getString(R.string.find_password), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeBathroomPasswordActivity.this ,FindBathroomPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public  void onclick(){

    }


    @Override
    protected int setTitle() {
        return R.string.save;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_change_bathroom_password;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

}
