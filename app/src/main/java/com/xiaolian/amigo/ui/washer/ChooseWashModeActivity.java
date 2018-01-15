package com.xiaolian.amigo.ui.washer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.washer.intf.IChooseWashModePresenter;
import com.xiaolian.amigo.ui.washer.intf.IChooseWashModeView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.DimentionUtils;
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

    private List<ChooseWashModeAdapter.WashModeItem> items =
            new ArrayList<ChooseWashModeAdapter.WashModeItem>() {
                {
                    add(new ChooseWashModeAdapter.WashModeItem("单脱水", "1"));
                    add(new ChooseWashModeAdapter.WashModeItem("一洗一漂一脱水", "2"));
                    add(new ChooseWashModeAdapter.WashModeItem("两洗一漂一脱水", "3"));
                    add(new ChooseWashModeAdapter.WashModeItem("两洗两漂一脱水", "4"));
                    add(new ChooseWashModeAdapter.WashModeItem("单脱水", "1"));
                    add(new ChooseWashModeAdapter.WashModeItem("两洗两漂一脱水", "4"));
                    add(new ChooseWashModeAdapter.WashModeItem("单脱水", "1"));
                    add(new ChooseWashModeAdapter.WashModeItem("两洗两漂一脱水", "4"));
                    add(new ChooseWashModeAdapter.WashModeItem("单脱水", "1"));
                }
            };
    private RecyclerView recyclerView;
    private Button bt_submit;
    private ChooseWashModeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_wash_mode);
        bindView();
        initAdapter();
        initRecyclerView();
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
                toggleSubmitButton();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void toggleSubmitButton() {
        if (adapter.getLastChoosePosition() == -1) {
            return;
        }
        if (bt_submit.getVisibility() == View.GONE) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
            lp.bottomMargin = 0;
            recyclerView.setLayoutParams(lp);
            bt_submit.setVisibility(View.VISIBLE);
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("支付");
        String priceText = String.format("%s元", items.get(adapter.getLastChoosePosition()).getPrice());
        SpannableString buttonSpan = new SpannableString(priceText);
        buttonSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(18, this)), 0, priceText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(buttonSpan);
        builder.append("，开始使用");
        bt_submit.setText(builder);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(42));
    }

    private void bindView() {
        recyclerView = findViewById(R.id.recyclerView);
        bt_submit = findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(v -> submit());
    }

    private void submit() {
        if (adapter.getLastChoosePosition() != -1) {
            String price = items.get(adapter.getLastChoosePosition()).getPrice();
            String mode = items.get(adapter.getLastChoosePosition()).getName();
            startActivity(new Intent(this, WasherQRCodeActivity.class)
                    .putExtra(WasherContent.INTENT_KEY_MODE, mode)
                    .putExtra(WasherContent.INTENT_KEY_PRICE, price));
        }
    }

    @Override
    protected void setUp() {

    }
}
