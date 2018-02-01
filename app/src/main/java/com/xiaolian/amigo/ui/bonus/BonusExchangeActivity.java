package com.xiaolian.amigo.ui.bonus;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangeView;
import com.xiaolian.amigo.util.CommonUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 兑换代金券
 *
 * @author zcd
 * @date 17/9/18
 */

public class BonusExchangeActivity extends BonusBaseActivity implements IBonusExchangeView {


    @BindView(R.id.et_change_code)
    EditText etChangeCode;

    @BindView(R.id.bt_submit)
    Button button;

    @Inject
    IBonusExchangePresenter<IBonusExchangeView> presenter;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(BonusExchangeActivity.this);

        CommonUtil.showSoftInput(this, etChangeCode);
    }

    @Override
    protected int setTitle() {
        return R.string.exchange_bonus;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_bonus_exchange;
    }

    @OnClick(R.id.bt_submit)
    public void exchange() {
        if (TextUtils.isEmpty(etChangeCode.getText())) {
            onError(getString(R.string.please_enter_exchange_code));
            return;
        }
        presenter.exchangeBonus(etChangeCode.getText().toString());
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void backToBonus() {
        startActivity(new Intent(this, BonusActivity.class));
    }
}
