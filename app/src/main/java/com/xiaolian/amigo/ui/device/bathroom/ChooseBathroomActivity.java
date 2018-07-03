package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;
import com.xiaolian.amigo.ui.widget.ZoomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 选择浴室
 *
 * @author zcd
 * @date 18/6/27
 */
public class ChooseBathroomActivity extends BathroomBaseActivity implements IChooseBathroomView {
    private List<ChooseBathroomAdapter.ItemWrapper> items = new ArrayList<ChooseBathroomAdapter.ItemWrapper>() {
        {
            add(new ChooseBathroomAdapter.ItemWrapper("1"));
            add(new ChooseBathroomAdapter.ItemWrapper("2"));
            add(new ChooseBathroomAdapter.ItemWrapper("3"));
            add(new ChooseBathroomAdapter.ItemWrapper("4"));
        }
    };
    private List<ChooseBathroomOuterAdapter.Item> items2 = new ArrayList<>();
    @Inject
    IChooseBathroomPresenter<IChooseBathroomView> presenter;

    private ZoomRecyclerView recyclerView;
    private ChooseBathroomAdapter adapter;
    private ChooseBathroomOuterAdapter adapter2;
    private LinearLayout llLeft;
    private LinearLayout llRight;
    private TextView tvMissedBookingTime;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivHelp;
    /**
     * 当前是否处于选中状态 选中状态显示预约使用 非选中状态显示购买编码
     */
    private boolean isSelected = false;
    /**
     * 是否显示付费使用
     */
    private boolean isShowBuyUse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bothroom);
        bindView();
        initRecyclerView();
    }

    private void bindView() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        recyclerView = findViewById(R.id.recyclerView);
        llLeft = findViewById(R.id.ll_left);
        llRight = findViewById(R.id.ll_right);
        llLeft.setOnClickListener(v -> onLeftClick());
        llRight.setOnClickListener(v -> onRightClick());
        tvMissedBookingTime = findViewById(R.id.tv_missed_booking_time);
        ivLeft = findViewById(R.id.iv_left);
        ivRight = findViewById(R.id.iv_right);
        tvLeft = findViewById(R.id.tv_left);
        tvRight = findViewById(R.id.tv_right);
        ivHelp = findViewById(R.id.iv_help);
        ivHelp.setOnClickListener(v -> {
            isSelected = !isSelected;
            changeToBookingUse();
        });
    }

    private void initRecyclerView() {
//        adapter = new ChooseBathroomAdapter(this, R.layout.item_choose_bathroom_outer, items);
        ChooseBathroomOuterAdapter.Item item1 = new ChooseBathroomOuterAdapter.Item(items);
        items2.add(item1);
        adapter2 = new ChooseBathroomOuterAdapter(this, R.layout.item_choose_bathroom_outer, items2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter2);
    }

    private void changeToBookingUse() {
        if (!isSelected) {
            llRight.setVisibility(View.VISIBLE);
            tvMissedBookingTime.setVisibility(View.INVISIBLE);
            tvLeft.setText("购买编码");
            ivLeft.setImageResource(R.drawable.ic_bathroom_buy_code);
            tvRight.setText("扫一扫");
            ivRight.setImageResource(R.drawable.ic_bathroom_scan);
        } else {
            tvMissedBookingTime.setVisibility(View.VISIBLE);
            tvLeft.setText("预约使用");
            ivLeft.setImageResource(R.drawable.ic_bathroom_booking);
            if (isShowBuyUse) {
                llRight.setVisibility(View.VISIBLE);
                tvRight.setText("付费使用");
                ivRight.setImageResource(R.drawable.ic_bathroom_pay_use);
            } else {
                llRight.setVisibility(View.GONE);
            }
        }
    }

    private void onLeftClick() {
        if (isSelected) {
            startActivity(new Intent(this, BookingActivity.class));
        } else {
            startActivity(new Intent(this, BuyCodeActivity.class));
        }
    }

    private void onRightClick() {
        if (isSelected) {

        } else {
            startActivity(new Intent(this, PayUseActivity.class));
        }
    }
}
