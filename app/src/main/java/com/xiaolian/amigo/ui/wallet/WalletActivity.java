package com.xiaolian.amigo.ui.wallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.RelativeGuide;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.funds.WithdrawExplanationRespDTO;
import com.xiaolian.amigo.di.componet.DaggerWalletActivityComponent;
import com.xiaolian.amigo.di.module.WalletActivityModule;
import com.xiaolian.amigo.ui.XLScrollView;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.wallet.intf.IWalletPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWalletView;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.util.Constant;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.xiaolian.amigo.ui.wallet.WithDrawActivity.KEY_CERTIFICATION_STATUS;
import static com.xiaolian.amigo.ui.wallet.WithDrawActivity.KEY_WITHDRAW_DATA;

/**
 * 我的钱包
 *
 * @author zcd
 * @date 17/9/18
 */
public class WalletActivity extends BaseActivity implements IWalletView {


    @Inject
    IWalletPresenter<IWalletView> presenter;

    /**
     * 余额
     */
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    /**
     * 提现入口
     */
    @BindView(R.id.rl_withdrawal)
    RelativeLayout rlWithdrawal;


    @BindView(R.id.rl_bill_record)
    RelativeLayout rlBillRecord ;

    @BindView(R.id.sv_main_container)
    XLScrollView svMainContainer ;


    @BindView(R.id.recharge_line)
    View rechargeLine ;

    @BindView(R.id.line_withdrawal)
    View lineWithdrawal ;

    @BindView(R.id.image_wallet)
    ImageView imageWallet ;
    private TextView tvToolbarTitle,tvTitle;

    private RelativeLayout rlToolBar;
    private View viewLine;



    private DecimalFormat df = new DecimalFormat("###.##");

    private boolean showTile = false;

