package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.util.ObjectsCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.ui.widget.BorderedSpan;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.DimentionUtils;
import com.xiaolian.amigo.util.TimeUtils;
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
    private Long ownerId;

    public LostAndFoundReplyDetailFollowDelegate(Context context, int lostFoundType,
                                                 Long ownerId) {
        this.context = context;
        this.lostFoundType = lostFoundType;
        this.ownerId = ownerId;
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

        if (ObjectsCompat.equals(ownerId, replyWrapper.getAuthorId())) {
            builder.append(" ");
            SpannableString ownerSpan = new SpannableString(
                    ObjectsCompat.equals(lostFoundType, LostAndFound.LOST) ? "失主" : "拾主");
            ImageSpan imageSpan = new ImageSpan(context,
                    ObjectsCompat.equals(lostFoundType, LostAndFound.LOST) ?
                            R.drawable.ic_lost_owner : R.drawable.ic_found_owner) {
                @Override
                public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                    Drawable b = getDrawable();
                    canvas.save();
                    int extra = textView.getLineCount() > 1 ? (int) textView.getLineSpacingExtra() : 0;
                    int transY = bottom - b.getBounds().bottom - extra;
                    transY -= paint.getFontMetricsInt().descent / 2;
                    canvas.translate(x, transY);
                    b.draw(canvas);
                    canvas.restore();
                }
            };
            ownerSpan.setSpan(imageSpan, 0, ownerSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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

            if (ObjectsCompat.equals(ownerId, replyWrapper.getReplyToUserId())) {
                builder.append(" ");
                SpannableString ownerSpan = new SpannableString(
                        ObjectsCompat.equals(lostFoundType, LostAndFound.LOST) ? "失主" : "拾主");
                ImageSpan imageSpan = new ImageSpan(context,
                        ObjectsCompat.equals(lostFoundType, LostAndFound.LOST) ?
                        R.drawable.ic_lost_owner : R.drawable.ic_found_owner) {
                    @Override
                    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                        Drawable b = getDrawable();
                        canvas.save();
                        int extra = textView.getLineCount() > 1 ? (int) textView.getLineSpacingExtra() : 0;
                        int transY = bottom - b.getBounds().bottom - extra;
                        transY -= paint.getFontMetricsInt().descent / 2;
                        canvas.translate(x, transY);
                        b.draw(canvas);
                        canvas.restore();
                    }
                };
                ownerSpan.setSpan(imageSpan, 0, ownerSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(ownerSpan);
                builder.append(" ");
            }
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

        SpannableString timeSpan = new SpannableString(TimeUtils.lostAndFoundTimestampFormat(replyWrapper.getTime()));
        timeSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(10, context)), 0, timeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        timeSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, timeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(timeSpan);

        textView.setText(builder);
    }
}
