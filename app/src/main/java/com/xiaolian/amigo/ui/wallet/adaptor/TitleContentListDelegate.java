package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标题内容列表
 *
 * @author zcd
 * @date 18/1/19
 */

public class TitleContentListDelegate implements ItemViewDelegate<WithdrawRechargeDetailAdapter.Item> {
    private Context context;

    public TitleContentListDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_title_content_list;
    }

    @Override
    public boolean isForViewType(WithdrawRechargeDetailAdapter.Item item, int position) {
        return item.getType() == WithdrawRechargeDetailAdapter.TITLE_CONTENT_LIST_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, WithdrawRechargeDetailAdapter.Item item, int position) {
        TitleContentListItem titleContentListItem = (TitleContentListItem) item;
        TitleContentListAdapter listAdapter = new TitleContentListAdapter(context, R.layout.item_title_content_item, titleContentListItem.getItems());
        RecyclerView recyclerView = holder.getView(R.id.rv_title_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new SpaceItemDecoration(15 * 3));
        recyclerView.setAdapter(listAdapter);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class TitleContentListItem extends WithdrawRechargeDetailAdapter.Item {
        private List<ListItem> items;

        public TitleContentListItem(List<ListItem> items, int type) {
            super(type);
            this.items = items;
        }
    }

    @Data
    public static class ListItem {
        private String title;
        private String content;
        private String contentColor;

        public ListItem(String title, String content, String contentColor) {
            this.title = title;
            this.content = content;
            this.contentColor = contentColor;
        }
    }

    public static class TitleContentListAdapter extends CommonAdapter<ListItem> {

        TitleContentListAdapter(Context context, int layoutId, List<ListItem> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, ListItem listItem, int position) {
            holder.setText(R.id.tv_title, listItem.getTitle());
            holder.setText(R.id.tv_content, listItem.getContent());
            holder.setTextColor(R.id.tv_content, Color.parseColor(listItem.getContentColor()));
        }
    }
}
