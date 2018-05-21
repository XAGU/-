package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.util.ObjectsCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundReplyDTO;
import com.xiaolian.amigo.ui.widget.BorderedSpan;
import com.xiaolian.amigo.util.DimentionUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/15
 */
public class LostAndFoundDetailCommentReplyAdapter
        extends CommonAdapter<LostAndFoundDetailCommentReplyAdapter.ReplyWrapper> {
    private Context context;
    private int type;
    private Long commentUserId;
    private Long ownerId;

    public LostAndFoundDetailCommentReplyAdapter(Context context, int layoutId, List<ReplyWrapper> datas,
                                                 int type, Long commentUserId, Long ownerId) {
        super(context, layoutId, datas);
        this.context = context;
        this.type = type;
        this.commentUserId = commentUserId;
        this.ownerId = ownerId;
    }

    @Override
    protected void convert(ViewHolder holder, ReplyWrapper replyWrapper, int position) {
        TextView textView = holder.getView(R.id.tv_reply);
        setReply(textView, replyWrapper);
    }

    private void setReply(TextView textView, ReplyWrapper replyWrapper) {
        if (replyWrapper.isFooter()) {
            SpannableString footerSpan = new SpannableString(replyWrapper.getContent());
            footerSpan.setSpan(new AbsoluteSizeSpan(
                            DimentionUtils.convertSpToPixels(12, context)), 0, footerSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            footerSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#499bff")), 0, footerSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(footerSpan);
            return;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString authorSpan = new SpannableString(replyWrapper.getUserNickName());
        authorSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        authorSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#499bff")), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(authorSpan);

        if (ObjectsCompat.equals(ownerId, replyWrapper.getUserId())) {
            builder.append(" ");
            SpannableString ownerSpan = new SpannableString(
                    ObjectsCompat.equals(type, LostAndFound.LOST) ? "失主" : "拾主");
            ImageSpan imageSpan = new ImageSpan(context,
                    ObjectsCompat.equals(type, LostAndFound.LOST) ?
                    R.drawable.ic_lost_owner : R.drawable.ic_found_owner) {
                @Override
                public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                    Drawable b = getDrawable();
                    canvas.save();
                    int extra;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        extra = textView.getLineCount() > 1 ? (int) textView.getLineSpacingExtra() : 0;
                    } else {
                        extra = (int) textView.getLineSpacingExtra();
                    }
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
        if (replyWrapper.getReplyToUserId() != null &&
                !TextUtils.isEmpty(replyWrapper.getReplyToUserNickName())) {
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
                        ObjectsCompat.equals(type, LostAndFound.LOST) ? "失主" : "拾主");
                ImageSpan imageSpan = new ImageSpan(context,
                        ObjectsCompat.equals(type, LostAndFound.LOST) ?
                        R.drawable.ic_lost_owner : R.drawable.ic_found_owner) {
                    @Override
                    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                        Drawable b = getDrawable();
                        canvas.save();
                        int extra;
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                            extra = textView.getLineCount() > 1 ? (int) textView.getLineSpacingExtra() : 0;
                        } else {
                            extra = (int) textView.getLineSpacingExtra();
                        }
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

        textView.setText(builder);
    }

    @Data
    public static final class ReplyWrapper {
        private String content;
        private Long replyToUserId;
        private String replyToUserNickName;
        private Long userId;
        private String userNickName;
        private boolean footer;

        public ReplyWrapper(LostFoundReplyDTO lostFoundReplyDTO) {
            this.content = lostFoundReplyDTO.getContent();
            this.replyToUserId = lostFoundReplyDTO.getReplyToUserId();
            this.replyToUserNickName = lostFoundReplyDTO.getReplyToUserNickname();
            this.userId = lostFoundReplyDTO.getUserId();
            this.userNickName = lostFoundReplyDTO.getUserNickname();
            this.footer = false;
        }

        public ReplyWrapper(String content, boolean footer) {
            this.content = content;
            this.footer = footer;
        }
    }
}
