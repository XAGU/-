package com.xiaolian.amigo.ui.lostandfound;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailFollowDelegate;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailMainDelegate;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundReplyDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundReplyDetailView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.ActionSheetDialog;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundReplyDialog;
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
 * 失物招领详情
 *
 * @author zcd
 * @date 17/9/21
 */

public class LostAndFoundReplyDetailActivity extends LostAndFoundBaseActivity implements ILostAndFoundReplyDetailView {
    private static final String TAG = LostAndFoundReplyDetailActivity.class.getSimpleName();
    public static final String KEY_COMMENT_ID = "LostAndFoundReplyDetailActivityCommentID";
    public static final String KEY_COMMENT_CONTENT = "LostAndFoundReplyDetailActivityCommentContent";
    public static final String KEY_COMMENT_AUTHOR_ID = "LostAndFoundReplyDetailActivityCommentAuthorId";
    public static final String KEY_COMMENT_AUTHOR = "LostAndFoundReplyDetailActivityCommentAuthor";
    public static final String KEY_OWNER = "LostAndFoundReplyDetailActivityOwner";
    public static final String KEY_OWNER_ID = "LostAndFoundReplyDetailActivityOwnerId";
    public static final String KEY_TIME = "LostAndFoundReplyDetailActivityTime";
    public static final String KEY_AVATAR = "LostAndFoundReplyDetailActivityAvatar";
    public static final String KEY_LOST_FOUND_ID = "LostAndFoundReplyDetailActivityLostFoundId";
    public static final String KEY_LOST_FOUND_TYPE = "LostAndFoundReplyDetailActivityType";

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

    @BindView(R.id.v_more_hold)
    View vMoreHold;

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private ActionSheetDialog actionSheetDialog;
    private LostAndFoundReplyDialog replyDialog;

    @Inject
    ILostAndFoundReplyDetailPresenter<ILostAndFoundReplyDetailView> presenter;

    private LostAndFoundReplyDetailAdapter adapter;
    private List<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> followRelays
            = new ArrayList<>();
    private LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper mainReply;

    private Long commentAuthorId;
    private String commentAuthor;
    private Long commentId;
    private Integer lostFoundType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_reply_detail);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(this);

        if (getIntent() != null) {
            commentId = getIntent().getLongExtra(KEY_COMMENT_ID, -1);
            commentAuthor = getIntent().getStringExtra(KEY_COMMENT_AUTHOR);
            commentAuthorId = getIntent().getLongExtra(KEY_COMMENT_AUTHOR_ID, -1);
            lostFoundType = getIntent().getIntExtra(KEY_LOST_FOUND_TYPE, com.xiaolian.amigo.data.enumeration.annotation.LostAndFound.LOST);
            presenter.setLostFoundId(getIntent().getLongExtra(KEY_LOST_FOUND_ID, -1));
            presenter.setCommentId(commentId);
            presenter.setOwnerId(getIntent().getLongExtra(KEY_OWNER_ID, -1));
            mainReply = new LostAndFoundReplyDetailAdapter
                    .LostAndFoundReplyDetailWrapper(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.MAIN,
                    getIntent().getBooleanExtra(KEY_OWNER, false),
                    getIntent().getStringExtra(KEY_COMMENT_CONTENT),
                    commentAuthor,
                    getIntent().getLongExtra(KEY_TIME, 0),
                    getIntent().getStringExtra(KEY_AVATAR));
        }
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
        presenter.getReplies();
    }

    private void initRecyclerView() {
        followRelays.add(0, mainReply);
        adapter = new LostAndFoundReplyDetailAdapter(this, followRelays);
        adapter.addItemViewDelegate(new LostAndFoundReplyDetailMainDelegate(this, lostFoundType));
        adapter.addItemViewDelegate(new LostAndFoundReplyDetailFollowDelegate(this, lostFoundType));
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 21)));
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                try {
                    if (followRelays.get(position).getType() == LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW) {
                        if (ObjectsCompat.equals(followRelays.get(position).getAuthorId(), presenter.getUserId())) {
                            if (actionSheetDialog == null) {
                                String chooseOne = "删除";
                                actionSheetDialog = new ActionSheetDialog(LostAndFoundReplyDetailActivity.this)
                                        .builder()
                                        .setTitle("选择")
                                        .addSheetItem(chooseOne, ActionSheetDialog.SheetItemColor.Orange,
                                                which -> presenter.deleteReply(followRelays.get(position).getId()));
                            }
                            actionSheetDialog.setOnCancalListener(dialog -> {
                            });
                            actionSheetDialog.show();
                        } else {
                            publishReply(commentId, followRelays.get(position).getAuthorId(),
                                    followRelays.get(position).getAuthor());
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                LostAndFoundReplyDetailActivity.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                LostAndFoundReplyDetailActivity.this.onRefresh();
            }
        });
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
//        refreshLayout.autoRefresh(20);
    }

    @OnClick({R.id.iv_three_dot, R.id.v_more_hold,
            R.id.iv_three_dot2, R.id.v_more_hold_1})
    public void onMoreClick() {
        if (actionSheetDialog == null) {
            String chooseOne = presenter.isOwner() ? "删除" : "举报";
            actionSheetDialog = new ActionSheetDialog(this)
                    .builder()
                    .setTitle("选择")
                    .addSheetItem(chooseOne, ActionSheetDialog.SheetItemColor.Orange, which -> presenter.reportOrDelete());
        }
        actionSheetDialog.setOnCancalListener(dialog -> {
        });
        actionSheetDialog.show();
    }

    @OnClick(R.id.ll_footer)
    public void publishReply() {
        publishReply(commentId, commentAuthorId, commentAuthor);
    }

    private void publishReply(Long replyToId, Long replyToUserId, String replyToUserName) {
        if (replyDialog == null) {
            replyDialog = new LostAndFoundReplyDialog(this);
        }
        replyDialog.setReplyUser(replyToUserName);
        replyDialog.setPublishClickListener((dialog, reply) -> {
            presenter.publishReply(replyToId, replyToUserId, reply);
        });
        replyDialog.show();
    }

    @Override
    public void onRefresh() {
        presenter.resetPage();
        followRelays.clear();
        followRelays.add(mainReply);
        presenter.getReplies();
    }

    private void onLoadMore() {
        presenter.getReplies();
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
    }

    @Override
    public void addMore(List<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> wrappers) {
        followRelays.addAll(wrappers);
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
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
