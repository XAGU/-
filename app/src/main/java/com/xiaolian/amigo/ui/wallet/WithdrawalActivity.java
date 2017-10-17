package com.xiaolian.amigo.ui.wallet;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
        setUpEditText();
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

    // 设置输入框只能输入2位小数
    private void setUpEditText() {
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
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
