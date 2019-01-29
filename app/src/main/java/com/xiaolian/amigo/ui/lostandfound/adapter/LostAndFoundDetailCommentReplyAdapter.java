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
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.DimentionUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;

import lombok.Data;

/**
 * 评论回复adapter
 * @author zcd
 * @date 18/5/15
 *
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
            footerSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#3969ad")), 0, footerSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(footerSpan);
            return;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString authorSpan = new SpannableString(replyWrapper.getUserNickName());
        authorSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        authorSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#3969ad")), 0, authorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(authorSpan);

        if (ObjectsCompat.equals(ownerId, replyWrapper.getUserId())) {
            setImageText(textView, builder, "联主", R.drawable.blog );
        }else{
            if (replyWrapper.getVest() != null && replyWrapper.getVest().equals( Constant.VEST_ADMIN)){
                setImageText(textView, builder, "管理员", R.drawable.blog_admin);
            }
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
            commentUserSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#3969ad")),
                    0, commentUserSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(commentUserSpan);

            if (ObjectsCompat.equals(ownerId, replyWrapper.getReplyToUserId())) {

                setImageText(textView ,builder ,"联主",R.drawable.blog);
            }else{
                if (replyWrapper.getVest() != null && replyWrapper.getVest().equals(Constant.VEST_ADMIN)){
                    setImageText(textView, builder, "管理员", R.drawable.blog_admin);
                }
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

    /**
     * 绘制有图片的文字
     * @param textView
     * @param builder
     * @param vestName
     * @param blog
     */
    private void setImageText(TextView textView, SpannableStringBuilder builder, String vestName, int blog) {
        builder.append(" ");
        SpannableString ownerSpan = new SpannableString(vestName);
        ImageSpan imageSpan = new ImageSpan(context, blog) {
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

    @Data
    public static final class ReplyWrapper {
        private String content;
        private Long replyToUserId;
        private String replyToUserNickName;
        private Long userId;
        private String userNickName;
        private boolean footer;

        /**
         * 马甲 1 普通学生 2 管理员已学生身份回复 3 管理员
         */
        private Integer vest ;

        public ReplyWrapper(LostFoundReplyDTO lostFoundReplyDTO) {
            this.content = lostFoundReplyDTO.getContent();
            this.replyToUserId = lostFoundReplyDTO.getReplyToUserId();
            this.replyToUserNickName = lostFoundReplyDTO.getReplyToUserNickname();
            this.userId = lostFoundReplyDTO.getUserId();
            this.userNickName = lostFoundReplyDTO.getUserNickname();
            this.footer = false;
            this.vest = lostFoundReplyDTO.getVest();
        }

        public ReplyWrapper(String content, boolean footer) {
            this.content = content;
            this.footer = footer;
        }
    }
}
