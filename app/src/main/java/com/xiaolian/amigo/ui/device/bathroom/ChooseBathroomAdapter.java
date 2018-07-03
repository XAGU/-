package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/27
 */
public class ChooseBathroomAdapter extends CommonAdapter<ChooseBathroomAdapter.ItemWrapper> {

    public ChooseBathroomAdapter(Context context, int layoutId, List<ItemWrapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ItemWrapper itemWrapper, int position) {
        holder.setText(R.id.tv_content, itemWrapper.getName());

    }

    @Data
    public static final class ItemWrapper {
        private String name;

        public ItemWrapper(String name) {
            this.name = name;
        }
    }
}
