package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationView;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class UserCertificationActivity extends UserBaseActivity implements IUserCertificationView {

    @Inject
    IUserCertificationPresenter<IUserCertificationView> presenter ;


    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected int setTitle() {
        return R.string.user_certification_title;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_user_certification;
    }

}
