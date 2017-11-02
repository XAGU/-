package com.xiaolian.amigo.ui.bonus;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangeView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 兑换红包
 * @author zcd
 */

public class BonusExchangeActivity extends BonusBaseActivity implements IBonusExchangeView {


    @BindView(R.id.et_change_code)
    EditText et_changeCode;

    @BindView(R.id.bt_submit)
    Button button;

    @Inject
    IBonusExchangePresenter<IBonusExchangeView> presenter;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(BonusExchangeActivity.this);
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
        if (TextUtils.isEmpty(et_changeCode.getText())) {
            onError("请输入兑换码");
            return;
        }
        presenter.exchangeBonus(et_changeCode.getText().toString());
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void backToBonus() {
        startActivity(new Intent(this, BonusActivity.class));
    }
}
