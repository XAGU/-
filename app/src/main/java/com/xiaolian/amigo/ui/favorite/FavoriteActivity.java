package com.xiaolian.amigo.ui.favorite;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.RecycleViewDivider;
import com.xiaolian.amigo.ui.favorite.adaptor.FavoriteAdaptor;
import com.xiaolian.amigo.ui.favorite.intf.IFavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoriteView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/18.
 */

public class FavoriteActivity extends FavoriteBaseActivity implements IFavoriteView {

    private static final String TAG = FavoriteActivity.class.getSimpleName();

    @Inject
    IFavoritePresenter<IFavoriteView> presenter;

    List<FavoriteAdaptor.FavoriteWrapper> favorites = new ArrayList<>();

    // 收藏设备recycleView适配器
    FavoriteAdaptor adaptor;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_favorite);
//    }

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
        presenter.requestFavorites(Constant.PAGE_START_NUM);
    }

    @Override
    public void addMore(List<FavoriteAdaptor.FavoriteWrapper> favorites) {
        this.favorites.addAll(favorites);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void deleteOne(int index) {
        Log.i(TAG, String.format("删除收藏设备前列表数量为：%d", this.favorites.size()));
        FavoriteAdaptor.FavoriteWrapper result = this.favorites.remove(index);
        Log.i(TAG, String.format("删除收藏设备后列表数量为：%d", this.favorites.size()));
        if (null != result) {
            Log.i(TAG, "删除收藏设备成功！deviceId=" + result.getId());
            adaptor.notifyDataSetChanged();
        }
    }

    @Override
    protected void setUp() {

    }
}
