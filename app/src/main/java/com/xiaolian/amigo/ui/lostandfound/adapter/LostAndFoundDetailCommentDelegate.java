package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundReplyDTO;
import com.xiaolian.amigo.ui.widget.BorderedSpan;
import com.xiaolian.amigo.ui.widget.CustomLinearLayoutManager;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.DimentionUtils;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.util.TimeUtils;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 失物招领详情评论
 *
 * @author zcd
 * @date 18/5/14
 */

public class LostAndFoundDetailCommentDelegate
        implements ItemViewDelegate<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> {
    private static final String TAG = LostAndFoundDetailCommentDelegate.class.getSimpleName();
    private Context context;
    private OnReplyCommentListener replyCommentListener;
    private OnMoreReplyClickListener moreReplyClickListener;
    private LostAndFoundDetailContentDelegate.OnLikeClickListener likeClickListener;
    private SpaceItemDecoration itemDecoration;
    private boolean animating = false;
    private Animation animation;

    public LostAndFoundDetailCommentDelegate(Context context,
                                             OnReplyCommentListener replyCommentListener,
                                             OnMoreReplyClickListener moreReplyClickListener,
                                             LostAndFoundDetailContentDelegate.OnLikeClickListener likeClickListener) {
        this.context = context;
        this.replyCommentListener = replyCommentListener;
        this.moreReplyClickListener = moreReplyClickListener;
        this.likeClickListener =likeClickListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lost_and_found_detail_comment;
    }

    @Override
    public boolean isForViewType(LostAndFoundDetailAdapter.LostAndFoundDetailWrapper item, int position) {
        if (item == null) {
            return false;
        }
        return ObjectsCompat.equals(item.getItemType(),
                LostAndFoundDetailAdapter.LostAndFoundDetailItemType.COMMENT);
    }

    @Override
    public void convert(ViewHolder holder,
                        LostAndFoundDetailAdapter.LostAndFoundDetailWrapper lostAndFoundDetailWrapper,
                        int position) {
//        holder.setText(R.id.tv_comment_author, lostAndFoundDetailWrapper.getCommentAuthor());  lostAndFoundDetailWrapper.getType(),
        setCommentAuthor(holder.getView(R.id.tv_comment_author), lostAndFoundDetailWrapper.isOwner(),
                lostAndFoundDetailWrapper.getCommentAuthor() ,holder.getView(R.id.iv_owner) ,lostAndFoundDetailWrapper.getVest()
                ,holder.getView(R.id.iv_admin));
        holder.setText(R.id.tv_comment_content, lostAndFoundDetailWrapper.getCommentContent());
        holder.setText(R.id.tv_time,
                TimeUtils.lostAndFoundTimestampFormat(lostAndFoundDetailWrapper.getTime()));

        holder.getView(R.id.iv_reply).setVisibility(lostAndFoundDetailWrapper.isCommentEnable() ?
                View.VISIBLE : View.GONE);
        holder.getView(R.id.iv_reply).setOnClickListener(v -> {
            if (replyCommentListener != null) {
                replyCommentListener.onReplyComment(lostAndFoundDetailWrapper.getId(),
                        lostAndFoundDetailWrapper.getCommentAuthorId(),
                        lostAndFoundDetailWrapper.getCommentAuthor());
            }
        });


        holder.getView(R.id.iv_like).setVisibility(lostAndFoundDetailWrapper.isCommentEnable() ?
                View.VISIBLE : View.GONE);
        holder.getView(R.id.tv_like_count).setVisibility(lostAndFoundDetailWrapper.isCommentEnable() ?
                View.VISIBLE : View.GONE);
        holder.setText(R.id.tv_like_count, String.valueOf(lostAndFoundDetailWrapper.getLikeCount()));
        holder.setImageResource(R.id.iv_like,
                lostAndFoundDetailWrapper.isLiked() ?
                        R.drawable.icon_praise_sel : R.drawable.ic_unlike);

        holder.getView(R.id.iv_like).setOnClickListener(v -> {
            like(holder, lostAndFoundDetailWrapper, position);
        });
        holder.getView(R.id.tv_like_count).setOnClickListener(v -> {
            like(holder, lostAndFoundDetailWrapper, position);
        });

        holder.getView(R.id.rl_author_info).setOnClickListener(v -> onMoreReply(lostAndFoundDetailWrapper));
        holder.getView(R.id.tv_comment_content).setOnClickListener(v -> onMoreReply(lostAndFoundDetailWrapper));
        holder.getView(R.id.recyclerView).setOnClickListener(v -> onMoreReply(lostAndFoundDetailWrapper));

        Glide.with(context).load(Constant.IMAGE_PREFIX + lostAndFoundDetailWrapper.getAvatar())
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into((ImageView) holder.getView(R.id.iv_avatar));
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        if (lostAndFoundDetailWrapper.getReplies() == null
                || lostAndFoundDetailWrapper.getReplies().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            List<LostAndFoundDetailCommentReplyAdapter.ReplyWrapper> replies = new ArrayList<>();
            for (LostFoundReplyDTO reply : lostAndFoundDetailWrapper.getReplies()) {
                replies.add(new LostAndFoundDetailCommentReplyAdapter.ReplyWrapper(reply));
            }

            if (lostAndFoundDetailWrapper.getReplyContent() > 2) {
                replies.add(
                        new LostAndFoundDetailCommentReplyAdapter.ReplyWrapper(
                                String.format(Locale.getDefault(), "共%d条回复 >", lostAndFoundDetailWrapper.getReplyContent()),
                                true));
            }
            LostAndFoundDetailCommentReplyAdapter adapter =
                    new LostAndFoundDetailCommentReplyAdapter(context,
                            R.layout.item_lost_and_found_detail_comment_reply,
                            replies, lostAndFoundDetailWrapper.getType(),
                            lostAndFoundDetailWrapper.getCommentAuthorId(),
                            lostAndFoundDetailWrapper.getOwnerId());
//            if (itemDecoration == null) {
//                itemDecoration = new SpaceItemDecoration(ScreenUtils.dpToPxInt(context, 5));
//                recyclerView.addItemDecoration(itemDecoration);
//            }
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    try {
                        onMoreReply(lostAndFoundDetailWrapper);
                    } catch (Exception e) {
                        // do nothing
                    }
//                    if (replies.get(position).isFooter()) {
//                        if (moreReplyClickListener != null) {
//                            moreReplyClickListener.onMoreReplyClick(lostAndFoundDetailWrapper.getId(),
//                                    lostAndFoundDetailWrapper.getCommentContent(),
//                                    lostAndFoundDetailWrapper.getCommentAuthorId(),
//                                    lostAndFoundDetailWrapper.getCommentAuthor(),
//                                    lostAndFoundDetailWrapper.isOwner(),
//                                    lostAndFoundDetailWrapper.getOwnerId(),
//                                    lostAndFoundDetailWrapper.getTime(),
//                                    lostAndFoundDetailWrapper.getAvatar(),
//                                    lostAndFoundDetailWrapper.getId());
//                        }
//                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new CustomLinearLayoutManager(context));
        }
    }

    private void like(ViewHolder holder, LostAndFoundDetailAdapter.LostAndFoundDetailWrapper lostAndFoundDetailWrapper, int position) {
        if (animating) {
            return;
        }
        if (likeClickListener != null) {
            Integer likeCount = lostAndFoundDetailWrapper.getLikeCount();
            if (lostAndFoundDetailWrapper.isLiked()) {
                lostAndFoundDetailWrapper.setLiked(false);
                lostAndFoundDetailWrapper.setLikeCount(likeCount - 1);
                holder.setImageResource(R.id.iv_like, R.drawable.ic_unlike);
//                    holder.setText(R.id.tv_like_count,
//                            String.valueOf(lostAndFoundDetailWrapper.getLikeCount()));
                likeClickListener.onLikeClick(position, lostAndFoundDetailWrapper.getId(), true);
            } else {
                lostAndFoundDetailWrapper.setLiked(true);
                lostAndFoundDetailWrapper.setLikeCount(likeCount + 1);
                holder.setImageResource(R.id.iv_like, R.drawable.icon_praise_sel);
//                    holder.setText(R.id.tv_like_count,
//                            String.valueOf(lostAndFoundDetailWrapper.getLikeCount()));
                likeClickListener.onLikeClick(position, lostAndFoundDetailWrapper.getId(), false);
                if (animation == null) {
                    animation = AnimationUtils.loadAnimation(context, R.anim.lost_found_like);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            animating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animating = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                holder.getView(R.id.iv_like).startAnimation(animation);
            }
        }
    }

    private void onMoreReply(LostAndFoundDetailAdapter.LostAndFoundDetailWrapper lostAndFoundDetailWrapper) {
        if (moreReplyClickListener != null) {
            moreReplyClickListener.onMoreReplyClick(lostAndFoundDetailWrapper.getId(),
                    lostAndFoundDetailWrapper.getCommentContent(),
                    lostAndFoundDetailWrapper.getCommentAuthorId(),
                    lostAndFoundDetailWrapper.getCommentAuthor(),
                    lostAndFoundDetailWrapper.isOwner(),
                    lostAndFoundDetailWrapper.getOwnerId(),
                    lostAndFoundDetailWrapper.getTime(),
                    lostAndFoundDetailWrapper.getAvatar()
                    ,lostAndFoundDetailWrapper.getVest());
        }
    }

    /**
     * 评论用户的标签图片
     * @param textView
     * @param isOwner
     * @param author
     * @param ower
     * @param vest
     * @param ivVest
     */
    private void setCommentAuthor(TextView textView, boolean isOwner, String author , TextView ower , Integer vest
                        ,ImageView ivVest) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString authorSpan = new SpannableString(author);
        authorSpan.setSpan(new AbsoluteSizeSpan(
                DimentionUtils.convertSpToPixels(12, context)), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        authorSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(authorSpan);
        textView.setText(builder);
        if (isOwner) {
            ower.setVisibility(View.VISIBLE);
            ower.setText("联主");
        }else{
            ower.setVisibility(View.GONE);
            if (vest != null && vest.equals(Constant.VEST_ADMIN)){
                ivVest.setVisibility(View.VISIBLE);
            }
        }

    }

    public interface OnReplyCommentListener {
        void onReplyComment(Long replyToId, Long replyToUserId, String replyToUserName);
    }

    public interface OnMoreReplyClickListener {
        void onMoreReplyClick(Long commentId, String commentContent,
                              Long commentAuthorId, String commentAuthor,
                              boolean owner, Long ownerId, Long time,
                              String avatar , Integer vest);
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (path != null && path instanceof String) {
                Glide.with(context).load((String) path)
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(imageView);
            }
        }

    }
}
