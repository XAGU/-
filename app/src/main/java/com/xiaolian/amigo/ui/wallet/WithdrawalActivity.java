package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.wallet.adaptor.ChooseWithdrawAdapter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalView;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

/**
 * 提现
 * <p>
 * Created by zcd on 10/10/17.
 */

public class WithdrawalActivity extends WalletBaseActivity implements IWithdrawalView {
    private static final int REQUEST_CODE_CHOOSE_WITHDRAW_WAY = 0x0121;
    private static final int REQUEST_CODE_CHOOSE_WITHDRAW_WAY2 = 0x0122;
    @Inject
    IWithdrawalPresenter<IWithdrawalView> presenter;

    @BindView(R.id.et_amount)
    EditText et_amount;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @BindView(R.id.rl_choose_withdraw_way)
    RelativeLayout rl_choose_withdraw_way;
    // 提现至支付宝
    @BindView(R.id.rl_choose_withdraw_way2)
    RelativeLayout rl_choose_withdraw_way2;

    @BindView(R.id.tv_withdraw_way)
    TextView tv_withdraw_way;
    @BindView(R.id.tv_withdraw_way2)
    TextView tv_withdraw_way2;

    private Long withdrawId;

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
        if (TextUtils.isEmpty(et_amount.getText()) && withdrawId != null) {
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
        if (withdrawId == null) {
            onError("请选择提现账户");
            return;
        }
        presenter.withdraw(et_amount.getText().toString().trim(), withdrawId);
    }

    @OnClick(R.id.rl_choose_withdraw_way)
    void chooseWithdrawWay() {
        startActivityForResult(new Intent(this, ListChooseActivity.class).
                        putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                        ListChooseActivity.ACTION_LIST_WITHDRAW_WAY),
                REQUEST_CODE_CHOOSE_WITHDRAW_WAY);
    }
    @OnClick(R.id.rl_choose_withdraw_way2)
    void chooseWithdrawWay2() {
        startActivityForResult(new Intent(this, ChooseWithdrawActivity.class),
                REQUEST_CODE_CHOOSE_WITHDRAW_WAY2);
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_WITHDRAW_WAY:
                    if (data != null) {
                        tv_withdraw_way.setText(data.getStringExtra(
                                ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ITEM_RESULT));
                    }
                    break;
                case REQUEST_CODE_CHOOSE_WITHDRAW_WAY2:
                    if (data != null) {
                        ChooseWithdrawAdapter.Item item = (ChooseWithdrawAdapter.Item) data.getSerializableExtra(Constant.EXTRA_KEY);
                        tv_withdraw_way2.setText(item.getContent());
                        withdrawId = item.getId();
                    }
                    break;
            }
        }
    }
}
