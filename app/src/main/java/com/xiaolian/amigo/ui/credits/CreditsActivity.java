package com.xiaolian.amigo.ui.credits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.credits.intf.ICreditsPresenter;
import com.xiaolian.amigo.ui.credits.intf.ICreditsView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.util.DimentionUtils;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * 我的积分
 *
 * @author zcd
 * @date 18/2/23
 */
public class CreditsActivity extends CreditsBaseActivity implements ICreditsView {
    @Inject
    ICreditsPresenter<ICreditsView> presenter;

    private List<CreditsAdapter.CreditsItem> items = new ArrayList<>();
    private CreditsAdapter adapter;
    private TextView tvCredits;
    private RecyclerView recyclerView;
    private AvailabilityDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        bindView();
        initRecyclerView();
        presenter.getCredits();
        presenter.getRules();
    }

    private void initRecyclerView() {
        adapter = new CreditsAdapter(this, R.layout.item_point, items);
        adapter.setExchangeClickListener((bonusId, deviceType, bonusAmount, pointAmount) ->
                presenter.checkForExchange(bonusId, deviceType, bonusAmount, pointAmount));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new GridSpacesItemDecoration(2,
                ScreenUtils.dpToPxInt(this, 10), false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 确认积分兑换
     */
    private void onExchange(Long bonusId, Integer credits, Integer deviceType) {
        presenter.exchange(bonusId, credits, deviceType);
    }

    private void bindView() {
        recyclerView = findViewById(R.id.recyclerView);
        tvCredits = findViewById(R.id.tv_credits);
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    public void renderRules(List<CreditsAdapter.CreditsItem> items) {
        this.items.addAll(items);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void renderCredits(int credits) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String creditsTip = getString(R.string.current_credits_colon);
        builder.append(creditsTip);
        String creditsStr = String.valueOf(credits);
        SpannableString creditsSpan = new SpannableString(creditsStr);
        creditsSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(22, this)), 0,
                creditsStr.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(creditsSpan);
        tvCredits.setText(builder);
    }

    @Override
    public void showExchangeDialog(Long bonusId, Integer deviceType, String bonusAmount, Integer pointAmount) {
        if (dialog == null) {
            dialog = new AvailabilityDialog(CreditsActivity.this);
        }
        dialog.setTitle(String.format("确认兑换¥%s%s代金券码？", bonusAmount,
                Device.getDevice(deviceType).getDesc()));
        dialog.setTip(String.format(Locale.getDefault(), "兑换成功后将会扣除%d积分", pointAmount));
        dialog.setOkText("确认");
        dialog.setOnOkClickListener(dialog -> onExchange(bonusId, pointAmount, deviceType));
        dialog.show();
    }
}
