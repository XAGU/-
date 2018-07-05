package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodeView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 购买编码
 *
 * @author zcd
 * @date 18/6/29
 */
public class BuyCodeActivity extends UseWayActivity implements IBuyCodeView {

    @Inject
    IBuyCodePresenter<IBuyCodeView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected void setToolbarTitle(TextView textView) {
        textView.setText("购买编码");
    }

    @Override
    protected void setTitle(TextView textView) {
        textView.setText("购买编码");
    }

    @Override
    protected void setToolbarSubTitle(TextView textView) {
        textView.setText("购买记录");
        textView.setOnClickListener(v -> onSubtitleClick());
    }

    @Override
    protected void setSubTitle(TextView textView) {
        textView.setText("购买记录");
        textView.setOnClickListener(v -> onSubtitleClick());
    }

    @Override
    protected void setTips(TextView tip1, TextView tip2, TextView tip3, TextView tip4,
                           TextView tip, RelativeLayout rlTip) {
        tip1.setText(getString(R.string.buy_code_tip1));
        tip2.setText(getString(R.string.buy_code_tip2));
        tip3.setText(getString(R.string.buy_code_tip3));
        tip4.setText(getString(R.string.buy_code_tip4));
        tip.setText("编码使用说明");
    }

    private void onSubtitleClick() {
        startActivity(new Intent(this, BuyRecordActivity.class));
    }
}
