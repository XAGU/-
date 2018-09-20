package com.xiaolian.amigo.ui.lostandfound;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
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
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailTitleDelegate;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView2;
import com.xiaolian.amigo.ui.widget.CustomLinearLayoutManager;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.BookingCancelDialog;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundBottomDialog;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundCommentDialog;
import com.xiaolian.amigo.ui.widget.dialog.LostAndFoundReplyDialog;
import com.xiaolian.amigo.ui.widget.dialog.PrepayDialog;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.KeyboardStatusDetector;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.util.SoftInputUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity2.KEY_LIKE;

/**
 * 联子详情
 *
 * @author zcd
 * @date 17/9/21
 */

public class LostAndFoundDetailActivity2 extends LostAndFoundBaseActivity implements ILostAndFoundDetailView2  ,KeyboardStatusDetector.KeyboardVisibilityListener {
    public static final String KEY_TYPE = "LostAndFoundDetailType";
    public static final String KEY_ID = "LostAndFoundDetailId";

    public static final String KEY_DELETE = "LostAndFoundDelete";
    private static final  String TAG = LostAndFoundDetailActivity2.class.getSimpleName();
    private static final int REQUEST_CODE_REPLY_DETAIL = 0x0119;

    private boolean isDelete ;
    @BindView(R.id.et_reply)
    EditText etReply;
    @BindView(R.id.reply)
    TextView reply;
    @BindView(R.id.v_more_hold_1)
    ImageView vMoreHold1;
    private LostAndFoundDetailAdapter adapter;
    private List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> items = new Vector<>();
    private LostAndFoundDetailAdapter.LostAndFoundDetailWrapper content;
    private LostAndFoundDetailAdapter.LostAndFoundDetailWrapper hotTitle =
            new LostAndFoundDetailAdapter.LostAndFoundDetailWrapper(LostAndFoundDetailAdapter.LostAndFoundDetailItemType.TITLE,
                    "热门回复");
    private LostAndFoundDetailAdapter.LostAndFoundDetailWrapper normalTitle =
            new LostAndFoundDetailAdapter.LostAndFoundDetailWrapper(LostAndFoundDetailAdapter.LostAndFoundDetailItemType.TITLE,
                    "全部回复");


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
    RelativeLayout llFooter;

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


    private boolean isReplyName = false;

    private long replyToId;
    private long replyToUserId;

    /**
     * 记录是否like
     */
    private int likeed ;
    @Inject
    ILostAndFoundDetailPresenter2<ILostAndFoundDetailView2> presenter;

