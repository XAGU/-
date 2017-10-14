package com.xiaolian.amigo.ui.wallet;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

/**
 * <p>
 * Created by zcd on 10/10/17.
 */

public class WithdrawalActivity extends WalletBaseActivity implements IWithdrawalView {
    @Inject
    IWithdrawalPresenter<IWithdrawalView> presenter;

    @BindView(R.id.et_amount)
    EditText et_amount;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(WithdrawalActivity.this);
    }

    @Override
    protected int setTitle() {
        return R.string.withdrawal;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_wallet_withdrawal;
    }

    private void toggleButton() {
        if (TextUtils.isEmpty(et_amount.getText())) {
            bt_submit.setEnabled(false);
        } else {
            bt_submit.setEnabled(true);
        }
    }

    @OnTextChanged(R.id.et_amount)
    void onEditTextChange() {
        toggleButton();
    }

    @OnClick(R.id.bt_submit)
    void onSubmit() {
        if (TextUtils.isEmpty(et_amount.getText())) {
            onError("请输入提现金额");
            return;
        }
        presenter.withdraw(et_amount.getText().toString().trim());
    }

    @Override
    public void back() {
        onBackPressed();
    }
}
