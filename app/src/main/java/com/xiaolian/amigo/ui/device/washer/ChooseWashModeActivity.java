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
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.WasherModeDialog;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 选择洗衣机模式
 * <p>
 * Created by zcd on 18/1/12.
 */

public class ChooseWashModeActivity extends WasherBaseActivity implements IChooseWashModeView {

    @Inject
    IChooseWashModePresenter<IChooseWashModeView> presenter;

    private static final int CHOOSE_BONUS_CODE = 0x0010;

    private List<ChooseWashModeAdapter.WashModeItem> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private WasherModeDialog dialog;
    private ChooseWashModeAdapter adapter;

    private Long defaultBonusId;
    private String defaultBonusDescription;
    private Double defaultBonusAmount;
    private Long chosenBonusId;
    private String chosenBonusDescription;
    private Double chosenBonusAmount;
    private Double chosenOriginalPirce;

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
        presenter.getWasherMode();
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
                showModeDetailDialog(items.get(position).getName(),
                        presenter.getDeviceNo(), items.get(position).getPrice(), items.get(position).getMode());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void showModeDetailDialog(String modeDesc, String deviceNo, String price, Integer mode) {
        if (dialog == null) {
            dialog = new WasherModeDialog(this);
        }
        dialog.setConfirmClickListener(() -> onModeConfirm(modeDesc,
                deviceNo, dialog.getPrice(), mode));
        if (!ObjectsCompat.equals(defaultBonusId, Constant.INVALID_ID) && !TextUtils.isEmpty(defaultBonusDescription)) {
            dialog.setBonus(defaultBonusDescription);
            dialog.setBonusClickListener(this::chooseBonus);
            chosenOriginalPirce = Double.valueOf(price);
            if (chosenOriginalPirce - defaultBonusAmount <= 0) {
                dialog.setSubmit("0");
            } else {
                dialog.setSubmit(String.valueOf(chosenOriginalPirce - defaultBonusAmount));
            }
        } else {
            dialog.setSubmit(price);
        }
        dialog.setMode(modeDesc, price);
        dialog.show();
        dialog.setOnDismissListener(dialog1 -> {
            chosenBonusId = Constant.INVALID_ID;
            chosenBonusDescription = null;
        });
    }

    private void chooseBonus() {
        Intent intent = new Intent(this, BonusActivity.class);
        intent.putExtra(BonusActivity.INTENT_KEY_BONUS_ACTION, BonusActivity.ACTION_CHOOSE);
        intent.putExtra(BonusActivity.INTENT_KEY_BONUS_DEVICE_TYPE, Device.WASHER.getType());
        startActivityForResult(intent, CHOOSE_BONUS_CODE);
    }

    private void onModeConfirm(String modeDesc, String deviceNo, String price, Integer mode) {
        Long bonusId;
        if (!ObjectsCompat.equals(chosenBonusId, Constant.INVALID_ID)) {
            bonusId = chosenBonusId;
        } else if (!ObjectsCompat.equals(defaultBonusId, Constant.INVALID_ID)
                && TextUtils.isEmpty(chosenBonusDescription)) {
            bonusId = defaultBonusId;
        } else {
            bonusId = null;
        }
        presenter.payAndGenerate(bonusId, modeDesc, deviceNo, price, mode);
    }

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
//        bt_submit = findViewById(R.id.bt_submit);
//        bt_submit.setOnClickListener(v -> submit());
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideLoading();
            onBackPressed();
        });
    }

    private void submit() {
        if (adapter.getLastChoosePosition() != -1) {
            String price = items.get(adapter.getLastChoosePosition()).getPrice();
            String mode = items.get(adapter.getLastChoosePosition()).getName();
            startActivity(new Intent(this, WasherQRCodeActivity.class)
                    .putExtra(WasherContent.KEY_MODE_DESC, mode)
                    .putExtra(WasherContent.KEY_PRICE, price));
        }
    }

    @Override
    protected void setUp() {
        presenter.setDeviceNo(getIntent().getStringExtra(WasherContent.KEY_DEVICE_NO));
        defaultBonusId = getIntent().getLongExtra(WasherContent.KEY_BONUS_ID, Constant.INVALID_ID);
        defaultBonusAmount = getIntent().getDoubleExtra(WasherContent.KEY_BONUS_AMOUNT, 0.0);
        defaultBonusDescription = getIntent().getStringExtra(WasherContent.KEY_BONUS_DESC);
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
                chosenBonusId = Constant.INVALID_ID;
                chosenBonusAmount = 0.0;
                chosenBonusDescription = getString(R.string.not_use_bonus);
                refreshBonusDialog();
            } else if (resultCode == RESULT_OK) {
                BonusAdaptor.BonusWrapper choosedBonus = (BonusAdaptor.BonusWrapper) data.getSerializableExtra(BonusActivity.INTENT_KEY_BONUS_RESULT);
                if (choosedBonus != null) {
                    chosenBonusId = choosedBonus.getId();
                    chosenBonusAmount = choosedBonus.getAmount();
                    chosenBonusDescription = choosedBonus.getDescription();
                    refreshBonusDialog();
                }
            }
        }
    }

    private void refreshBonusDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.setBonus(chosenBonusDescription);
            if (chosenOriginalPirce - chosenBonusAmount <= 0) {
                dialog.setSubmit("0");
            } else {
                dialog.setSubmit(String.valueOf(chosenOriginalPirce - chosenBonusAmount));
            }
        }
    }

    @Override
    public void gotoShowQRCodeView(String data, String price, String modeDesc) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        startActivity(new Intent(this, WasherQRCodeActivity.class)
                .putExtra(WasherContent.KEY_PRICE, String.valueOf(chosenOriginalPirce))
                .putExtra(WasherContent.KEY_MODE_DESC, modeDesc)
                .putExtra(WasherContent.KEY_QR_CODE_URL, data));
    }
}
