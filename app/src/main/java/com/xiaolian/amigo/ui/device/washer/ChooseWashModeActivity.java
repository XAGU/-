package com.xiaolian.amigo.ui.device.washer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.bonus.BonusActivity;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.device.washer.intf.IChooseWashModePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IChooseWashModeView;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.WasherModeDialog;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.xiaolian.amigo.ui.bonus.BonusActivity.INTENT_IS_USE_BONUS;
import static com.xiaolian.amigo.ui.device.washer.WasherContent.KEY_TYPE;
import static com.xiaolian.amigo.util.Constant.FROM_LOCATION;
import static com.xiaolian.amigo.util.Constant.WASH_DRYER;

/**
 * 选择洗衣机模式
 *
 * @author zcd
 * @date 18/1/12
 */

public class ChooseWashModeActivity extends WasherBaseActivity implements IChooseWashModeView {

    @Inject
    IChooseWashModePresenter<IChooseWashModeView> presenter;

    private static final int CHOOSE_BONUS_CODE = 0x0110;
    private static final int REQUEST_CODE_RECHARGE = 0x0111;

    private List<ChooseWashModeAdapter.WashModeItem> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private WasherModeDialog dialog;
    private ChooseWashModeAdapter adapter;

    private Long defaultBonusId;
    private String defaultBonusDescription;
    private Double defaultBonusAmount;
    /**
     * 为null表示未选择，为－1表示不使用红包
     **/
    private Long chosenBonusId;
    private String chosenBonusDescription;
    private Double chosenBonusAmount;
    private Double chosenOriginalPirce;
    private String chosenModeDesc;
    private Integer chosenMode;
    private Double balance;
    private int type ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_wash_mode);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setUp();
        bindView();
        initAdapter();
        initRecyclerView();
        if (type == Constant.WASH_DRYER) {
            presenter.getWasherMode();
        }else {
            presenter.getDryerMode();
        }
    }

    private void initAdapter() {
        adapter = new ChooseWashModeAdapter(this, R.layout.item_choose_wash_mode, items);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (adapter.getLastChoosePosition() != -1) {
                    items.get(adapter.getLastChoosePosition()).setChoose(false);
                    items.get(position).setChoose(true);
                } else {
                    items.get(position).setChoose(true);
                }
                adapter.setLastChoosePosition(position);
                adapter.notifyDataSetChanged();
//                toggleSubmitButton();
                showModeDetailDialog(items.get(position).getName(), items.get(position).getPrice(), items.get(position).getMode());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void showModeDetailDialog(String modeDesc, String price, Integer mode) {
        this.chosenModeDesc = modeDesc;
        this.chosenMode = mode;
        this.chosenOriginalPirce = Double.valueOf(price);
        if (dialog == null) {
            dialog = new WasherModeDialog(this);
        }
        setDialogLayout();
        dialog.setMode(modeDesc, price);
        dialog.show();
        dialog.setOnDismissListener(dialog1 -> {
            chosenBonusId = null;
            chosenBonusDescription = null;

            clearChooseStatus();
        });
    }

    private void setDialogLayout() {
        boolean needRecharge = false;
        if (chosenBonusId == null) {
            chosenBonusId = defaultBonusId;
            chosenBonusAmount = defaultBonusAmount;
            chosenBonusDescription = defaultBonusDescription;
        }
        if (type == WASH_DRYER){
            dialog.setTvTitle("*请勿与其他人同时扫描支付相同的洗衣机");
        }else{
            dialog.setTvTitle("*请勿与其他人同时扫描支付相同的烘干机");
        }
        if (!ObjectsCompat.equals(chosenBonusId, Constant.INVALID_ID) && !TextUtils.isEmpty(chosenBonusDescription)) {
            // 有红包
            dialog.setBonus(chosenBonusDescription);
            dialog.setBonusClickListener(this::chooseBonus);
            if (chosenOriginalPirce - chosenBonusAmount <= 0) {
                // 红包金额大于价格
                dialog.setSubmit(0.0);
            } else {
                // 红包金额小于价格
                if (chosenOriginalPirce - chosenBonusAmount > balance) {
                    // 红包金额加上余额小于价格
                    dialog.setSubmit(getString(R.string.to_recharge));
                    needRecharge = true;
                } else {
                    dialog.setSubmit(chosenOriginalPirce - chosenBonusAmount);
                }
            }
        } else {
            // 没有红包
            if (chosenOriginalPirce > balance) {
                dialog.setSubmit(getString(R.string.to_recharge));
                needRecharge = true;
            } else {
                dialog.setSubmit(chosenOriginalPirce);
            }
        }
        if (needRecharge) {
            dialog.setConfirmClickListener(this::gotoRecharge);
        } else {
            dialog.setConfirmClickListener(() -> onModeConfirm(chosenModeDesc, dialog.getPrice(), chosenMode));
        }
    }

    private void gotoRecharge() {
        startActivityForResult(new Intent(this, RechargeActivity.class)
                .putExtra(FROM_LOCATION ,"洗衣机"),
                REQUEST_CODE_RECHARGE);
    }

    private void clearChooseStatus() {
        if (adapter.getLastChoosePosition() != -1) {
            items.get(adapter.getLastChoosePosition()).setChoose(false);
            adapter.setLastChoosePosition(-1);
            adapter.notifyDataSetChanged();
        }
    }

    private void chooseBonus() {
        Intent intent = new Intent(this, BonusActivity.class);
        intent.putExtra(BonusActivity.INTENT_KEY_BONUS_ACTION, BonusActivity.ACTION_CHOOSE);
        intent.putExtra(BonusActivity.INTENT_KEY_BONUS_DEVICE_TYPE, type);
        startActivityForResult(intent, CHOOSE_BONUS_CODE);
    }

    private void onModeConfirm(String modeDesc, Double price, Integer mode) {
        if (price == null || mode == null) {
            return;
        }
        Long bonusId;
        if (ObjectsCompat.equals(chosenBonusId, Constant.INVALID_ID)) {
            bonusId = null;
        } else {
            bonusId = chosenBonusId;
        }
        presenter.payAndGenerate(bonusId, modeDesc, price, mode);
    }

    @SuppressWarnings("unused")
    private void toggleSubmitButton() {
//        if (adapter.getLastChoosePosition() == -1) {
//            return;
//        }
//        if (bt_submit.getVisibility() == View.GONE) {
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
//            lp.bottomMargin = 0;
//            recyclerView.setLayoutParams(lp);
//            bt_submit.setVisibility(View.VISIBLE);
//        }
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        builder.append("支付");
//        String priceText = String.format("%s元", items.get(adapter.getLastChoosePosition()).getPrice());
//        SpannableString buttonSpan = new SpannableString(priceText);
//        buttonSpan.setSpan(new AbsoluteSizeSpan(
//                        DimentionUtils.convertSpToPixels(18, this)), 0, priceText.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.append(buttonSpan);
//        builder.append("，开始使用");
//        bt_submit.setText(builder);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(42));
    }

    private void bindView() {
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideLoading();
            onBackPressed();
        });
    }

    @SuppressWarnings("unused")
    private void submit() {
        if (adapter.getLastChoosePosition() != -1) {
            String price = items.get(adapter.getLastChoosePosition()).getPrice();
            String mode = items.get(adapter.getLastChoosePosition()).getName();
            startActivity(new Intent(this, WasherQrCodeActivity.class)
                    .putExtra(WasherContent.KEY_MODE_DESC, mode)
                    .putExtra(WasherContent.KEY_PRICE, price)
                    .putExtra(KEY_TYPE ,type));
        }
    }


    @Override
    protected void setUp() {
        if (getIntent() != null) {
            presenter.setDeviceNo(getIntent().getStringExtra(WasherContent.KEY_DEVICE_NO));
            defaultBonusId = getIntent().getLongExtra(WasherContent.KEY_BONUS_ID, Constant.INVALID_ID);
            defaultBonusAmount = getIntent().getDoubleExtra(WasherContent.KEY_BONUS_AMOUNT, 0.0);
            defaultBonusDescription = getIntent().getStringExtra(WasherContent.KEY_BONUS_DESC);
            balance = getIntent().getDoubleExtra(WasherContent.KEY_BALANCE, -1.0);
            type = getIntent().getIntExtra(KEY_TYPE ,4);
        }
        if (balance == null || balance < 0) {
            balance = presenter.getLocalBalance();
        }
    }

    @Override
    public void addMore(List<ChooseWashModeAdapter.WashModeItem> items) {
        this.items.addAll(items);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_BONUS_CODE) {
            if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    if (!data.getBooleanExtra(INTENT_IS_USE_BONUS , true)) {
                        chosenBonusId = Constant.INVALID_ID;
                        chosenBonusAmount = 0.0;
                        chosenBonusDescription = getString(R.string.not_use_bonus);
                        refreshBonusDialog();
                    }
                }
            } else if (resultCode == RESULT_OK) {
                if (data != null) {
                    BonusAdaptor.BonusWrapper choosedBonus = (BonusAdaptor.BonusWrapper) data.getSerializableExtra(BonusActivity.INTENT_KEY_BONUS_RESULT);
                    if (choosedBonus != null) {
                        chosenBonusId = choosedBonus.getId();
                        chosenBonusAmount = choosedBonus.getAmount();
                        chosenBonusDescription = choosedBonus.getDescription();
                        refreshBonusDialog();
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_RECHARGE) {
            presenter.getBalance();
        }
    }

    private void refreshBonusDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.setBonus(chosenBonusDescription);
            setDialogLayout();
//            if (chosenOriginalPirce - chosenBonusAmount <= 0) {
//                dialog.setSubmit(0.0);
//            } else {
//                dialog.setSubmit(chosenOriginalPirce - chosenBonusAmount);
//            }
        }
    }

    @Override
    public void gotoShowQRCodeView(String data, String modeDesc) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        startActivity(new Intent(this, WasherQrCodeActivity.class)
                .putExtra(WasherContent.KEY_PRICE, String.valueOf(chosenOriginalPirce))
                .putExtra(WasherContent.KEY_MODE_DESC, modeDesc)
                .putExtra(WasherContent.KEY_QR_CODE_URL, data)
                .putExtra(KEY_TYPE , type));
    }

    @Override
    public void refreshBalance(Double balance) {
        this.balance = balance;
        if (dialog != null && dialog.isShowing()) {
            setDialogLayout();
        }
    }
}
