package com.xiaolian.amigo.ui.lostandfound;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailCommentDelegate;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailContentDelegate;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView2;
import com.xiaolian.amigo.ui.widget.CustomLinearLayoutManager;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.ActionSheetDialog;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundBottomDialog;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundCommentDialog;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundReplyDialog;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 失物招领详情
 *
 * @author zcd
 * @date 17/9/21
 */

public class LostAndFoundDetailActivity2 extends LostAndFoundBaseActivity implements ILostAndFoundDetailView2 {
    public static final String KEY_TYPE = "LostAndFoundDetailType";
    public static final String KEY_ID = "LostAndFoundDetailId";
    private static final int REQUEST_CODE_REPLY_DETAIL = 0x0119;
    private LostAndFoundDetailAdapter adapter;
    private List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> items = new Vector<>();
    private LostAndFoundDetailAdapter.LostAndFoundDetailWrapper content;

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

    @BindView(R.id.ll_footer)
    LinearLayout llFooter;

    @BindView(R.id.view_line)
    View viewLine;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_three_dot)
    ImageView ivThreeDot;

    @BindView(R.id.v_more_hold)
    View vMoreHold;

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private LostAndFoundCommentDialog commentDialog;
    private LostAndFoundReplyDialog replyDialog;
    private LostAndFoundBottomDialog bottomDialog;
    private volatile boolean refreshFlag = false;

    @Inject
    ILostAndFoundDetailPresenter2<ILostAndFoundDetailView2> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_detail2);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(this);

        initRecyclerView();

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                tvTitle.setVisibility(View.VISIBLE);
                ivThreeDot.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
                vMoreHold.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                ivThreeDot.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                vMoreHold.setVisibility(View.GONE);
            }
        });

        if (getIntent() != null) {
            if (ObjectsCompat.equals(getIntent().getIntExtra(KEY_TYPE, 1), com.xiaolian.amigo.data.enumeration.annotation.LostAndFound.LOST)) {
                presenter.setType(com.xiaolian.amigo.data.enumeration.annotation.LostAndFound.LOST);
                showLostDetail();
            } else {
                presenter.setType(com.xiaolian.amigo.data.enumeration.annotation.LostAndFound.FOUND);
                showFoundDetail();
            }
//            presenter.getDetail(getIntent().getLongExtra(KEY_ID, -1));
        }
    }

    private void initRecyclerView() {
        adapter = new LostAndFoundDetailAdapter(this, items);
        adapter.addItemViewDelegate(new LostAndFoundDetailCommentDelegate(this,
                this::publishReply, this::moreReply));
        adapter.addItemViewDelegate(new LostAndFoundDetailContentDelegate(this));
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
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                LostAndFoundDetailActivity2.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                LostAndFoundDetailActivity2.this.onRefresh();
            }
        });
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
    }

    private void moreReply(Long commentId, String commentContent,
                           Long commentAuthorId, String commentAuthor,
                           boolean owner, Long ownerId, Long time,
                           String avatar) {
        startActivityForResult(new Intent(this, LostAndFoundReplyDetailActivity.class)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_COMMENT_ID, commentId)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_COMMENT_CONTENT, commentContent)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_COMMENT_AUTHOR_ID, commentAuthorId)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_COMMENT_AUTHOR, commentAuthor)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_OWNER, owner)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_OWNER_ID, ownerId)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_TIME, time)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_AVATAR, avatar)
                .putExtra(LostAndFoundReplyDetailActivity.KEY_LOST_FOUND_ID, presenter.getLostAndFound().getId())
                .putExtra(LostAndFoundReplyDetailActivity.KEY_LOST_FOUND_TYPE,
                        presenter.getLostAndFound().getType())
                .putExtra(LostAndFoundReplyDetailActivity.KEY_COMMENT_ENABLE, presenter.isCommentEnable()),
                REQUEST_CODE_REPLY_DETAIL);
    }

    @Override
    public void onRefresh() {
        if (content == null) {
            presenter.getDetail(getIntent().getLongExtra(KEY_ID, -1));
        } else {
            presenter.refreshDetail();
        }
        presenter.resetPage();
        refreshFlag = true;
    }

    private void onLoadMore() {
        presenter.getComments();
    }

    private void showFoundDetail() {
        tvToolbarTitle.setText("招领详情");
        tvTitle.setText("招领详情");
    }

    private void showLostDetail() {
        tvToolbarTitle.setText("失物详情");
        tvTitle.setText("失物详情");
    }

    @Override
    protected void setUp() {
    }

    @Override
    public void render(LostAndFound lostAndFound) {
        if (content == null) {
            content = new LostAndFoundDetailAdapter.LostAndFoundDetailWrapper(lostAndFound);
            try {
                if (!items.isEmpty()) {
                    if (items.get(0).getItemType() == LostAndFoundDetailAdapter.LostAndFoundDetailItemType.CONTENT) {
                        items.remove(0);
                        items.add(0, content);
                    } else {
                        items.add(0, content);
                    }
                } else {
                    items.add(content);
                }
            } catch (Exception e) {
                // do nothing
            }
        } else {
            content.setCommentCount(lostAndFound.getCommentsCount());
            content.setViewCount(lostAndFound.getViewCount());
        }
        presenter.getComments();
    }

    @OnClick({R.id.iv_three_dot, R.id.v_more_hold,
            R.id.iv_three_dot2, R.id.v_more_hold_1})
    public void onMoreClick() {
        if (bottomDialog == null) {
            bottomDialog = new LostAndFoundBottomDialog(this);
        }
        bottomDialog.setOkText(presenter.isOwner() ? "删除" : "举报");
        bottomDialog.setOnOkClickListener(dialog -> presenter.reportOrDelete());
        bottomDialog.show();
    }

    @OnClick(R.id.ll_footer)
    public void publishComment() {
        if (!presenter.isCommentEnable()) {
            return;
        }
        if (commentDialog == null) {
            commentDialog = new LostAndFoundCommentDialog(this);
        }
        commentDialog.setPublishClickListener((dialog, comment) -> {
            if (TextUtils.isEmpty(comment)) {
                onError("内容为空");
                return;
            }
            presenter.publishComment(comment);
        });
        commentDialog.show();
    }

    private void publishReply(Long replyToId, Long replyToUserId, String replyToUserName) {
        if (!presenter.isCommentEnable()) {
            return;
        }
        if (replyDialog == null) {
            replyDialog = new LostAndFoundReplyDialog(this);
        }
        replyDialog.setReplyUser(replyToUserName);
        replyDialog.setPublishClickListener((dialog, reply) -> {
            if (TextUtils.isEmpty(reply)) {
                onError("内容为空");
                return;
            }
            presenter.publishReply(replyToId, replyToUserId, reply);
        });
        replyDialog.show();
    }


    @Override
    public void setRefreshComplete() {
        refreshLayout.finishRefresh(300);
    }

    @Override
    public void setLoadMoreComplete() {
        refreshLayout.finishLoadMore(500);
    }

    @Override
    public void showErrorView() {
//        rlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
//        rlEmpty.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorView() {
//        rlError.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
//        rlEmpty.setVisibility(View.VISIBLE);
        if (refreshFlag) {
            refreshFlag = false;
            items.clear();
            items.add(content);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addMore(List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> wrappers) {
        if (refreshFlag) {
            refreshFlag = false;
            items.clear();
            items.add(content);
        }
        items.addAll(wrappers);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void closePublishDialog() {
        if (commentDialog != null
                && commentDialog.isShowing()) {
            commentDialog.clearInput();
            commentDialog.dismiss();
        }
        if (replyDialog != null
                && replyDialog.isShowing()) {
            replyDialog.clearInput();
            replyDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_REPLY_DETAIL) {
//                refreshLostAndFound();
                onRefresh();
            }
        }
    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showFootView() {
        llFooter.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFootView() {
        llFooter.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (presenter.needRefresh()) {
            finishView();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
