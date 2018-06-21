package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.ObjectsCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
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
    private Context context;

    public LostAndFoundNoticeReplyDelegate(Context context) {
        this.context = context;
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
                TimeUtils.lostAndFoundTimestampFormat(0L));
        holder.setText(R.id.tv_content, noticeWrapper.getContent());
    }

    private void setReplyUserAndTime(TextView textView, String userName, String time) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString userNameSpan = new SpannableString(userName);
        userNameSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, userNameSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        userNameSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#499bff")), 0, userNameSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(userNameSpan);

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
}
