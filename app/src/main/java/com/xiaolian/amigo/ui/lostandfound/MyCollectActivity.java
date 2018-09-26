package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
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
import com.xiaolian.amigo.ui.lostandfound.adapter.SocialImgAdapter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView2;
import com.xiaolian.amigo.ui.lostandfound.intf.IMyCollectPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IMyCollectView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailActivity2.KEY_ID;
import static com.xiaolian.amigo.ui.lostandfound.SocalFragment.REQUEST_CODE_PHOTO;

/**
 * 我的发布
 * @author zcd
 * @date 18/5/12
 */
public class MyCollectActivity extends LostAndFoundBaseActivity implements IMyCollectView , SocialImgAdapter.PhotoClickListener {
    private static final String TAG = MyCollectActivity.class.getSimpleName();

    @Inject
    IMyCollectPresenter<IMyCollectView> presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private SocalContentAdapter adaptor;

    List<LostAndFoundDTO> lostAndFounds = new ArrayList<>();

    private volatile boolean refreshFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_my_collect);
        setUnBinder(ButterKnife.bind(this));

        initRecyclerView();
    }

    private void initRecyclerView() {

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        lostAndFounds = new ArrayList<>();
        adaptor = new SocalContentAdapter(this, R.layout.item_socal, lostAndFounds, new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(MyCollectActivity.this, LostAndFoundDetailActivity2.class);
                intent.putExtra(KEY_ID ,lostAndFounds.get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
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
        adaptor.setPhotoClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 21)));
        recyclerView.setAdapter(adaptor);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                MyCollectActivity.this.onLoadMore();
            }
            public void onRefresh(RefreshLayout refreshlayout) {
                MyCollectActivity.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
        refreshLayout.setEnableLoadMore(false);
    }

    private void onLoadMore() {
        presenter.getMyCollects();
    }

    private void onRefresh() {
        presenter.resetPage();
        refreshFlag = true;
        presenter.getMyCollects();
    }

    @Override
    protected void setUp() {
    }

    @Override
    protected void initInject() {
        super.initInject();
        getActivityComponent().inject(this);
        presenter.onAttach(MyCollectActivity.this);
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
        adaptor.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void notifyAdapter(int position, boolean b) {
        adaptor.notifyItemChanged(position);
    }

    @Override
    public void photoClick(int position, ArrayList<String> datas) {
        Intent intent = new Intent(MyCollectActivity.this, AlbumItemActivity.class);
        intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, position);
        intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, (ArrayList<String>) datas);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }
}
