package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.tmp.component.dialog.SearchDialog;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView;
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
    // 失物招领列表
    List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFounds = new ArrayList<>();
    List<LostAndFoundAdaptor.LostAndFoundWapper> losts = new ArrayList<>();
    List<LostAndFoundAdaptor.LostAndFoundWapper> founds = new ArrayList<>();

    LostAndFoundAdaptor adaptor;

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

    /**
     * 打开发布招领页面
     */
    @OnClick(R.id.tv_publish_found)
    void gotoPublishFound() {
        startActivity(new Intent(this, PublishFoundActivity.class));
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
        startActivity(new Intent(this, PublishLostActivity.class));
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
    protected void initData() {
        presenter.queryLostList(page, Constant.PAGE_SIZE, null, null);
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
    protected int getLayout() {
        return R.layout.activity_lost_and_found;
    }

    // 点击搜索
    @OnClick(R.id.tv_search)
    void search() {
        SearchDialog dialog = new SearchDialog(this);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
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


    @OnClick(R.id.tv_lost)
    void onLostClick() {
        if (listStatus) {
            switchListStatus();
            if (lostPage == 1) {
                page = lostPage;
                presenter.queryLostList(page, Constant.PAGE_SIZE, null, null);
            }
            this.lostAndFounds.clear();
            this.lostAndFounds.addAll(losts);
            adaptor.notifyDataSetChanged();
        }
    }
    @OnClick(R.id.tv_found)
    void onFoundClick() {
        if (!listStatus) {
            switchListStatus();
            if (foundPage == 1) {
                page = foundPage;
                presenter.queryFoundList(page, Constant.PAGE_SIZE, null, null);
            }
            this.lostAndFounds.clear();
            this.lostAndFounds.addAll(founds);
            adaptor.notifyDataSetChanged();
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
