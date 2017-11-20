package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.SearchDialog;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 失物招领
 * <p>
 * Created by caidong on 2017/9/13.
 */
public class LostAndFoundActivity extends LostAndFoundBaseListActivity implements ILostAndFoundView {
    private static final int REQUEST_CODE_PUBLISH = 0x0101;
    private static final String TAG = LostAndFoundActivity.class.getSimpleName();
    // 失物招领列表
    List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFounds = new ArrayList<>();
    List<LostAndFoundAdaptor.LostAndFoundWapper> losts = new ArrayList<>();
    List<LostAndFoundAdaptor.LostAndFoundWapper> founds = new ArrayList<>();
    List<LostAndFoundAdaptor.LostAndFoundWapper> searchResult = new ArrayList<>();

    LostAndFoundAdaptor adaptor;
    LostAndFoundAdaptor searchAdaptor;

    @Inject
    ILostAndFoundPresenter<ILostAndFoundView> presenter;

    TextView tv_lost;

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
    private int lostPage = Constant.PAGE_START_NUM;

    /**
     * 招领列表page
     */
    private int foundPage = Constant.PAGE_START_NUM;

    /**
     * 发布招领
     */
    TextView tv_publish_found;
    private SearchDialog searchDialog;
    private RecyclerView searchRecyclerView;

    /**
     * 打开发布招领页面
     */
    void gotoPublishFound() {
        startActivityForResult(new Intent(this, PublishFoundActivity.class), REQUEST_CODE_PUBLISH);
    }

    /**
     * 发布失物
     */
    TextView tv_publish_lost;

    /**
     * 打开发布招领页面
     */
    void gotoPublishLost() {
        startActivityForResult(new Intent(this, PublishLostActivity.class), REQUEST_CODE_PUBLISH);
    }

    /**
     * 我的发布
     */
    TextView tv_my_publish;

    /**
     * 打开发布招领页面
     */
    void gotoMyPublish() {
        startActivity(new Intent(this, MyPublishActivity.class));
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
        page = Constant.PAGE_START_NUM;
        lostPage = Constant.PAGE_START_NUM;
        foundPage = Constant.PAGE_START_NUM;
    }

    //  点击搜索
    void search() {
        if (searchDialog == null) {
            searchDialog = new SearchDialog(this);
            searchDialog.setSearchListener(searchStr -> {
                if (listStatus) {
                    presenter.searchFoundList(null, null, searchStr);
                } else {
                    presenter.searchLostList(null, null, searchStr);
                }
            });
            searchDialog.setCanceledOnTouchOutside(true);
            searchDialog.setCancelable(true);
            searchDialog.setOnDismissListener(dialog -> searchResult.clear());
        }
        searchDialog.show();
    }

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        lostAndFounds.clear();
        if (listStatus) {
            foundPage = Constant.PAGE_START_NUM;
            presenter.queryFoundList(foundPage, Constant.PAGE_SIZE);
            founds.clear();
        } else {
            lostPage = Constant.PAGE_START_NUM;
            presenter.queryLostList(lostPage, Constant.PAGE_SIZE);
            losts.clear();
        }

    }

    @Override
    public void onLoadMore() {
        if (listStatus) {
            page = foundPage;
            presenter.queryFoundList(foundPage, Constant.PAGE_SIZE);
        } else {
            page = lostPage;
            presenter.queryLostList(lostPage, Constant.PAGE_SIZE);
        }
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new LostAndFoundAdaptor(this, R.layout.item_lost_and_found, lostAndFounds);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                try {
                    Intent intent = new Intent(LostAndFoundActivity.this, LostAndFoundDetailActivity.class);
                    intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_ID,
                            lostAndFounds.get(position).getId());
                    // listStatus false表示失物 true表示招领
                    if (listStatus) {
                        intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE,
                                LostAndFoundDetailActivity.TYPE_FOUND);
                    } else {
                        intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE,
                                LostAndFoundDetailActivity.TYPE_LOST);
                    }
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
    }

    @Override
    protected int setFooterLayout() {
        return R.layout.footer_lost_and_found;
    }

    @Override
    protected void setFooter() {
        tv_my_publish = (TextView) getFooter().findViewById(R.id.tv_my_publish);
        tv_my_publish.setOnClickListener(v -> gotoMyPublish());
        tv_publish_found = (TextView) getFooter().findViewById(R.id.tv_publish_found);
        tv_publish_found.setOnClickListener(v -> gotoPublishFound());
        tv_publish_lost = (TextView) getFooter().findViewById(R.id.tv_publish_lost);
        tv_publish_lost.setOnClickListener(v -> gotoPublishLost());
    }

    @Override
    protected int setTitle() {
        return R.string.lost;
    }

    @Override
    protected int setSubTitle() {
        getSubTitle().setTextColor(ContextCompat.getColor(this, R.color.colorBlue));
        getSubTitle().setOnClickListener(v -> search());
        return R.string.search;
    }

    @Override
    protected int setTitle2() {
        return R.string.found;
    }

    @Override
    protected void initView() {
        setHeaderBackground(R.color.white);
        setMainBackground(R.color.colorBackgroundGray);
        setRefreshLayoutMargin(0, 0, 0, 0);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(LostAndFoundActivity.this);
        tv_lost = getToolBarTitle();
        tv_lost.setOnClickListener(v -> onLostClick());
        tv_found = getToolBarTitle2();
        tv_found.setOnClickListener(v -> onFoundClick());
//        presenter.queryLostList(page, Constant.PAGE_SIZE);
    }

    @Override
    public void addMoreLost(List<LostAndFoundAdaptor.LostAndFoundWapper> lost) {
        this.losts.addAll(lost);
        lostPage++;
        if (!listStatus) {
            this.lostAndFounds.addAll(lost);
            adaptor.notifyDataSetChanged();
            page = lostPage;
        }
    }

    @Override
    public void addMoreFound(List<LostAndFoundAdaptor.LostAndFoundWapper> found) {
        this.founds.addAll(found);
        foundPage++;
        if (listStatus) {
            this.lostAndFounds.addAll(found);
            adaptor.notifyDataSetChanged();
            page = foundPage;
        }
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
            searchAdaptor = new LostAndFoundAdaptor(this, R.layout.item_lost_and_found, searchResult, true);
            searchRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
            searchAdaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(LostAndFoundActivity.this, LostAndFoundDetailActivity.class);
                    intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_ID, searchResult.get(position).getId());
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

    @Override
    public void refresh() {
        onRefresh();
    }


    //    @OnClick(R.id.tv_lost)
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

    void onFoundClick() {
        if (!listStatus) {
            if (getRefreshLayout().isRefreshing()) {
                return;
            }
            switchListStatus();
            boolean needNotify = false;
            needNotify = !this.lostAndFounds.isEmpty();
            this.lostAndFounds.clear();
            if (foundPage == 1) {
                page = foundPage;
//                presenter.queryFoundList(page, Constant.PAGE_SIZE);
                getRefreshLayout().autoRefresh(0);
                if (needNotify) {
                    adaptor.notifyDataSetChanged();
                }
            } else {
                this.lostAndFounds.addAll(founds);
                adaptor.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showEmptyView(int tipRes) {
        this.lostAndFounds.clear();
        adaptor.notifyDataSetChanged();
        super.showEmptyView(tipRes);
    }

    @Override
    public void showErrorView() {
        this.lostAndFounds.clear();
        adaptor.notifyDataSetChanged();
        super.showErrorView();
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

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
