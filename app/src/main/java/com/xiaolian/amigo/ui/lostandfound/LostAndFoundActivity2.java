package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView2;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundPopupDialog;
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

    private LostAndFoundAdaptor2 adaptor;

    private LostAndFoundPopupDialog addDialog;

    private SearchDialog searchDialog;
    private RecyclerView searchRecyclerView;
    private LostAndFoundAdaptor2 searchAdaptor;

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
            public void onPublishLostClick() {
                startActivityForResult(new Intent(LostAndFoundActivity2.this, PublishLostAndFoundActivity.class)
                        .putExtra(PublishLostAndFoundActivity.KEY_TYPE, LostAndFound.LOST), REQUEST_CODE_PUBLISH);
            }

            @Override
            public void onPublishFoundClick() {
                startActivityForResult(new Intent(LostAndFoundActivity2.this, PublishLostAndFoundActivity.class)
                        .putExtra(PublishLostAndFoundActivity.KEY_TYPE, LostAndFound.FOUND), REQUEST_CODE_PUBLISH);
            }

            @Override
            public void onMyPublishClick() {
                startActivityForResult(
                        new Intent(LostAndFoundActivity2.this, MyPublishActivity2.class), REQUEST_CODE_PUBLISH);
            }
        });
        addDialog.show();
    }

    @OnClick(R.id.iv_add)
    public void onAddClick() {
        showAddDialog();
    }

    private void initRecyclerView() {
        adaptor = new LostAndFoundAdaptor2(this, R.layout.item_lost_and_found2, lostAndFounds);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                try {
                    Intent intent = new Intent(LostAndFoundActivity2.this, LostAndFoundDetailActivity2.class);
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
                LostAndFoundActivity2.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                LostAndFoundActivity2.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
    }

    void search() {
        if (searchDialog == null) {
            searchDialog = new SearchDialog(this);
            searchDialog.setSearchListener(searchStr -> {
                // TODO search
            });
            searchDialog.setCanceledOnTouchOutside(true);
            searchDialog.setCancelable(true);
            searchDialog.setOnDismissListener(dialog -> searchResult.clear());
        }
        searchDialog.show();
    }

    public void showNoSearchResult(String selectKey) {
        searchDialog.showNoResult(selectKey);
    }

    public void showSearchResult(List<LostAndFoundAdaptor2.LostAndFoundWrapper> wappers) {
        if (searchRecyclerView == null) {
            searchRecyclerView = new RecyclerView(this);
            searchAdaptor = new LostAndFoundAdaptor2(this, R.layout.item_lost_and_found2, searchResult, true);
            searchRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
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
        presenter.onAttach(LostAndFoundActivity2.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PUBLISH) {
//                refreshLostAndFound();
                onRefresh();
            }
        }
    }
}
