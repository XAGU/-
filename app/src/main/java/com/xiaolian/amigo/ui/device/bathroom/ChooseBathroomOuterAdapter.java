package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/3
 */
public class ChooseBathroomOuterAdapter extends CommonAdapter<ChooseBathroomOuterAdapter.Item> {
    private ChooseBathroomAdapter adapter;
    private Context context;
    public ChooseBathroomOuterAdapter(Context context, int layoutId, List<Item> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, Item item, int position) {
        adapter = new ChooseBathroomAdapter(context, R.layout.item_choose_bathroom, item.getA());
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Data
    public static final class Item {
        List<ChooseBathroomAdapter.ItemWrapper> a;

        public Item(List<ChooseBathroomAdapter.ItemWrapper> a) {
            this.a = a;
        }
    }
}
