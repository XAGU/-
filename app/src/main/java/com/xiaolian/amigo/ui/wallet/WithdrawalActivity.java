package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ObjectsCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.user.PasswordVerifyActivity;
import com.xiaolian.amigo.ui.wallet.adaptor.ChooseWithdrawAdapter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 提现
 *
 * @author zcd
 * @date 17/10/10
 */

public class WithdrawalActivity extends WalletBaseActivity implements IWithdrawalView {
    private static final int REQUEST_CODE_CHOOSE_WITHDRAW_WAY = 0x0121;
    private static final int REQUEST_CODE_CHOOSE_WITHDRAW_WAY2 = 0x0122;

    private static final int REQUEST_CODE_PASSWORD_VERIFY = 0x0123;
    @Inject
    IWithdrawalPresenter<IWithdrawalView> presenter;

    @BindView(R.id.et_amount)
    EditText etAmount;

    @BindView(R.id.bt_submit)
    Button btSubmit;

    @BindView(R.id.rl_choose_withdraw_way)
    RelativeLayout rlChooseWithdrawWay;
    /**
     * 提现至支付宝
     */
    @BindView(R.id.rl_choose_withdraw_way2)
    RelativeLayout rlChooseWithdrawWay2;

    @BindView(R.id.tv_withdraw_way)
    TextView tvWithdrawWay;
    @BindView(R.id.tv_withdraw_way2)
    TextView tvWithdrawWay2;
    @BindView(R.id.tv_withdraw_all)
    TextView tvWithdrawAll;
    /**
     * 可提现金额
     */
    @BindView(R.id.tv_withdraw_available)
    TextView tvWithdrawAvailable;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    private Long withdrawId;
    private String balance;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        setUpEditText();
        presenter.onAttach(WithdrawalActivity.this);
        tvWithdrawAvailable.setText(getString(R.string.withdraw_available, balance));
        presenter.requestAccounts(PayWay.ALIAPY.getType());

        CommonUtil.showSoftInput(this, etAmount);
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            balance = getIntent().getStringExtra(Constant.EXTRA_KEY);
        }
    }

    @Override
    protected int setTitle() {
        return R.string.withdrawal;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_wallet_withdrawal;
    }

    /**
     * 设置输入框只能输入2位小数
     */
    private void setUpEditText() {
        etAmount.addTextChangedListener(new TextWatcher() {
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
                if (posDot <= 0) {
                    return;
                }
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    private void toggleButton() {
        if (TextUtils.isEmpty(etAmount.getText())
                || withdrawId == null) {
            btSubmit.setEnabled(false);
        } else {
            btSubmit.setEnabled(true);
        }
    }

    @OnTextChanged(R.id.et_amount)
    void onEditTextChange() {
        toggleButton();
    }

    @OnClick(R.id.bt_submit)
    void onSubmit() {
        if (TextUtils.isEmpty(etAmount.getText())) {
            onError("请输入提现金额");
            return;
        }
        if (withdrawId == null) {
            onError("请选择提现账户");
            return;
        }
        double withdrawAmount;
        double balanceAmount;
        try {
            withdrawAmount = Double.valueOf(etAmount.getText().toString());
            balanceAmount = Double.valueOf(balance);
        } catch (NumberFormatException e) {
            withdrawAmount = 0.0;
            balanceAmount = 0.0;
        }
        if (balanceAmount < withdrawAmount) {
            onError("余额不足");
            return;
        }
        // 提现金额小于0.1
        if (withdrawAmount < 0.1) {
            onError(R.string.withdraw_amount_error_tip);
            return;
        }
        // 正常情况 余额大于10
        if (balanceAmount >= 10) {
            if (withdrawAmount < 10) {
                onError(R.string.withdraw_amount_error_tip);
                return;
            }
        }
        // 余额低于10元
        else {
            // 不是全部提现
            if (!TextUtils.equals(String.valueOf(withdrawAmount), String.valueOf(balanceAmount))) {
                onError(R.string.withdraw_amount_error_tip);
                return;
            }
        }

        //  进入密码验证
        Intent intent = new Intent(this,PasswordVerifyActivity.class);
        intent.putExtra("type",PasswordVerifyActivity.TYPE_MONEY_RETURN);
        startActivityForResult(intent,REQUEST_CODE_PASSWORD_VERIFY);
    }

    @OnClick(R.id.tv_withdraw_all)
    void withdrawAll() {
        etAmount.setText(balance);
        etAmount.setSelection(balance.length());
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
    public void gotoWithdrawDetail(Long id) {
        startActivity(new Intent(this, WithdrawalDetailActivity.class)
                .putExtra(Constant.EXTRA_KEY, id));
        finish();
    }

    @Override
    public void showWithdrawAccount(String accountName, Long id) {
        withdrawId = id;
        tvWithdrawWay2.setText(accountName);
        toggleButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_WITHDRAW_WAY:
                    if (data != null) {
                        tvWithdrawWay.setText(data.getStringExtra(
                                ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ITEM_RESULT));
                    }
                    break;
                case REQUEST_CODE_CHOOSE_WITHDRAW_WAY2:
                    if (data != null) {
                        ChooseWithdrawAdapter.Item item = (ChooseWithdrawAdapter.Item) data.getSerializableExtra(Constant.EXTRA_KEY);
                        tvWithdrawWay2.setText(item.getContent());
                        withdrawId = item.getId();
                        toggleButton();
                    }
                    break;
                case REQUEST_CODE_PASSWORD_VERIFY://密码验证成功后才能进入退款流程
                    presenter.withdraw(etAmount.getText().toString().trim(), tvWithdrawWay2.getText().toString().trim(),
                            withdrawId);
                default:
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WalletEvent event) {
        switch (event.getEventType()) {
            case DELETE_ACCOUNT:
                if (ObjectsCompat.equals(event.getObject(), this.withdrawId)) {
                    this.withdrawId = null;
                    tvWithdrawWay2.setText("");
                    presenter.clearAccount();
                    toggleButton();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



}