    private BookingCancelDialog deleteDialog ;
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
        keyboardListener();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy != 0) SoftInputUtils.hideSoftInputFromWindow(LostAndFoundDetailActivity2.this ,etReply);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDelete = false ;
    }

    public void init(){
        if (presenter == null  || vMoreHold == null
                || vMoreHold1 == null) return ;
        if (presenter.isCommentEnable()) {
            vMoreHold.setVisibility(View.VISIBLE);
            vMoreHold1.setVisibility(View.VISIBLE);
            if (content.isCollected()) {
                vMoreHold1.setBackgroundResource(R.drawable.college);
                vMoreHold.setBackgroundResource(R.drawable.college);
            } else {
                vMoreHold1.setBackgroundResource(R.drawable.uncollege);
                vMoreHold.setBackgroundResource(R.drawable.uncollege);
            }
        } else {
            vMoreHold.setVisibility(View.GONE);
            vMoreHold1.setVisibility(View.GONE);
        }
    }


    private void showDialog(){
        if (deleteDialog == null){
            deleteDialog = new BookingCancelDialog(this);
            deleteDialog.setTvTitle("确认删除此条联子吗？");
            deleteDialog.setTvTip("确认后词条联子的内容都将被删除");
            deleteDialog.setOnCancelClickListener(new PrepayDialog.OnCancelClickListener() {
                @Override
                public void onCancelClick(Dialog dialog) {
                    isDelete = true ;
                    presenter.reportOrDelete();
                }
            });
            deleteDialog.setOnOkClickListener(new PrepayDialog.OnOkClickListener() {
                @Override
                public void onOkClick(Dialog dialog) {
                    dialog.cancel();
                }
            });
        }

        deleteDialog.show();

    }

    private void initRecyclerView() {
        adapter = new LostAndFoundDetailAdapter(this, items);
        adapter.addItemViewDelegate(new LostAndFoundDetailCommentDelegate(this,
                this::publishReply, this::moreReply, new LostAndFoundDetailContentDelegate.OnLikeClickListener() {
            @Override
            public void onLikeClick(int position, long id, boolean like) {
                if (like) {

                    presenter.unLikeComment(position, id);
                } else {

                    presenter.likeComment(position, id);
                }
            }
        }));
        adapter.addItemViewDelegate(new LostAndFoundDetailContentDelegate(this, new LostAndFoundDetailContentDelegate.OnLikeClickListener() {
            @Override
            public void onLikeClick(int position, long id, boolean like) {
                if (like) {
                    likeed =2 ;
                    presenter.unLikeContent(position, id);
                } else {
                    likeed = 1 ;
                    presenter.likeContent(position, id);
                }
            }
        }));
        adapter.addItemViewDelegate(new LostAndFoundDetailTitleDelegate());
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        recyclerView.setItemAnimator(null);
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
        init();
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

    @OnTextChanged({R.id.et_reply})
    void etTextChange() {
        if (TextUtils.isEmpty(etReply.getText().toString())) {
            reply.setEnabled(false);
        } else {
            reply.setEnabled(true);
        }
    }


    @OnClick({R.id.et_reply})
    public void etReply() {
        isReplyName = false;
    }

    @OnClick({R.id.reply})
    public void reply() {
        Log.d(TAG ,"clickReply");
        String commentContent = etReply.getText().toString().trim();
        if (isReplyName) {
            presenter.publishReply(replyToId, replyToUserId, commentContent);
            etReply.setText("");
            SoftInputUtils.hideSoftInputFromWindow(this ,etReply);

            isReplyName = false ;
        } else {
            presenter.publishComment(commentContent);
            etReply.setText("");
            SoftInputUtils.hideSoftInputFromWindow(this ,etReply);
            isReplyName = false ;
        }

    }

    @Override
    protected void setUp() {
    }

    @Override
    public void render(LostAndFound lostAndFound) {
        if (presenter == null ) return ;
        if (presenter.isCommentEnable()){
            showComment();
        }else{
            hideComment();
        }
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
            content.setLikeCount(lostAndFound.getLikeCount());
        }
        if (presenter.isCommentEnable()) {
            presenter.getComments();
        }
    }


    @OnClick({R.id.iv_three_dot,
            R.id.iv_three_dot2})
    public void onMoreClick() {
        if (content == null) {
            return;
        }
        if (bottomDialog == null) {
            bottomDialog = new LostAndFoundBottomDialog(this);
        }
        bottomDialog.setOkText(presenter.isOwner() ? "删除" : "举报");
        bottomDialog.setOkTextColor(R.color.colorDark6);
        bottomDialog.setOnOkClickListener(dialog ->{
                    if (presenter.isOwner()){
                        showDialog();
                    }else {
                        presenter.reportOrDelete();
                    }
                }
                );
        bottomDialog.show();
    }



    @OnClick({R.id.v_more_hold, R.id.v_more_hold_1})
    public void collect() {
        if (presenter.isCommentEnable()) {
            vMoreHold.setVisibility(View.VISIBLE);
            vMoreHold1.setVisibility(View.VISIBLE);
            if (content.isCollected()) {
                presenter.unCollect();
            } else {
                presenter.collect();
            }
        } else {
            vMoreHold.setVisibility(View.GONE);
            vMoreHold1.setVisibility(View.GONE);
        }
    }



    private void publishReply(Long replyToId, Long replyToUserId, String replyToUserName) {
        if (!presenter.isCommentEnable()) {
            return;
        }

        etReply.setText("");
        etReply.setHint("回复：" + replyToUserName);
        SoftInputUtils.showSoftInputFromWindow(this, etReply);
        isReplyName = true;
        this.replyToId = replyToId;
        this.replyToUserId = replyToUserId;

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
//        rlEmpty.setVisibility(View.VISIBLE);
        if (refreshFlag) {
            refreshFlag = false;
            items.clear();
            items.add(content);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addMore(List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> wrappers,
                        List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> hots) {
        if (refreshFlag) {
            refreshFlag = false;
            items.clear();
            items.add(content);
            if (!hots.isEmpty()) {
                items.add(hotTitle);
                items.addAll(hots);
            }
            if (!wrappers.isEmpty()) {
                items.add(normalTitle);
            }
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

    @OnClick({R.id.iv_back})
    @Override
    public void finishView() {
        Intent intent = new Intent();
        intent.putExtra(LostAndFoundActivity2.KEY_COMMENT_COUNT, content == null ?
                0 : content.getCommentCount());
        intent.putExtra(KEY_LIKE ,likeed);
        intent.putExtra(KEY_DELETE ,isDelete);
        setResult(RESULT_OK ,intent);
        finish();
    }

    @Override
    public void showFootView(boolean isCollege) {
        vMoreHold1.setVisibility(View.VISIBLE);
        vMoreHold.setVisibility(View.VISIBLE);
        if (isCollege){
            vMoreHold.setBackgroundResource(R.drawable.college);
            vMoreHold1.setBackgroundResource(R.drawable.college);
        }else{

            vMoreHold.setBackgroundResource(R.drawable.uncollege);
            vMoreHold1.setBackgroundResource(R.drawable.uncollege);
        }

    }

    @Override
    public void hideFootView() {
        vMoreHold1.setVisibility(View.GONE);
        vMoreHold.setVisibility(View.GONE);
    }

    @Override
    public void collectSuccess() {
        onSuccess("收藏成功");
        if (content != null) {
            content.setCollected(true);
        }
        vMoreHold1.setBackground(null);
        vMoreHold.setBackground(null);
        vMoreHold1.setBackgroundResource(R.drawable.college);
        vMoreHold.setBackgroundResource(R.drawable.college);
    }

    @Override
    public void unCollectSuccess() {
        if (content != null) {
            content.setCollected(false);
        }

        vMoreHold1.setBackgroundResource(R.drawable.uncollege);
        vMoreHold.setBackgroundResource(R.drawable.uncollege);
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    public void keyboardListener(){
        int keyHeight = ScreenUtils.getScreenHeight(this) / 3 ;
        llFooter.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    reply.setVisibility(View.VISIBLE);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    reply.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void notifyAdapter(int position, boolean delay) {
        adapter.notifyItemChanged(position);
//        if (delay) {
//            recyclerView.postDelayed(() -> adapter.notifyItemChanged(position), 300);
//        } else {
//        }
    }

    @Override
    public void showComment() {
        if (llFooter != null) llFooter.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideComment() {
        if (llFooter != null) llFooter.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        recyclerView.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(LostAndFoundActivity2.KEY_COMMENT_COUNT, content == null ?
                0 : content.getCommentCount());
        intent.putExtra(KEY_LIKE ,likeed);
        intent.putExtra(KEY_DELETE ,isDelete);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();

        if (deleteDialog != null){
            if (deleteDialog.isShowing()) deleteDialog.cancel();

            deleteDialog = null ;
        }
    }

    @Override
    public void onVisibilityChanged(boolean keyboardVisible) {

    }
}
