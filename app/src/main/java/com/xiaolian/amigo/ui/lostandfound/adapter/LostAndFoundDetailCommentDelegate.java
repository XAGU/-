package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.graphics.Color;
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
import android.view.View;
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
    private SpaceItemDecoration itemDecoration;

    public LostAndFoundDetailCommentDelegate(Context context,
                                             OnReplyCommentListener replyCommentListener,
                                             OnMoreReplyClickListener moreReplyClickListener) {
        this.context = context;
        this.replyCommentListener = replyCommentListener;
        this.moreReplyClickListener = moreReplyClickListener;
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
//        holder.setText(R.id.tv_comment_author, lostAndFoundDetailWrapper.getCommentAuthor());
        setCommentAuthor(holder.getView(R.id.tv_comment_author), lostAndFoundDetailWrapper.isOwner(),
                lostAndFoundDetailWrapper.getType(),
                lostAndFoundDetailWrapper.getCommentAuthor());
        holder.getView(R.id.iv_owner)
                .setVisibility(lostAndFoundDetailWrapper.isOwner() ? View.VISIBLE : View.GONE);
        holder.setImageResource(R.id.iv_owner,
                ObjectsCompat.equals(lostAndFoundDetailWrapper.getType(), LostAndFound.LOST) ?
                        R.drawable.ic_lost_owner : R.drawable.ic_found_owner);
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

        holder.setText(R.id.tv_like_count, String.valueOf(lostAndFoundDetailWrapper.getLikeCount()));

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
            if (itemDecoration == null) {
                itemDecoration = new SpaceItemDecoration(ScreenUtils.dpToPxInt(context, 5));
                recyclerView.addItemDecoration(itemDecoration);
            }
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

    private void onMoreReply(LostAndFoundDetailAdapter.LostAndFoundDetailWrapper lostAndFoundDetailWrapper) {
        if (moreReplyClickListener != null) {
            moreReplyClickListener.onMoreReplyClick(lostAndFoundDetailWrapper.getId(),
                    lostAndFoundDetailWrapper.getCommentContent(),
                    lostAndFoundDetailWrapper.getCommentAuthorId(),
                    lostAndFoundDetailWrapper.getCommentAuthor(),
                    lostAndFoundDetailWrapper.isOwner(),
                    lostAndFoundDetailWrapper.getOwnerId(),
                    lostAndFoundDetailWrapper.getTime(),
                    lostAndFoundDetailWrapper.getAvatar());
        }
    }

    private void setCommentAuthor(TextView textView, boolean isOwner, int type, String author) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString authorSpan = new SpannableString(author);
        authorSpan.setSpan(new AbsoluteSizeSpan(
                DimentionUtils.convertSpToPixels(12, context)), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        authorSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(authorSpan);
        if (isOwner) {
//            builder.append(" ");
//            SpannableString ownerSpan = new SpannableString(
//                    ObjectsCompat.equals(type, LostAndFound.LOST) ? "失主" : "拾主");
//            Drawable bg = context.getResources().getDrawable(R.drawable.bg_lost_and_found_stroke_gray);
////            ownerSpan.setSpan(new ImageSpan(bg) {
////                @Override
////                public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
////                    paint.setTextSize(50);
////                    int len = Math.round(paint.measureText(text, start, end));
////                    getDrawable().setBounds(0, 0, len, 60);
////                    super.draw(canvas, text, start, end, x, top, y, bottom, paint);
////                    paint.setColor(Color.parseColor("#999999"));
////                    paint.setTextSize(40);
////                    canvas.drawText(text.subSequence(start, end).toString(), x + 10, y, paint);
////                }
////            }, 0, ownerSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ownerSpan.setSpan(new AbsoluteSizeSpan(
//                            DimentionUtils.convertSpToPixels(9, context)), 0, ownerSpan.length(),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ownerSpan.setSpan(new BorderedSpan(context, 10, 5), 0, ownerSpan.length(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            builder.append(ownerSpan);
        }
        textView.setText(builder);
    }

    public interface OnReplyCommentListener {
        void onReplyComment(Long replyToId, Long replyToUserId, String replyToUserName);
    }

    public interface OnMoreReplyClickListener {
        void onMoreReplyClick(Long commentId, String commentContent,
                              Long commentAuthorId, String commentAuthor,
                              boolean owner, Long ownerId, Long time,
                              String avatar);
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
