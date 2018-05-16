package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.ObjectsCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
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
            ownerSpan.setSpan(new AbsoluteSizeSpan(
                            DimentionUtils.convertSpToPixels(9, context)), 0, ownerSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ownerSpan.setSpan(new BorderedSpan(context, 20, 7), 0, ownerSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(ownerSpan);
            builder.append(" ");
        }
        if (ObjectsCompat.equals(replyWrapper.getReplyToUserId(), commentUserId)) {
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
