package com.xiaolian.amigo.ui.main;

import android.app.Activity;
import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 10/10/17.
 */

public class ProfileAdaptor extends CommonAdapter<ProfileAdaptor.Item> {

    public ProfileAdaptor(Context context, int layoutId, List<Item> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Item item, int position) {
        holder.setImageResource(R.id.imageLeft, item.getLeftImageId());
        holder.setText(R.id.textMid, item.getText());
    }

    @Data
    public static class Item {
        public Item(int leftImageId, String text, Class<? extends Activity> activityClazz) {
            this.leftImageId = leftImageId;
            this.text = text;
            this.activityClazz = activityClazz;
        }

        int leftImageId;
        String text;
        Class<? extends Activity> activityClazz;
    }
}
