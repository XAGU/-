package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 我的发布页面
 * @author zcd
 */

public class MyPublishActivity extends LostAndFoundBaseListActivity implements ILostAndFoundView {
    // 失物招领列表
    List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFounds = new ArrayList<>();

    LostAndFoundAdaptor adaptor;

    @Inject
    ILostAndFoundPresenter<ILostAndFoundView> presenter;

    @Override
    protected void initData() {
        presenter.getMyLostAndFounds();
    }

    @Override
    protected void initPresenter() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(MyPublishActivity.this);
    }

    @Override
    protected RecyclerView.Adapter getAdaptor() {
        adaptor = new LostAndFoundAdaptor(this, R.layout.item_lost_and_found, lostAndFounds);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(MyPublishActivity.this, LostAndFoundDetailActivity.class);
                intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_ID, lostAndFounds.get(position).getId());
                if (lostAndFounds.get(position).getType() == 2) {
                    intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE,
                            LostAndFoundDetailActivity.TYPE_FOUND);
                } else {
                    intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE,
                            LostAndFoundDetailActivity.TYPE_LOST);
                }
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adaptor;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_my_publish;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void addMoreLost(List<LostAndFoundAdaptor.LostAndFoundWapper> lost) {
        // this is for LostAndFoundActivity
    }

    @Override
    public void addMoreFound(List<LostAndFoundAdaptor.LostAndFoundWapper> found) {
        // this is for LostAndFoundActivity
    }

    @Override
    public void addMore(List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFoundWappers) {
        this.lostAndFounds.addAll(lostAndFoundWappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void showNoSearchResult(String selectKey) {
        // this is for LostAndFoundActivity
    }

    @Override
    public void showSearchResult(List<LostAndFoundAdaptor.LostAndFoundWapper> wappers) {
        // this is for LostAndFoundActivity
    }
}
