package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.support.v4.util.ObjectsCompat;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author zcd
 * @date 18/6/21
 */
public class LostAndFoundDetailTitleDelegate implements ItemViewDelegate<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lost_and_found_detail_title;
    }

    @Override
    public boolean isForViewType(LostAndFoundDetailAdapter.LostAndFoundDetailWrapper item, int position) {
        if (item == null) {
            return false;
        }
        return ObjectsCompat.equals(item.getItemType(),
                LostAndFoundDetailAdapter.LostAndFoundDetailItemType.TITLE);
    }

    @Override
    public void convert(ViewHolder holder, LostAndFoundDetailAdapter.LostAndFoundDetailWrapper lostAndFoundDetailWrapper, int position) {
        holder.setText(R.id.tv_title, lostAndFoundDetailWrapper.getContentTitle());
    }
}
