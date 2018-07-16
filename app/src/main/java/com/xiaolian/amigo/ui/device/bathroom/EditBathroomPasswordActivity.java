package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IEditBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IEditBathroomPasswordView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author zcd
 * @date 18/7/13
 */
public class EditBathroomPasswordActivity extends BathroomBaseActivity
        implements IEditBathroomPasswordView {

    @Inject
    IEditBathroomPasswordPresenter<IEditBathroomPasswordView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bathroom_password);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);

//        initView();
    }

}
