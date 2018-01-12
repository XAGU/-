package com.xiaolian.amigo.ui.washer;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 选择洗衣机模式适配器
 * <p>
 * Created by zcd on 18/1/12.
 */

public class ChooseWashModeAdapter extends CommonAdapter<ChooseWashModeAdapter.WashModeItem> {
    private int lastChoosePosition = -1;

    ChooseWashModeAdapter(Context context, int layoutId, List<WashModeItem> datas) {
        super(context, layoutId, datas);
    }

    public int getLastChoosePosition() {
        return lastChoosePosition;
    }

    public void setLastChoosePosition(int lastChoosePosition) {
        this.lastChoosePosition = lastChoosePosition;
    }

    @Override
    protected void convert(ViewHolder holder, WashModeItem washModeItem, int position) {
        holder.setText(R.id.tv_name, washModeItem.getName());
        holder.setText(R.id.tv_price, String.format("售价：%s元", washModeItem.getPrice()));
        if (washModeItem.isChoose()) {
            holder.setBackgroundRes(R.id.ll_choose_wash_mode, R.drawable.content_border_selected);
        } else {
            holder.setBackgroundRes(R.id.ll_choose_wash_mode, R.drawable.content_border);
        }

    }

    @Data
    static final class WashModeItem {
        private String name;
        private String price;
        private boolean choose;

        WashModeItem(String name, String price) {
            this.name = name;
            this.price = price;
            this.choose = false;
        }
    }
}
