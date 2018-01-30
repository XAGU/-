package com.xiaolian.amigo.ui.wallet;

import android.text.TextUtils;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.ui.wallet.intf.IAddAccountPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IAddAccountView;
import com.xiaolian.amigo.ui.widget.ClearableEditText;
import com.xiaolian.amigo.util.CommonUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加账户
 *
 * @author zcd
 * @date 17/10/27
 */

public class AddAccountActivity extends WalletBaseActivity implements IAddAccountView {
    @Inject
    IAddAccountPresenter<IAddAccountView> presenter;

    @BindView(R.id.et_account)
    ClearableEditText etAccount;
    @BindView(R.id.et_real_name)
    ClearableEditText etRealName;
    @BindView(R.id.bt_submit)
    Button btSubmit;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(AddAccountActivity.this);

        CommonUtil.showSoftInput(this, etAccount);
    }

    @OnClick(R.id.bt_submit)
    public void add() {
        if (TextUtils.isEmpty(etAccount.getText())) {
            onError(etAccount.getHint().toString());
            return;
        }
        if (TextUtils.isEmpty(etRealName.getText())) {
            onError(etRealName.getHint().toString());
            return;
        }
        presenter.addAccount(etAccount.getText().toString(),
                etRealName.getText().toString(), PayWay.ALIAPY.getType());
    }

    @Override
    protected int setTitle() {
        return R.string.add_alipay_account;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_add_account;
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
