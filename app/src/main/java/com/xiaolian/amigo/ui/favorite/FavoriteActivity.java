package com.xiaolian.amigo.ui.favorite;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.IntentAction;
import com.xiaolian.amigo.ui.favorite.adaptor.FavoriteAdaptor;
import com.xiaolian.amigo.ui.favorite.intf.IFavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoriteView;
import com.xiaolian.amigo.ui.repair.RepairApplyActivity;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 我收藏的设备
 * Created by caidong on 2017/9/18.
 */

public class FavoriteActivity extends FavoriteBaseActivity implements IFavoriteView {

    private static final String TAG = FavoriteActivity.class.getSimpleName();

    @Inject
    IFavoritePresenter<IFavoriteView> presenter;

    List<FavoriteAdaptor.FavoriteWrapper> favorites = new ArrayList<>();

    // 收藏设备recycleView适配器
    FavoriteAdaptor adaptor;
    private IntentAction action;

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        favorites.clear();
        presenter.requestFavorites(Constant.PAGE_START_NUM);
    }

    @Override
    protected void onLoadMore() {
        presenter.requestFavorites(page);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new FavoriteAdaptor(favorites, presenter);
        adaptor.setOnItemLongClickListener(() -> onSuccess("请左滑操作"));
        adaptor.setOnItemClickListener(position -> {
            if (action == IntentAction.ACTION_CHOOSE_FAVORITE_FOR_REPAIR) {
                Intent intent = new Intent(FavoriteActivity.this, RepairApplyActivity.class);
                intent.putExtra(Constant.LOCATION_ID, favorites.get(position).getId());
                intent.putExtra(Constant.DEVICE_TYPE, Device.DISPENSER.getType());
                intent.putExtra(Constant.LOCATION, Device.DISPENSER.getDesc()
                        + Constant.CHINEASE_COLON + favorites.get(position).getLocation());
                startActivity(intent);
            }
        });
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
    }

    @Override
    protected int setTitle() {
        return R.string.my_favorite;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setMainBackground(R.color.white);
        setHeaderBackground(R.color.white);
    }

    @Override
    public void addMore(List<FavoriteAdaptor.FavoriteWrapper> favorites) {
        this.favorites.addAll(favorites);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void deleteOne(int index) {
        Log.i(TAG, String.format(Locale.getDefault(), "删除收藏设备前列表数量为：%d", this.favorites.size()));
        FavoriteAdaptor.FavoriteWrapper result = this.favorites.remove(index);
        Log.i(TAG, String.format(Locale.getDefault(), "删除收藏设备后列表数量为：%d", this.favorites.size()));
        if (null != result) {
            Log.i(TAG, "删除收藏设备成功！deviceId=" + result.getId());
            adaptor.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmptyView() {
        super.showEmptyView(getString(R.string.empty_tip), R.color.white);
    }

    @Override
    public void showErrorView() {
        super.showErrorView(R.color.white);
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            int actionType = getIntent().getIntExtra(Constant.INTENT_ACTION, 0);
            action = IntentAction.getAction(actionType);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
