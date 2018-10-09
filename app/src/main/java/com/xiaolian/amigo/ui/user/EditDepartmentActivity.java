package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;

import butterknife.ButterKnife;

public class EditDepartmentActivity extends BaseToolBarActivity{
    @Override
    protected void initInject() {

    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        setMainBackground(R.color.colorBackgroundGray);
    }

    @Override
    protected int setTitle() {
        return 0;
    }

    @Override
    protected int setLayout() {
        return 0;
    }
}
