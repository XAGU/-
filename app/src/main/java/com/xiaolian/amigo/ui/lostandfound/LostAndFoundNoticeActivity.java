package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundNoticeAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundNoticeLikeDelegate;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundNoticeReplyDelegate;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundNoticePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundNoticeView;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundReplyDialog;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/6/13
 */
public class LostAndFoundNoticeActivity extends LostAndFoundBaseActivity
        implements ILostAndFoundNoticeView {

    @Inject
    ILostAndFoundNoticePresenter<ILostAndFoundNoticeView> presenter;

    private SmartRefreshLayout refreshLayout;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private TextView tvToolbarTitle;
    private TextView tvToolbarTitle2;
    private TextView tvTitle;
    private TextView tvTitle2;
    private LinearLayout llHeader;
    private View viewLine;
    private View vDivide;
    private View vDivide2;
    RelativeLayout rlEmpty;
    RelativeLayout rlError;
    private boolean refreshFlag = false;
    private LostAndFoundNoticeAdapter adapter;
    private List<LostAndFoundNoticeAdapter.NoticeWrapper> replies = new ArrayList<LostAndFoundNoticeAdapter.NoticeWrapper>(){
        {
//            add(new LostAndFoundNoticeAdapter.NoticeWrapper(LostAndFoundNoticeAdapter.ItemType.REPLY, "reply1",
//                    "userName1"));
//            add(new LostAndFoundNoticeAdapter.NoticeWrapper(LostAndFoundNoticeAdapter.ItemType.REPLY, "reply2",
//                    "userName2"));
//            add(new LostAndFoundNoticeAdapter.NoticeWrapper(LostAndFoundNoticeAdapter.ItemType.REPLY, "reply3",
//                    "userName3"));
//            add(new LostAndFoundNoticeAdapter.NoticeWrapper(LostAndFoundNoticeAdapter.ItemType.REPLY, "reply4",
//                    "userName4"));
        }
    };
    private List<LostAndFoundNoticeAdapter.NoticeWrapper> likes = new ArrayList<LostAndFoundNoticeAdapter.NoticeWrapper>(){
        {
//            add(new LostAndFoundNoticeAdapter.NoticeWrapper(LostAndFoundNoticeAdapter.ItemType.LIKE,
//                    "like1xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
//                    "userName1"));
//            add(new LostAndFoundNoticeAdapter.NoticeWrapper(LostAndFoundNoticeAdapter.ItemType.LIKE, "like2",
//                    "userName2"));
//            add(new LostAndFoundNoticeAdapter.NoticeWrapper(LostAndFoundNoticeAdapter.ItemType.LIKE, "like3",
//                    "userName3"));
//            add(new LostAndFoundNoticeAdapter.NoticeWrapper(LostAndFoundNoticeAdapter.ItemType.LIKE, "like4",
//                    "userName4"));
        }
    };
    private List<LostAndFoundNoticeAdapter.NoticeWrapper> items = new ArrayList<>();
    private LostAndFoundReplyDialog replyDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_notice);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        bindView();
        initView();
    }

    private void initView() {
        tvToolbarTitle.setText("回复");
        tvTitle.setText("回复");
        tvToolbarTitle2.setText("点赞");
        tvTitle2.setText("点赞");
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset < -(tvToolbarTitle.getHeight())) {
                tvTitle.setVisibility(View.VISIBLE);
                vDivide2.setVisibility(View.VISIBLE);
                vDivide.setVisibility(View.GONE);
                tvTitle2.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                vDivide2.setVisibility(View.GONE);
                vDivide.setVisibility(View.VISIBLE);
                tvTitle2.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });
        initRefreshLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        items.addAll(replies);
        adapter = new LostAndFoundNoticeAdapter(this, items);
        adapter.addItemViewDelegate(new LostAndFoundNoticeLikeDelegate(this));
        adapter.addItemViewDelegate(new LostAndFoundNoticeReplyDelegate(this,
                this::publishReply));
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(LostAndFoundNoticeActivity.this, LostAndFoundDetailActivity2.class);
                intent.putExtra(LostAndFoundDetailActivity2.KEY_TYPE,
                        items.get(position).getLostFoundType());
                intent.putExtra(LostAndFoundDetailActivity2.KEY_ID, items.get(position).getLostFoundId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                LostAndFoundNoticeActivity.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                LostAndFoundNoticeActivity.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
    }

    private void bindView() {
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        appBarLayout = findViewById(R.id.app_bar_layout);
        llHeader = findViewById(R.id.ll_header);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle2 = findViewById(R.id.tv_toolbar_title2);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle2 = findViewById(R.id.tv_title2);
        viewLine = findViewById(R.id.view_line);
        vDivide = findViewById(R.id.v_divide);
        vDivide2 = findViewById(R.id.v_divide2);
        rlEmpty = findViewById(R.id.rl_empty);
        rlError = findViewById(R.id.rl_error);
        tvToolbarTitle.setOnClickListener(v -> changeItemToReply());
        tvToolbarTitle2.setOnClickListener(v -> changeItemToLike());
        tvTitle.setOnClickListener(v -> changeItemToReply());
        tvTitle2.setOnClickListener(v -> changeItemToLike());
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
    }

    private void changeItemToReply() {
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
        tvToolbarTitle2.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
        tvTitle2.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        presenter.changeItemTo(LostAndFoundNoticeAdapter.ItemType.REPLY);
        items.clear();
        items.addAll(replies);
        adapter.notifyDataSetChanged();
        if (replies.isEmpty()) {
            refreshLayout.autoRefresh(20);
        }
    }

    private void changeItemToLike() {
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        tvToolbarTitle2.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        tvTitle2.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
        presenter.changeItemTo(LostAndFoundNoticeAdapter.ItemType.LIKE);
        items.clear();
        items.addAll(likes);
        adapter.notifyDataSetChanged();
        if (likes.isEmpty()) {
            refreshLayout.autoRefresh(20);
        }
    }

    private void publishReply(Long lostFoundId, Long replyToId, Long replyToUserId, String replyToUserName) {
//        if (!presenter.isCommentEnable()) {
//            return;
//        }
        if (replyDialog == null) {
            replyDialog = new LostAndFoundReplyDialog(this);
        }
        replyDialog.setReplyUser(replyToUserName);
        replyDialog.setPublishClickListener((dialog, reply) -> {
            if (TextUtils.isEmpty(reply)) {
                onError("内容为空");
                return;
            }
            presenter.publishReply(lostFoundId, replyToId, replyToUserId, reply);
        });
        replyDialog.show();
    }

    private void onRefresh() {
        refreshFlag = true;
        presenter.resetPage();
        presenter.getList();
    }

    private void onLoadMore() {
        presenter.getList();
    }

    @Override
    protected void setUp() {
    }

    @Override
    public void setLoadMoreComplete() {
        refreshLayout.finishRefresh(300);
    }

    @Override
    public void setRefreshComplete() {
        refreshLayout.finishLoadMore(300);
    }
    @Override
    public void showEmptyView() {
        rlEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        rlEmpty.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        rlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorView() {
        rlError.setVisibility(View.GONE);
    }

    @Override
    public void addMoreReply(List<LostAndFoundNoticeAdapter.NoticeWrapper> wrappers) {
        if (refreshFlag) {
            refreshFlag = false;
            replies.clear();
        }
        items.clear();
        replies.addAll(wrappers);
        items.addAll(replies);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addMoreLike(List<LostAndFoundNoticeAdapter.NoticeWrapper> wrappers) {
        if (refreshFlag) {
            refreshFlag = false;
            likes.clear();
        }
        items.clear();
        likes.addAll(wrappers);
        items.addAll(likes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void closePublishDialog() {
        if (replyDialog != null
                && replyDialog.isShowing()) {
            replyDialog.clearInput();
            replyDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
