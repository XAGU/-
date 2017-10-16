package com.xiaolian.amigo.ui.device.dispenser;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.ui.device.DeviceBaseListActivity;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
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
public class ChooseDispenserActivity extends DeviceBaseListActivity implements IChooseDispenerView {

    private static final String TAG = ChooseDispenserActivity.class.getSimpleName();

    @Inject
    IChooseDispenserPresenter<IChooseDispenerView> presenter;
    ChooseDispenserAdaptor adaptor;
    List<ChooseDispenserAdaptor.DispenserWapper> items = new ArrayList<>();

    @Override
    protected void onRefresh() {
        // ignore
    }

    @Override
    protected void onLoadMore() {
        // ignore
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        setAutoRefresh(false);
        adaptor = new ChooseDispenserAdaptor(this, R.layout.item_dispenser, items);
        adaptor.setOnItemClickListener(new ChooseDispenserAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getApplicationContext(), DispenserActivity.class)
                        .putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS, items.get(position).getDevice().getMacAddress())
                        .putExtra(MainActivity.INTENT_KEY_LOCATION, items.get(position).getDevice().getLocation())
                        .putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, 2));
            }
        });
        recyclerView.setAdapter(adaptor);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int setTitle() {
        return R.string.nearby_dispenser;
    }

    @Override
    protected void initView() {
        getRefreshLayout().setEnableRefresh(false);
        getRefreshLayout().setEnableLoadmore(false);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ChooseDispenserActivity.this);

        // presenter.requestFavorites();
        presenter.onLoad();
    }

    @Override
    public void addMore(List<ChooseDispenserAdaptor.DispenserWapper> wrappers) {
        items.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void addScanDevices(List<ScanDeviceGroup> devices) {
        Log.i(TAG, "扫描到的设备列表：" + devices);
    }
}
