package com.xiaolian.amigo.ui.device.dispenser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.ui.device.DeviceBaseActivity;
import com.xiaolian.amigo.ui.device.DeviceBaseListActivity;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 附近饮水机页面
 *
 * @author zcd
 */
public class ChooseDispenserActivity extends DeviceBaseActivity implements IChooseDispenerView {

    private static final String TAG = ChooseDispenserActivity.class.getSimpleName();

    @Inject
    IChooseDispenserPresenter<IChooseDispenerView> presenter;
    ChooseDispenserAdaptor adaptor;
    /**
     * 列表显示的是附近列表还是收藏列表
     * false 表示附近列表
     * true 表示收藏列表
     */
    boolean listStatus = false;
    List<ChooseDispenserAdaptor.DispenserWrapper> items = new ArrayList<>();
    List<ChooseDispenserAdaptor.DispenserWrapper> nearbyItems = new ArrayList<>();
    List<ChooseDispenserAdaptor.DispenserWrapper> favoriteItems = new ArrayList<>();

    TextView tv_nearby;
    TextView tv_favorite;

    LinearLayout ll_footer;

    RecyclerView recyclerView;

    RelativeLayout rl_empty;
    RelativeLayout rl_error;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dispenser);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ChooseDispenserActivity.this);
        adaptor = new ChooseDispenserAdaptor(this, R.layout.item_dispenser, items);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adaptor);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
        rl_error = (RelativeLayout) findViewById(R.id.rl_error);

        ll_footer = (LinearLayout) findViewById(R.id.ll_footer);

        tv_nearby = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNearbyClick();
            }
        });
        tv_favorite = (TextView) findViewById(R.id.tv_toolbar_title2);
        tv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteClick();
            }
        });
        presenter.onLoad();
    }

    private void onNearbyClick() {
        if (listStatus) {
            switchListStatus();
            this.items.clear();
            if (nearbyItems.isEmpty()) {
                presenter.onLoad();
                adaptor.notifyDataSetChanged();
            } else {
                this.items.addAll(nearbyItems);
                adaptor.notifyDataSetChanged();
            }
        }
    }

    private void onFavoriteClick() {
        if (!listStatus) {
            switchListStatus();
            this.items.clear();
            if (favoriteItems.isEmpty()) {
                presenter.requestFavorites();
            } else {
                this.items.addAll(favoriteItems);
                adaptor.notifyDataSetChanged();
//                adaptor.notifyItemRangeChanged(0, items.size());
//                recyclerView.setAdapter(adaptor);
            }
        }
    }


    private void switchListStatus() {
        if (listStatus) {
            ll_footer.setVisibility(View.VISIBLE);
            listStatus = false;
            tv_nearby.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
            tv_favorite.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        } else {
            ll_footer.setVisibility(View.GONE);
            listStatus = true;
            tv_nearby.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
            tv_favorite.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
        }
    }

    @Override
    public void addMore(List<ChooseDispenserAdaptor.DispenserWrapper> wrappers) {
        items.addAll(wrappers);
        favoriteItems.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void addScanDevices(List<ScanDeviceGroup> devices) {
        updateDevice(devices);
        Log.i(TAG, "扫描到的设备列表：" + devices);
    }

    @Override
    public void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        rl_empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        recyclerView.setVisibility(View.VISIBLE);
        rl_empty.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        recyclerView.setVisibility(View.GONE);
        rl_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorView() {
        recyclerView.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
    }

    private void updateDevice(List<ScanDeviceGroup> devices) {
        if (nearbyItems.isEmpty()) {
            for (ScanDeviceGroup scanDeviceGroup : devices) {
                nearbyItems.add(new ChooseDispenserAdaptor.DispenserWrapper(scanDeviceGroup));
            }
            if (!listStatus) {
                items.addAll(nearbyItems);
                adaptor.notifyDataSetChanged();
            }
        } else {
            boolean needNotify = false;
            for (ScanDeviceGroup scanDeviceGroup : devices) {
                for (ChooseDispenserAdaptor.DispenserWrapper wrapper : nearbyItems) {
                    if (CommonUtil.equals(scanDeviceGroup.getResidenceId(),
                            wrapper.getResidenceId())) {
                        for (String key : scanDeviceGroup.getWater().keySet()) {
                            if (TextUtils.equals(key, DispenserWater.HOT.getType())) {
                                wrapper.setHot(scanDeviceGroup.getWater().get(key));
                                needNotify = true;
                            } else if (TextUtils.equals(key, DispenserWater.COLD.getType())) {
                                wrapper.setCold(scanDeviceGroup.getWater().get(key));
                                needNotify = true;
                            } else if (TextUtils.equals(key, DispenserWater.ICE.getType())) {
                                wrapper.setIce(scanDeviceGroup.getWater().get(key));
                                needNotify = true;
                            }
                        }
                    }
                }
            }
            if (needNotify && !listStatus) {
                items.clear();
                items.addAll(nearbyItems);
                adaptor.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(TradeError tradeError) {

    }
}
