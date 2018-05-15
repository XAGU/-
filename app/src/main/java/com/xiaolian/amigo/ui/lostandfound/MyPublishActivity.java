package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.LostAndFound;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor;
import com.xiaolian.amigo.ui.lostandfound.adapter.MyPublishAdaptor;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 我的发布页面
 *
 * @author zcd
 * @date 17/9/18
 */
@Deprecated
public class MyPublishActivity extends LostAndFoundBaseListActivity implements ILostAndFoundView {
    /**
     * 失物招领列表
     */
    List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFounds = new ArrayList<>();

    MyPublishAdaptor adaptor;

    @Inject
    ILostAndFoundPresenter<ILostAndFoundView> presenter;

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        lostAndFounds.clear();
        presenter.getMyLostAndFounds();
    }

    @Override
    public void onLoadMore() {
        presenter.getMyLostAndFounds();
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {

        adaptor = new MyPublishAdaptor(this, R.layout.item_my_publish, lostAndFounds, true);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        adaptor.setClickListener(position -> {
            Intent intent = new Intent(MyPublishActivity.this, LostAndFoundDetailActivity.class);
            intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_ID, lostAndFounds.get(position).getId());
            if (lostAndFounds.get(position).getType() == LostAndFound.FOUND.getType()) {
                intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE,
                        LostAndFoundDetailActivity.TYPE_FOUND);
            } else {
                intent.putExtra(LostAndFoundDetailActivity.INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE,
                        LostAndFoundDetailActivity.TYPE_LOST);
            }
            startActivity(intent);
        });
        adaptor.setLongClickListener(() ->
                onSuccess("请左滑操作"));
        adaptor.setDeleteListener(position ->
                presenter.deleteLostAndFounds(lostAndFounds.get(position).getId()));
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int setTitle() {
        return R.string.my_publish;
    }

    @Override
    protected void initView() {
        setHeaderBackground(R.color.white);
        setMainBackground(R.color.colorBackgroundGray);
        setRefreshLayoutMargin(0, 0, 0, 0);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(MyPublishActivity.this);
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

    @Override
    public void refresh() {
        onRefresh();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
