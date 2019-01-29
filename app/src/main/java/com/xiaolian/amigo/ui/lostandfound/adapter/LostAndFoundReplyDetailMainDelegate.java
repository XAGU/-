package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author zcd
 * @date 18/5/14
 */
public class LostAndFoundReplyDetailMainDelegate
        implements ItemViewDelegate<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> {
    private Context context;
    private int lostFoundType;

    public LostAndFoundReplyDetailMainDelegate(Context context, int lostFoundType) {
        this.context = context;
        this.lostFoundType = lostFoundType;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lost_and_found_reply_detail_main;
    }

    @Override
    public boolean isForViewType(LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper item, int position) {
        return ObjectsCompat.equals(item.getType(),
                LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.MAIN);
    }

    @Override
    public void convert(ViewHolder holder, LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper lostAndFoundReplyDetailWrapper, int position) {
        holder.setText(R.id.tv_comment_author, lostAndFoundReplyDetailWrapper.getAuthor());
        holder.setText(R.id.tv_content, lostAndFoundReplyDetailWrapper.getContent());
        holder.setText(R.id.tv_time, TimeUtils.lostAndFoundTimestampFormat(lostAndFoundReplyDetailWrapper.getTime()));
        if (lostAndFoundReplyDetailWrapper.isOwner()){
            holder.getView(R.id.iv_owner).setVisibility(View.VISIBLE);
            holder.setImageResource(R.id.iv_owner ,R.drawable.blog);
        }else{
           if (lostAndFoundReplyDetailWrapper.getVest() != null &&
                   lostAndFoundReplyDetailWrapper.getVest().equals(Constant.VEST_ADMIN)){
               holder.getView(R.id.iv_owner).setVisibility(View.VISIBLE);
               holder.setImageResource(R.id.iv_owner ,R.drawable.blog_admin);
           }else{
               holder.getView(R.id.iv_owner).setVisibility(View.GONE);
           }
        }
        Glide.with(context).load(Constant.IMAGE_PREFIX + lostAndFoundReplyDetailWrapper.getImage())
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into((ImageView) holder.getView(R.id.iv_avatar));
    }
}
