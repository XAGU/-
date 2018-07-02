package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IPayUsePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IPayUseView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 付费使用
 *
 * @author zcd
 * @date 18/6/29
 */
public class PayUseActivity extends BathroomBaseActivity implements IPayUseView {

    @Inject
    IPayUsePresenter<IPayUseView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_shower);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }
}
