package com.xiaolian.amigo.ui.device.washer;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

import static com.xiaolian.amigo.util.Constant.DRYER;
import static com.xiaolian.amigo.util.Constant.WASH_DRYER;

/**
 * 选择洗衣机模式适配器
 *
 * @author zcd
 * @date 18/1/12
 */

public class ChooseWashModeAdapter extends CommonAdapter<ChooseWashModeAdapter.WashModeItem> {
    private int lastChoosePosition = -1;

    ChooseWashModeAdapter(Context context, int layoutId, List<WashModeItem> datas) {
        super(context, layoutId, datas);
    }

    int getLastChoosePosition() {
        return lastChoosePosition;
    }

    void setLastChoosePosition(int lastChoosePosition) {
        this.lastChoosePosition = lastChoosePosition;
    }

    @Override
    protected void convert(ViewHolder holder, WashModeItem washModeItem, int position) {
        holder.setText(R.id.tv_name, washModeItem.getName());
        holder.setText(R.id.tv_price, String.format("售价：%s元", washModeItem.getPrice()));
        int type = washModeItem.getType();
        if (type == WASH_DRYER){
            holder.setImageResource(R.id.image ,R.drawable.ic_choose_wash_mode);
        }else{
            holder.setImageResource(R.id.image ,R.drawable.dryer1);
        }
        if (washModeItem.isChoose()) {
            holder.setBackgroundRes(R.id.ll_choose_wash_mode, R.drawable.content_border_selected);
        } else {
            holder.setBackgroundRes(R.id.ll_choose_wash_mode, R.drawable.content_border);
        }



    }

    @Data
    public static final class WashModeItem {
        private Integer mode;
        private String name;
        private String price;
        private boolean choose;
        private int type ;


        WashModeItem(String name, String price, Integer mode ,int type) {
            this.name = name;
            this.price = price;
            this.mode = mode;
            this.choose = false;
            this.type = type ;
        }
    }
}
