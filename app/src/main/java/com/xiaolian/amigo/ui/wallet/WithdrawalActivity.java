package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeTypeListRespDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatBindRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.user.EditProfileActivity;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.user.PasswordVerifyActivity;
import com.xiaolian.amigo.ui.user.ThirdBindActivity;
import com.xiaolian.amigo.ui.wallet.adaptor.ChooseWithdrawAdapter;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeTypeAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.xiaolian.amigo.ui.user.EditProfileActivity.WECHAT_BIND;

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
    /**
     * 提现方式
     */
    @BindView(R.id.withdraw_type)
    RecyclerView withdrawType ;

    /**
     * 提现微信
     */

    @BindView(R.id.ll_wx)
    LinearLayout llWx ;

    @BindView(R.id.edit_name)
    EditText editName ;

    @BindView(R.id.rl_choose_withdraw_way_xw)
    RelativeLayout rlChooseWithdrawWayWX ;

    @BindView(R.id.tv_withdraw_way_wx)
    TextView tvWithdrawWayWX ;

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

    private RechargeTypeAdaptor typeAdaptor;
    /**
     * 充值方式列表
     */
    List<RechargeTypeAdaptor.RechargeWrapper> rechargeTypes = new ArrayList<>();

    private int rechargeSelectedPosition = -1;
    private int rechargeTypeSelectedPosition = 0;

    private String appid ;

    private String wechatCode ;

    private IWXAPI mWxApi ;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        setUpEditText();
        presenter.onAttach(WithdrawalActivity.this);
        tvWithdrawAvailable.setText(getString(R.string.withdraw_available, balance));
        presenter.requestAccounts(PayWay.ALIAPY.getType());
        presenter.withdrawType();
        CommonUtil.showSoftInput(this, etAmount);
        initWithdrawType();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EditProfileActivity.Event event) {
        switch (event.getType()) {
            case WECHAT_CODE:
                wechatCode = (String) event.getMsg();
                break;
            default:
                break;
        }
    }

    private void initWithdrawType(){
        typeAdaptor = new RechargeTypeAdaptor(this, R.layout.item_recharge_type, rechargeTypes);
        withdrawType.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        withdrawType.addItemDecoration(new GridSpacesItemDecoration(2, ScreenUtils.dpToPxInt(this, 10), false));
        typeAdaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (rechargeTypeSelectedPosition != -1) {
                    rechargeTypes.get(rechargeTypeSelectedPosition).setSelected(false);
                    rechargeTypes.get(position).setSelected(true);
                } else {
                    rechargeTypes.get(position).setSelected(true);
                }
                rechargeTypeSelectedPosition = position;
                typeAdaptor.notifyDataSetChanged();
                toggleSubmitButton();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        withdrawType.setAdapter(typeAdaptor);
    }

    private void toggleSubmitButton() {
        if (rechargeSelectedPosition != -1 && rechargeTypeSelectedPosition != -1) {
            btSubmit.setEnabled(true);
        }

        if (rechargeTypeSelectedPosition == 0){
            choseAPay();
        }else{
            choseWX();
            // 获取学校appId服务号
            if (TextUtils.isEmpty(appid)) {
                presenter.getWechatAppid();
            }
        }
    }

    private void choseAPay(){
        rlChooseWithdrawWay2.setVisibility(View.VISIBLE);
        llWx.setVisibility(View.GONE);
    }

    private void choseWX(){
        rlChooseWithdrawWay2.setVisibility(View.GONE);
        llWx.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            balance = getIntent().getStringExtra(Constant.EXTRA_KEY);
        }
    }

    @OnClick(R.id.rl_choose_withdraw_way_xw)
    public void chooseWX(){
            new Thread(()->{
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "amigo_wx_login";
                mWxApi = WXAPIFactory.createWXAPI(this,appid, false);
                mWxApi.sendReq(req);
            }).start();
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

        if (rechargeSelectedPosition == 1){
            if (TextUtils.isEmpty(editName.getText())){
                onError("请填写微信真实姓名");
                return ;
            }
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
    public void setAppid(String appId) {
        this.appid = appId ;
    }

    @Override
    public void showTypeList(QueryRechargeTypeListRespDTO data) {
        rechargeTypes.clear();
        List<Integer> typeList = data.getRechargeTypes();
        if (typeList != null && typeList.size() > 0){
            for (Integer type : typeList){
                if (type == PayWay.ALIAPY.getType()){
                    rechargeTypes.add(new RechargeTypeAdaptor.RechargeWrapper(PayWay.ALIAPY.getType(), PayWay.ALIAPY.getDrawableRes(), "支付宝", true));
                }else if (type == PayWay.WECHAT.getType()){
                    rechargeTypes.add(new RechargeTypeAdaptor.RechargeWrapper(PayWay.WECHAT.getType(), PayWay.WECHAT.getDrawableRes(), "微信", false));
                }
            }
            typeAdaptor.notifyDataSetChanged();
        }
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
                    if (rechargeSelectedPosition == 0) {
                        presenter.withdraw(etAmount.getText().toString().trim(), tvWithdrawWay2.getText().toString().trim(),
                                withdrawId);
                    }else if (rechargeSelectedPosition == 1){
                        presenter.wechatWithdraw(etAmount.getText().toString().trim() , appid ,editName.getText().toString().trim());
                    }

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
