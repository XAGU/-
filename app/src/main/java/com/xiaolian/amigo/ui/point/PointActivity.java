package com.xiaolian.amigo.ui.point;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.point.intf.IPointView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 我的积分
 *
 * @author zcd
 * @date 18/2/23
 */
public class PointActivity extends PointBaseActivity implements IPointView {

    private List<PointAdapter.PointItem> items = new ArrayList<PointAdapter.PointItem>() {
        {
            add(new PointAdapter.PointItem(1, "1", 200));
            add(new PointAdapter.PointItem(2, "2", 200));
            add(new PointAdapter.PointItem(3, "1", 204));
            add(new PointAdapter.PointItem(4, "3", 200));
            add(new PointAdapter.PointItem(4, "1", 200));
            add(new PointAdapter.PointItem(3, "1", 2100));
            add(new PointAdapter.PointItem(2, "4", 2000));
            add(new PointAdapter.PointItem(1, "1", 200));
            add(new PointAdapter.PointItem(1, "1", 205));
        }
    };
    private PointAdapter adapter;
    private RecyclerView recyclerView;
    private AvailabilityDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        bindView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new PointAdapter(this, R.layout.item_point, items);
        adapter.setExchangeClickListener((deviceType, bonusAmount, pointAmount) -> {
            if (dialog == null) {
                dialog = new AvailabilityDialog(PointActivity.this);
            }
            dialog.setTitle(String.format("确认兑换¥%s%s代金券码？", bonusAmount,
                    Device.getDevice(deviceType).getDesc()));
            dialog.setTip(String.format(Locale.getDefault(), "兑换成功后将会扣除%d积分", pointAmount));
            dialog.setOkText("确认");
            dialog.setOnOkClickListener(dialog -> onExchange());
            dialog.show();
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new GridSpacesItemDecoration(2,
                ScreenUtils.dpToPxInt(this, 10), false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 确认积分兑换
     */
    private void onExchange() {
        onSuccess("兑换成功");
    }

    private void bindView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        getActivityComponent().inject(this);
    }
}
