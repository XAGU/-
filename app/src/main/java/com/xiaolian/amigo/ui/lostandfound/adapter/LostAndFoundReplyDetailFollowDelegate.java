package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.support.v4.util.ObjectsCompat;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author zcd
 * @date 18/5/14
 */
public class LostAndFoundReplyDetailFollowDelegate
        implements ItemViewDelegate<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> {
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
        holder.setText(R.id.tv_comment_content, lostAndFoundReplyDetailWrapper.getCommentContent());
    }
}
