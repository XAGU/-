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
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.DimentionUtils;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author zcd
 * @date 18/6/13
 */
public class LostAndFoundNoticeReplyDelegate implements ItemViewDelegate<LostAndFoundNoticeAdapter.NoticeWrapper> {
    public interface OnReplyClickListener {
        void onReply(Long lostFoundId, Long replyToId, Long replyToUserId, String replyToUserName);
    }
    private Context context;
    private OnReplyClickListener replyClickListener;

    private Paint spanPaint;

    public LostAndFoundNoticeReplyDelegate(Context context, OnReplyClickListener replyClickListener) {
        this.context = context;
        this.replyClickListener = replyClickListener;
        spanPaint = new Paint();
        spanPaint.setTextSize(DimentionUtils.convertSpToPixels(12, context));
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lost_and_found_notice_reply;
    }

    @Override
    public boolean isForViewType(LostAndFoundNoticeAdapter.NoticeWrapper item, int position) {
        return ObjectsCompat.equals(item.getItemType(), LostAndFoundNoticeAdapter.ItemType.REPLY);
    }

    @Override
    public void convert(ViewHolder holder, LostAndFoundNoticeAdapter.NoticeWrapper noticeWrapper, int position) {
        Glide.with(context).load(Constant.IMAGE_PREFIX + noticeWrapper.getImageUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into((ImageView) holder.getView(R.id.iv_avatar));
        setReplyUserAndTime(holder.getView(R.id.tv_title), noticeWrapper.getUserName(),
                TimeUtils.lostAndFoundTimestampFormat(noticeWrapper.getCreateTime()) , noticeWrapper.getVest() );
        holder.setText(R.id.tv_content, noticeWrapper.getContent());
        holder.getView(R.id.iv_reply).setOnClickListener(v -> {
            if (replyClickListener != null) {
                replyClickListener.onReply(noticeWrapper.getLostFoundId(),
                        noticeWrapper.getItemId(), noticeWrapper.getUserId(), noticeWrapper.getUserName());
            }
        });
    }

    private void setReplyUserAndTime(TextView textView, String userName, String time ,Integer vest ) {

        float spanLength = 0;
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString userNameSpan = new SpannableString(userName);
        userNameSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, userNameSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        userNameSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#499bff")), 0, userNameSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(userNameSpan);
        spanLength += spanPaint.measureText(userName.toString());


        if (vest != null && vest.equals( Constant.VEST_ADMIN)){
            spanLength = setImageTextView(textView, spanLength, builder , "管理员" ,R.drawable.blog_admin);
        }

        SpannableString replayConstantSpan = new SpannableString(" 回复了你\n");
        replayConstantSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, replayConstantSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        replayConstantSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 0, replayConstantSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(replayConstantSpan);

        SpannableString timeSpan = new SpannableString(time);
        timeSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(10, context)), 0, timeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        timeSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, timeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(timeSpan);

        textView.setText(builder);

    }


    private float setImageTextView(TextView textView, float spanLength, SpannableStringBuilder builder , String text , int resource) {
        builder.append(" ");
        SpannableString ownerSpan = new SpannableString(text);
        ImageSpan imageSpan = new ImageSpan(context, resource){
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
        spanLength += spanPaint.measureText(" " + ownerSpan.toString() + " ");
        return spanLength;
    }
}
