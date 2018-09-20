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
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor2;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailContentDelegate;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocalContentAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocalContentAdapter2;
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

import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailActivity2.KEY_ID;

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

//    private LostAndFoundAdaptor2 adaptor;

//    List<LostAndFoundAdaptor2.LostAndFoundWrapper> lostAndFounds = new ArrayList<>();

    List<LostAndFoundDTO> lostAndFounds = new ArrayList<>();

    private volatile boolean refreshFlag;

    private SocalContentAdapter2 publicAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_my_publish);
        setUnBinder(ButterKnife.bind(this));
        initRecyclerView();
    }

    private void initRecyclerView() {
        lostAndFounds = new ArrayList<>();
        publicAdapter = new SocalContentAdapter2(this, R.layout.item_socal2, lostAndFounds, new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(MyPublishActivity2.this, LostAndFoundDetailActivity2.class);
                    intent.putExtra(KEY_ID ,lostAndFounds.get(position).getId());
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    onError("请左滑删除");
                    return false;
                }
            }, new LostAndFoundDetailContentDelegate.OnLikeClickListener() {
                @Override
                public void onLikeClick(int position, long id, boolean like) {
                    if (like) {
                        presenter.unLikeComment(position, id);
                    } else {
                        presenter.likeComment(position, id);
                    }
                }
            });
        publicAdapter.setPresenter(presenter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 21)));
        recyclerView.setAdapter(publicAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                MyPublishActivity2.this.onLoadMore();
            }
            public void onRefresh(RefreshLayout refreshlayout) {
                MyPublishActivity2.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
        refreshLayout.setEnableLoadMore(false);
    }

    private void onLoadMore() {
        presenter.getMyList();
    }

    private void onRefresh() {
        presenter.resetPage();
        refreshFlag = true;
        presenter.getMyList();
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
        refreshLayout.finishLoadMore(300);
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
    public void addMore(List<LostAndFoundDTO> wrappers) {
        if (refreshFlag) {
            refreshFlag = false;
            lostAndFounds.clear();
        }
        lostAndFounds.addAll(wrappers);
        publicAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoSearchResult(String searchStr) {

    }


    @Override
    public void showSearchResult(List<LostAndFoundDTO> wrappers) {

    }

    @Override
    public void showFootView() {

    }

    @Override
    public void hideFootView() {

    }

    @Override
    public void showNoticeRemind() {

    }

    @Override
    public void hideNoticeRemind() {

    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void notifyAdapter(int position, boolean b) {
        if (publicAdapter != null) {
            publicAdapter.notifyItemChanged(position);
        }

    }

    @Override
    public void delete(int position) {
        try {
            lostAndFounds.remove(position);
            publicAdapter.notifyItemRemoved(position);
            publicAdapter.notifyItemRangeChanged(position ,lostAndFounds.size());
        }catch (Exception e){
            Log.e(TAG ,e.getMessage());
        }

    }

}
