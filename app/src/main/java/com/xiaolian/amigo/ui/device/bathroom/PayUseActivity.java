package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
public class PayUseActivity extends UseWayActivity implements IPayUseView {

    @Inject
    IPayUsePresenter<IPayUseView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_booking);
//        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected void setToolbarTitle(TextView textView) {
        textView.setText("付费使用");
    }

    @Override
    protected void setTitle(TextView textView) {
        textView.setText("付费使用");
    }

    @Override
    protected void setToolbarSubTitle(TextView textView) {
        textView.setVisibility(View.GONE);
    }

    @Override
    protected void setSubTitle(TextView textView) {
        textView.setVisibility(View.GONE);
    }

    @Override
    protected void setTips(TextView tip1, TextView tip2, TextView tip3, TextView tip4,
                           TextView tip, RelativeLayout rlTip) {
        tip1.setVisibility(View.GONE);
        tip2.setVisibility(View.GONE);
        tip3.setVisibility(View.GONE);
        tip4.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        rlTip.setVisibility(View.GONE);
    }
}