    /**
     * 在界面未销毁前引导层显示了几次，用来防止进入界面后又一次显示
     */
    private int showNewbieGuideCount  ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
    }


    protected void initView() {
        DaggerWalletActivityComponent.builder()
                .walletActivityModule(new WalletActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build().inject(this);

        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(WalletActivity.this);
        IOverScrollDecor iOverScrollDecor = OverScrollDecoratorHelper.setUpOverScroll(svMainContainer);
        initToolBar();
        svMainContainer.setOnScrollListener((scrollY -> {
            if (scrollY > (tvToolbarTitle.getHeight()) + tvToolbarTitle.getPaddingTop()) {
                setTitleVisiable(View.VISIBLE);
                showTile = true;
            } else {
                setTitleVisiable(View.GONE);
                showTile = false;
            }

        }));

        iOverScrollDecor.setOverScrollUpdateListener((decor, state, offset) -> {
            if (showTile)
                return;
            if (offset < -(tvToolbarTitle.getHeight()) + tvToolbarTitle.getPaddingTop()) {
                setTitleVisiable(View.VISIBLE);
            } else {
                setTitleVisiable(View.GONE);
            }
        });
        showNewbieGuideCount = 0 ;
    }

    protected void initToolBar() {
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle = findViewById(R.id.tv_title);
        rlToolBar = findViewById(R.id.rl_toolbar);
        viewLine = findViewById(R.id.view_line);
        setTitleVisiable(View.GONE);
    }

    private void setTitleVisiable(int visiable) {
        tvTitle.setVisibility(visiable);
        viewLine.setVisibility(visiable);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.requestNetWork();
    }


    private void showNewbieGuide(){
        NewbieGuide.with(this)
                .setLabel("guide1")
                .setShowCounts(3)
                .addGuidePage(GuidePage.newInstance()
                        .setBackgroundColor(getResources().getColor(R.color.colorBlank60))
                        .addHighLight(rlBillRecord  ,HighLight.Shape.RECTANGLE, new RelativeGuide(R.layout.view_guide_simple,
                                Gravity.BOTTOM ,5)))
                .show();
        showNewbieGuideCount++ ;

    }

    /**
     * 充值
     */
    @OnClick({ R.id.rl_recharge})
    void recharge() {
        startActivity(new Intent(this, RechargeActivity.class));
    }

    /**
     * 查看预付金额列表
     */
    @OnClick(R.id.rl_prepay)
    void prepay() {
        startActivity(this, PrepayActivity.class);
    }

    /**
     * 提现
     */
    @OnClick(R.id.rl_withdrawal)
    void withdrawal() {
         presenter.queryWithDraw();
    }


    /**
     * 账单页面
     */
    @OnClick(R.id.rl_bill_record)
    void withdrawalRecord() {
        startActivity(new Intent(this, BalanceDetailListActivity.class));
    }

    @Override
    public void setBalanceText(Double balance) {
        String balanceTxt = String.format(Locale.getDefault() ,"￥%s" ,df.format(balance));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(balanceTxt);
        AbsoluteSizeSpan absoluteSizeSpanSmall = new AbsoluteSizeSpan(14 ,true);
        stringBuilder.setSpan(absoluteSizeSpanSmall ,0 , 1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan absoluteSizeSpanBig = new AbsoluteSizeSpan(29 ,true);
        stringBuilder.setSpan(absoluteSizeSpanBig ,1 , balanceTxt.length() -1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBalance.setText(stringBuilder);
    }

    @Override
    public void setPrepayText(Double prepay) {
//        tvPrepay.setText(String.format(Locale.getDefault(), "¥%s", df.format(prepay)));
    }

    @Override
    public void gotoWithDraw() {
        startActivity(new Intent(this, WithdrawalActivity.class)
                .putExtra(Constant.EXTRA_KEY, df.format(presenter.getChargeBalance()) ));
    }

    @Override
    public void showTimeValidDialog(String title, String remark) {
        AvailabilityDialog dialog = new AvailabilityDialog(this);
        if (TextUtils.isEmpty(title)) {
            dialog.setTip(getString(R.string.withdraw_tip));
        } else {
            dialog.setTip(remark);
        }
        dialog.setType(AvailabilityDialog.Type.WITHDRAW_VALID);
        dialog.setTitle(title);
        dialog.setCancelVisible(false);
        dialog.setOkText(getString(R.string.i_know));
        dialog.setOnOkClickListener(Dialog::dismiss
//                startActivity(new Intent(this, WithdrawalActivity.class)
//                        .putExtra(Constant.EXTRA_KEY, tvBalance.getText().toString().replace("¥", "")))
        );
        dialog.show();
    }

    @Override
    public void setBalancePresentText(Double givingBalance) {
        setBalancePresent(String.format(Locale.getDefault(), "¥%s", df.format(givingBalance)));
    }

    @Override
    public void setWithdrawAvailableText(Double chargeBalance) {
        setWithdrawAvailable(String.format(Locale.getDefault(), "¥%s", df.format(chargeBalance)));
    }



    /**
     * 设置增送的余额
     */
    private void setBalancePresent(String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String tip = getString(R.string.present_balance_colon);
        SpannableString valueSpan = new SpannableString(value);
        valueSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorFullRed)),
                0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tip);
        builder.append(valueSpan);
//        tvBalancePresent.setText(builder);
    }



    /**
     * 可提现余额
     */
    private void setWithdrawAvailable(String value) {
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        String tip = getString(R.string.withdraw_available_colon);
//        SpannableString valueSpan = new SpannableString(value);
//        valueSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorFullRed)),
//                0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.append(tip);
//        builder.append(valueSpan);
//        tvWithdrawAvailable.setText(builder);
    }

    public void showWithDraw() {
        rlWithdrawal.setVisibility(View.VISIBLE);
        rechargeLine.setVisibility(View.GONE);
        lineWithdrawal.setVisibility(View.VISIBLE);

        // 只能显示一次
        if (showNewbieGuideCount == 0) {
            showNewbieGuide();
        }
    }

    @Override
    public void hideWithDraw() {
        rlWithdrawal.setVisibility(View.GONE);
        rechargeLine.setVisibility(View.VISIBLE);
        lineWithdrawal.setVisibility(View.GONE);
    }


    @Override
    public void startWithDraw(WithdrawExplanationRespDTO data , int status) {
        Log.e(TAG, "startWithDraw: data ------" + data.toString() );

        Intent intent = new Intent(this ,WithDrawActivity.class);
        intent.putExtra(KEY_WITHDRAW_DATA  ,data);
        intent.putExtra(KEY_CERTIFICATION_STATUS , status);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
