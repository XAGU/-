package com.xiaolian.amigo.ui.lostandfound;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView2;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundPopupDialog;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundPublishDialog;
import com.xiaolian.amigo.ui.widget.dialog.SearchDialog;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zcd
 * @date 18/5/12
 */
public class LostAndFoundActivity2 extends LostAndFoundBaseActivity implements ILostAndFoundView2 {
    private static final String TAG = LostAndFoundActivity2.class.getSimpleName();
    private static final int REQUEST_CODE_PUBLISH = 0x0101;

    @Inject
    ILostAndFoundPresenter2<ILostAndFoundView2> presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.iv_add)
    ImageView ivAdd;

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.ll_footer)
    LinearLayout llFooter;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.abl_actionbar)
    AppBarLayout ablActionbar;

    private LostAndFoundAdaptor2 adaptor;

    private LostAndFoundPopupDialog addDialog;

    private LostAndFoundPublishDialog publishDialog;

    private SearchDialog searchDialog;
    private RecyclerView searchRecyclerView;
    private LostAndFoundAdaptor2 searchAdaptor;

    private volatile boolean refreshFlag = false;

    List<LostAndFoundAdaptor2.LostAndFoundWrapper> lostAndFounds = new ArrayList<>();

    List<LostAndFoundAdaptor2.LostAndFoundWrapper> searchResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found2);
        setUnBinder(ButterKnife.bind(this));

        initRecyclerView();
    }

    private void showAddDialog() {
        if (addDialog == null) {
            addDialog = new LostAndFoundPopupDialog(this);
        }
        addDialog.setLostAndFoundListener(new LostAndFoundPopupDialog.OnLostAndFoundClickListener() {
            @Override
            public void onMyNoticeClick() {
//                startActivityForResult(new Intent(LostAndFoundActivity2.this, PublishLostAndFoundActivity.class)
//                        .putExtra(PublishLostAndFoundActivity.KEY_TYPE, LostAndFound.LOST), REQUEST_CODE_PUBLISH);
                startActivity(new Intent(LostAndFoundActivity2.this, LostAndFoundNoticeActivity.class));
            }

            @Override
            public void onMyFavoriteClick() {
//                startActivityForResult(new Intent(LostAndFoundActivity2.this, PublishLostAndFoundActivity.class)
//                        .putExtra(PublishLostAndFoundActivity.KEY_TYPE, LostAndFound.FOUND), REQUEST_CODE_PUBLISH);
                startActivity(new Intent(LostAndFoundActivity2.this, MyCollectActivity.class));
            }

            @Override
            public void onMyPublishClick() {
                startActivityForResult(
                        new Intent(LostAndFoundActivity2.this, MyPublishActivity2.class), REQUEST_CODE_PUBLISH);
            }
        });
        addDialog.setNoticeCount(presenter.getNoticeCount());
        addDialog.show();
    }

    private void showPublishDialog() {
        if (publishDialog == null) {
            publishDialog = new LostAndFoundPublishDialog(this);
        }
        publishDialog.setOnPublishLostAndFoundListener(new LostAndFoundPublishDialog.PublishLostAndFoundListener() {
            @Override
            public void onPublishLost(Dialog dialog) {
                startActivityForResult(new Intent(LostAndFoundActivity2.this, PublishLostAndFoundActivity.class)
                        .putExtra(PublishLostAndFoundActivity.KEY_TYPE, LostAndFound.LOST), REQUEST_CODE_PUBLISH);
            }

            @Override
            public void onPublishFound(Dialog dialog) {
                startActivityForResult(new Intent(LostAndFoundActivity2.this, PublishLostAndFoundActivity.class)
                        .putExtra(PublishLostAndFoundActivity.KEY_TYPE, LostAndFound.FOUND), REQUEST_CODE_PUBLISH);
            }
        });
        publishDialog.show();
    }

    @OnClick(R.id.ll_footer)
    public void onPublishClick() {
        showPublishDialog();
    }

    @OnClick(R.id.iv_add)
    public void onAddClick() {
        showAddDialog();
    }

    private void initRecyclerView() {
        adaptor = new LostAndFoundAdaptor2(this, R.layout.item_lost_and_found2, lostAndFounds);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                try {
                    Intent intent = new Intent(LostAndFoundActivity2.this, LostAndFoundDetailActivity2.class);
                    intent.putExtra(LostAndFoundDetailActivity2.KEY_TYPE,
                            lostAndFounds.get(position).getType());
                    intent.putExtra(LostAndFoundDetailActivity2.KEY_ID, lostAndFounds.get(position).getId());
                    startActivityForResult(intent, REQUEST_CODE_PUBLISH);
                    lostAndFounds.get(position).addViewCount();
//                    adaptor.notifyDataSetChanged();
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
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                LostAndFoundActivity2.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                LostAndFoundActivity2.this.onRefresh();
            }
        });
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
    }

    @OnClick(R.id.ll_search)
    void onSearchClick() {
        search();
    }

    void search() {
        if (searchDialog == null) {
            searchDialog = new SearchDialog(this);
            searchDialog.setSearchListener(searchStr -> {
                presenter.getList(true, searchStr);
            });
            searchDialog.setCanceledOnTouchOutside(true);
            searchDialog.setCancelable(true);
            searchDialog.setOnDismissListener(dialog -> {
                searchResult.clear();
                ablActionbar.setExpanded(true);
            });
        }
        ablActionbar.setExpanded(false);
        searchDialog.show();
    }

    @Override
    public void showNoSearchResult(String selectKey) {
        searchDialog.showNoResult(selectKey);
    }

    @Override
    public void showSearchResult(List<LostAndFoundAdaptor2.LostAndFoundWrapper> wappers) {
        if (searchRecyclerView == null) {
            searchRecyclerView = new RecyclerView(this);
            searchAdaptor = new LostAndFoundAdaptor2(this, R.layout.item_lost_and_found2, searchResult, false);
//            searchRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
            searchAdaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                    Intent intent = new Intent(LostAndFoundActivity.this, LostAndFoundDetailActivity.class);
//                    intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_ID, searchResult.get(position).getId());
//                    // listStatus false表示失物 true表示招领
//                    if (listStatus) {
//                        intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE,
//                                LostAndFoundDetailActivity.TYPE_FOUND);
//                    } else {
//                        intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE,
//                                LostAndFoundDetailActivity.TYPE_LOST);
//                    }
//                    startActivity(intent);
                    try {
                        Intent intent = new Intent(LostAndFoundActivity2.this, LostAndFoundDetailActivity2.class);
                        intent.putExtra(LostAndFoundDetailActivity2.KEY_TYPE,
                                wappers.get(position).getType());
                        intent.putExtra(LostAndFoundDetailActivity2.KEY_ID, wappers.get(position).getId());
                        startActivityForResult(intent, REQUEST_CODE_PUBLISH);

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

            searchRecyclerView.setAdapter(searchAdaptor);
            searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        searchResult.clear();
        searchResult.addAll(wappers);
        searchAdaptor.notifyDataSetChanged();
        searchDialog.showResult(searchRecyclerView);
    }

    @Override
    public void showFootView() {
        llFooter.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFootView() {
        llFooter.setVisibility(View.GONE);
    }

    private void onLoadMore() {
        presenter.getList(false, null);
    }

    private void onRefresh() {
        presenter.resetPage();
        refreshFlag = true;
        presenter.getList(false, null);
        presenter.fetchNoticeCount();
    }

    @Override
    protected void setUp() {
    }

    @Override
    protected void initInject() {
        super.initInject();
        getActivityComponent().inject(this);
        presenter.onAttach(LostAndFoundActivity2.this);
    }

    @Override
    public void setRefreshComplete() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh(300);
        }
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
    public void addMore(List<LostAndFoundAdaptor2.LostAndFoundWrapper> wrappers) {
        if (refreshFlag) {
            refreshFlag = false;
            lostAndFounds.clear();
        }
        lostAndFounds.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PUBLISH) {
//                refreshLostAndFound();
                onRefresh();
            }
        } else {
            adaptor.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
