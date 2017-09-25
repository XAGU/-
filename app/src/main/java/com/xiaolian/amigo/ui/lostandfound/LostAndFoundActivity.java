package com.xiaolian.amigo.ui.lostandfound;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView;
import com.xiaolian.amigo.ui.widget.dialog.SearchDialog;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 失物招领
 * <p>
 * Created by caidong on 2017/9/13.
 */
public class LostAndFoundActivity extends LostAndFoundBaseListActivity implements ILostAndFoundView {
    private static final int REQUEST_CODE_PUBLISH = 0x0101;
    // 失物招领列表
    List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFounds = new ArrayList<>();
    List<LostAndFoundAdaptor.LostAndFoundWapper> losts = new ArrayList<>();
    List<LostAndFoundAdaptor.LostAndFoundWapper> founds = new ArrayList<>();
    List<LostAndFoundAdaptor.LostAndFoundWapper> searchResult = new ArrayList<>();

    LostAndFoundAdaptor adaptor;
    LostAndFoundAdaptor searchAdaptor;

    @Inject
    ILostAndFoundPresenter<ILostAndFoundView> presenter;

    @BindView(R.id.tv_lost)
    TextView tv_lost;

    @BindView(R.id.tv_found)
    TextView tv_found;

    /**
     * 列表显示的是失物列表还是招领列表
     * false 表示失物列表
     * true 表示招领列表
     */
    private boolean listStatus = false;

    /**
     * 失物列表page
     */
    private int lostPage = 1;

    /**
     * 招领列表page
     */
    private int foundPage = 1;

    /**
     * 发布招领
     */
    @BindView(R.id.tv_publish_found)
    TextView tv_publish_found;
    private SearchDialog searchDialog;
    private RecyclerView searchRecyclerView;

    /**
     * 打开发布招领页面
     */
    @OnClick(R.id.tv_publish_found)
    void gotoPublishFound() {
        startActivityForResult(new Intent(this, PublishFoundActivity.class), REQUEST_CODE_PUBLISH);
    }

    /**
     * 发布失物
     */
    @BindView(R.id.tv_publish_lost)
    TextView tv_publish_lost;

    /**
     * 打开发布招领页面
     */
    @OnClick(R.id.tv_publish_lost)
    void gotoPublishLost() {
        startActivityForResult(new Intent(this, PublishLostActivity.class), REQUEST_CODE_PUBLISH);
    }
    /**
     * 我的发布
     */
    @BindView(R.id.tv_my_publish)
    TextView tv_my_publish;
    /**
     * 打开发布招领页面
     */
    @OnClick(R.id.tv_my_publish)
    void gotoMyPublish() {
        startActivity(new Intent(this, MyPublishActivity.class));
    }

    @Override
    protected void initPresenter() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(LostAndFoundActivity.this);
    }

    protected RecyclerView.Adapter getAdaptor() {
        adaptor = new LostAndFoundAdaptor(this, R.layout.item_lost_and_found, lostAndFounds);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(LostAndFoundActivity.this, LostAndFoundDetailActivity.class);
                intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_ID, lostAndFounds.get(position).getId());
                // listStatus false表示失物 true表示招领
                if (listStatus) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PUBLISH) {
                refreshLostAndFound();
                if (listStatus) {
                    presenter.queryFoundList(page, Constant.PAGE_SIZE);
                } else {
                    presenter.queryLostList(page, Constant.PAGE_SIZE);
                }
            }
        }
    }

    /**
     * 刷新失物招领列表
     */
    private void refreshLostAndFound() {
        lostAndFounds.clear();
        losts.clear();
        founds.clear();
        page = 1;
        lostPage = 1;
        foundPage = 1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_lost_and_found;
    }

    // 点击搜索
    @OnClick(R.id.tv_search)
    void search() {
        if (searchDialog == null) {
            searchDialog = new SearchDialog(this);
            searchDialog.setSearchListener(new SearchDialog.OnSearchListener() {
                @Override
                public void onSearch(String searchStr) {
                    if (listStatus) {
                        presenter.searchFoundList(null, null, searchStr);
                    } else {
                        presenter.searchLostList(null, null, searchStr);
                    }
                }
            });
            searchDialog.setCanceledOnTouchOutside(true);
            searchDialog.setCancelable(true);
            searchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchResult.clear();
                }
            });
        }
        searchDialog.show();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        lostAndFounds.clear();
        if (listStatus) {
            foundPage = 1;
            presenter.queryFoundList(foundPage, Constant.PAGE_SIZE);
            founds.clear();
        } else {
            lostPage = 1;
            presenter.queryLostList(lostPage, Constant.PAGE_SIZE);
            losts.clear();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLoadMore() {
        if (listStatus) {
            presenter.queryFoundList(foundPage, Constant.PAGE_SIZE);
        } else {
            presenter.queryLostList(lostPage, Constant.PAGE_SIZE);
        }
    }

    @Override
    public void addMoreLost(List<LostAndFoundAdaptor.LostAndFoundWapper> lost) {
        this.losts.addAll(lost);
        this.lostAndFounds.addAll(lost);
        adaptor.notifyDataSetChanged();
        lostPage ++;
    }

    @Override
    public void addMoreFound(List<LostAndFoundAdaptor.LostAndFoundWapper> found) {
        this.founds.addAll(found);
        this.lostAndFounds.addAll(found);
        adaptor.notifyDataSetChanged();
        foundPage ++;
    }

    @Override
    public void addMore(List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFoundWappers) {
        // this is for MyPublishActivity
    }

    @Override
    public void showNoSearchResult(String selectKey) {
        searchDialog.showNoResult(selectKey);
    }

    @Override
    public void showSearchResult(List<LostAndFoundAdaptor.LostAndFoundWapper> wappers) {
        if (searchRecyclerView == null) {
            searchRecyclerView = new RecyclerView(this);
            searchAdaptor = new LostAndFoundAdaptor(this, R.layout.item_lost_and_found, searchResult);
            searchAdaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(LostAndFoundActivity.this, LostAndFoundDetailActivity.class);
                    intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_ID, lostAndFounds.get(position).getId());
                    // listStatus false表示失物 true表示招领
                    if (listStatus) {
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

            searchRecyclerView.setAdapter(searchAdaptor);
            searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        searchResult.clear();
        searchResult.addAll(wappers);
        searchAdaptor.notifyDataSetChanged();
        searchDialog.showResult(searchRecyclerView);
    }


    @OnClick(R.id.tv_lost)
    void onLostClick() {
        if (listStatus) {
            switchListStatus();
            this.lostAndFounds.clear();
            if (lostPage == 1) {
                page = lostPage;
                presenter.queryLostList(page, Constant.PAGE_SIZE);
            } else {
                this.lostAndFounds.addAll(losts);
                adaptor.notifyDataSetChanged();
            }
        }
    }
    @OnClick(R.id.tv_found)
    void onFoundClick() {
        if (!listStatus) {
            switchListStatus();
            this.lostAndFounds.clear();
            if (foundPage == 1) {
                page = foundPage;
                presenter.queryFoundList(page, Constant.PAGE_SIZE);
            } else {
                this.lostAndFounds.addAll(founds);
                adaptor.notifyDataSetChanged();
            }
        }
    }
    private void switchListStatus() {
        if (listStatus) {
            listStatus = false;
            tv_lost.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
            tv_found.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        } else {
            listStatus = true;
            tv_lost.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
            tv_found.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
        }
    }


}
