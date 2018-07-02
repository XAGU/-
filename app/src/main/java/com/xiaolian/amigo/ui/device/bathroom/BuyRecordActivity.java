package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyRecordView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 购买记录
 *
 * @author zcd
 * @date 18/6/29
 */
public class BuyRecordActivity extends BathroomBaseActivity implements IBuyRecordView {

    @Inject
    IBuyRecordPresenter<IBuyRecordView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_shower);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }
}
