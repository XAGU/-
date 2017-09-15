package com.xiaolian.amigo.ui.user;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IEditMobilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobileView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 修改手机号
 * @author zcd
 */

public class EditMobileActivity extends UserBaseActivity implements IEditMobileView {

    @Inject
    IEditMobilePresenter<IEditMobileView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mobile);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        mPresenter.onAttach(EditMobileActivity.this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {

    }
}
