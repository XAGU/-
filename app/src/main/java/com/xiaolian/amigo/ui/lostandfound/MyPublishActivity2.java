package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView2;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的发布
 * @author zcd
 * @date 18/5/12
 */
public class MyPublishActivity2 extends LostAndFoundBaseActivity implements ILostAndFoundView2 {
    private static final String TAG = MyPublishActivity2.class.getSimpleName();

    @Inject
    ILostAndFoundPresenter2<ILostAndFoundView2> presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private LostAndFoundAdaptor2 adaptor;

    List<LostAndFoundAdaptor2.LostAndFoundWrapper> lostAndFounds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_my_publish);
        setUnBinder(ButterKnife.bind(this));

        initRecyclerView();
    }

    private void initRecyclerView() {
        adaptor = new LostAndFoundAdaptor2(this, R.layout.item_lost_and_found2, lostAndFounds);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                try {
                    Intent intent = new Intent(MyPublishActivity2.this, LostAndFoundDetailActivity2.class);
                    intent.putExtra(LostAndFoundDetailActivity2.KEY_TYPE,
                            lostAndFounds.get(position).getType());
                    intent.putExtra(LostAndFoundDetailActivity2.KEY_ID, lostAndFounds.get(position).getId());
                    startActivity(intent);

                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.wtf(TAG, "数组越界", e);
                } catch (Exception e) {
                    Log.wtf(TAG, e);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                MyPublishActivity2.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                MyPublishActivity2.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
    }

    private void onLoadMore() {
        presenter.getList(false);
    }

    private void onRefresh() {
        presenter.resetPage();
        lostAndFounds.clear();
        presenter.getList(false);
    }

    @Override
    protected void setUp() {
    }

    @Override
    protected void initInject() {
        super.initInject();
        getActivityComponent().inject(this);
        presenter.onAttach(MyPublishActivity2.this);
    }

    @Override
    public void setRefreshComplete() {
        refreshLayout.finishRefresh(300);
    }

    @Override
    public void setLoadMoreComplete() {
        refreshLayout.finishLoadmore();
    }

    @Override
    public void showErrorView() {
        rlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        rlEmpty.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorView() {
        rlError.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
        rlEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void addMore(List<LostAndFoundAdaptor2.LostAndFoundWrapper> wrappers) {
        lostAndFounds.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }
}
