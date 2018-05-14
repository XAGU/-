package com.xiaolian.amigo.ui.lostandfound;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailFollowDelegate;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailMainDelegate;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 失物招领详情
 *
 * @author zcd
 * @date 17/9/21
 */

public class LostAndFoundReplyDetailActivity extends LostAndFoundBaseActivity implements ILostAndFoundDetailView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.ll_header)
    LinearLayout llHeader;

    @BindView(R.id.view_line)
    View viewLine;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_three_dot)
    ImageView ivThreeDot;

    private LostAndFoundReplyDetailAdapter adapter;
    private List<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> followRelays
            = new ArrayList<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper>(){
        {
            add(new LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW,
                    false, "回复11111111111111111111111", "jfdkfjkd", 437875347384L, ""));
            add(new LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW,
                    false, "回复11111111111111111111111", "jfdkfjkd", 437875347384L, ""));
            add(new LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW,
                    false, "回复11111111111111111111111", "jfdkfjkd", 437875347384L, ""));
            add(new LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW,
                    false, "回复11111111111111111111111", "jfdkfjkd", 437875347384L, ""));
            add(new LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW,
                    false, "回复11111111111111111111111", "jfdkfjkd", 437875347384L, ""));
            add(new LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW,
                    false, "回复11111111111111111111111", "jfdkfjkd", 437875347384L, ""));
        }
    };
    private LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper mainReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_reply_detail);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        mainReply = new LostAndFoundReplyDetailAdapter
                .LostAndFoundReplyDetailWrapper(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.MAIN,
                true,
                "这里是main内容,很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长",
                "这里是main作者", 1343434834L, "");
        initRecyclerView();

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                tvTitle.setVisibility(View.VISIBLE);
                ivThreeDot.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                ivThreeDot.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });
    }

    private void initRecyclerView() {
        followRelays.add(0, mainReply);
        adapter = new LostAndFoundReplyDetailAdapter(this, followRelays);
        adapter.addItemViewDelegate(new LostAndFoundReplyDetailMainDelegate());
        adapter.addItemViewDelegate(new LostAndFoundReplyDetailFollowDelegate());
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 21)));
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                LostAndFoundReplyDetailActivity.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                LostAndFoundReplyDetailActivity.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
//        refreshLayout.autoRefresh(20);
    }

    private void onRefresh() {

    }

    private void onLoadMore() {

    }

    @Override
    protected void setUp() {

    }

    @Override
    public void render(LostAndFound lostAndFound) {
    }

    @Override
    protected void onDestroy() {
//        presenter.onDetach();
        super.onDestroy();
    }
}
