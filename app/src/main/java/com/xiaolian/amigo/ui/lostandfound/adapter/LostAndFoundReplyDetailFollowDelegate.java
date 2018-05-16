package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.ObjectsCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.ui.widget.BorderedSpan;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.DimentionUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author zcd
 * @date 18/5/14
 */
public class LostAndFoundReplyDetailFollowDelegate
        implements ItemViewDelegate<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> {
    private Context context;
    private int lostFoundType;

    public LostAndFoundReplyDetailFollowDelegate(Context context, int lostFoundType) {
        this.context = context;
        this.lostFoundType = lostFoundType;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lost_and_found_reply_detail_follow;
    }

    @Override
    public boolean isForViewType(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper item, int position) {
        return ObjectsCompat.equals(item.getType(),
                LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW);
    }

    @Override
    public void convert(ViewHolder holder, LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper lostAndFoundReplyDetailWrapper, int position) {
        TextView textView = holder.getView(R.id.tv_comment_content);
        Glide.with(context).load(Constant.IMAGE_PREFIX + lostAndFoundReplyDetailWrapper.getImage())
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into((ImageView) holder.getView(R.id.iv_avatar));
        setReply(textView, lostAndFoundReplyDetailWrapper);
    }

    private void setReply(TextView textView, LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper replyWrapper) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString authorSpan = new SpannableString(replyWrapper.getAuthor());
        authorSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        authorSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#499bff")), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(authorSpan);

        if (replyWrapper.isOwner()) {
            builder.append(" ");
            SpannableString ownerSpan = new SpannableString(
                    ObjectsCompat.equals(lostFoundType, LostAndFound.LOST) ? "失主" : "拾主");
            ownerSpan.setSpan(new AbsoluteSizeSpan(
                            DimentionUtils.convertSpToPixels(9, context)), 0, ownerSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ownerSpan.setSpan(new BorderedSpan(context, 20, 12), 0, ownerSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(ownerSpan);
            builder.append(" ");
        }
        if (replyWrapper.getReplyToUserId() != null && !TextUtils.isEmpty(replyWrapper.getReplyToUserNickName())) {
            SpannableString replyConstantSpan = new SpannableString("回复");
            replyConstantSpan.setSpan(new AbsoluteSizeSpan(
                            DimentionUtils.convertSpToPixels(12, context)), 0, replyConstantSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            replyConstantSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")),
                    0, replyConstantSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(replyConstantSpan);

            SpannableString commentUserSpan = new SpannableString(replyWrapper.getReplyToUserNickName());
            commentUserSpan.setSpan(new AbsoluteSizeSpan(
                            DimentionUtils.convertSpToPixels(12, context)), 0, commentUserSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentUserSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#499bff")),
                    0, commentUserSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(commentUserSpan);
        }

        SpannableString colonSpan = new SpannableString(": ");
        colonSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, colonSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        colonSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, colonSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(colonSpan);

        SpannableString replyContent = new SpannableString(replyWrapper.getContent());
        replyContent.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, replyContent.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        replyContent.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, replyContent.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(replyContent);

        builder.append(" ");

        SpannableString timeSpan = new SpannableString("4小时前");
        timeSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(10, context)), 0, timeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        timeSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, timeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(timeSpan);

        textView.setText(builder);
    }
}
