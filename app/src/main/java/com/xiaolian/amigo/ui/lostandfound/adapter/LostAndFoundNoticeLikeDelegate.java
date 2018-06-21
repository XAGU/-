package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.ObjectsCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ViewTreeObserver;
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
public class LostAndFoundNoticeLikeDelegate implements ItemViewDelegate<LostAndFoundNoticeAdapter.NoticeWrapper> {
    private Context context;

    public LostAndFoundNoticeLikeDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lost_and_found_notice_like;
    }

    @Override
    public boolean isForViewType(LostAndFoundNoticeAdapter.NoticeWrapper item, int position) {
        return ObjectsCompat.equals(item.getItemType(), LostAndFoundNoticeAdapter.ItemType.LIKE);
    }

    @Override
    public void convert(ViewHolder holder, LostAndFoundNoticeAdapter.NoticeWrapper noticeWrapper, int position) {
        holder.setText(R.id.tv_content, noticeWrapper.getContent());
        Glide.with(context).load(Constant.IMAGE_PREFIX + noticeWrapper.getImageUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into((ImageView) holder.getView(R.id.iv_avatar));
//        setUserNameAndContent(holder.getView(R.id.tv_content), noticeWrapper.getUserName(), noticeWrapper.getContent());
        holder.setText(R.id.tv_user_name, noticeWrapper.getUserName());
        holder.setText(R.id.tv_content, "点赞了“" + noticeWrapper.getContent() + "”");
        holder.setText(R.id.tv_time, TimeUtils.lostAndFoundTimestampFormat(0L));
        TextView tvContent = holder.getView(R.id.tv_content);
        tvContent.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ViewTreeObserver obs = tvContent.getViewTreeObserver();
                        obs.removeOnGlobalLayoutListener(this);
                        if (tvContent.getLineCount() > 1) {
                            int lineEndIndex = tvContent.getLayout().getLineEnd(0);
                            String text = tvContent.getText().subSequence(0, lineEndIndex - 4) + "...”";
                            tvContent.setText(text);
                        }
                    }
                });
    }

    private void setUserNameAndContent(TextView textView, String userName, String content) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString userNameSpan = new SpannableString(userName);
        userNameSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, userNameSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        userNameSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#499bff")), 0, userNameSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(userNameSpan);

        SpannableString contentSpan = new SpannableString(" 点赞了“" + content);
        contentSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, contentSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        contentSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 0, contentSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(contentSpan);

        textView.setText(builder);
    }
}
