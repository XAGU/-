package com.xiaolian.amigo.ui.more.adapter;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 10/13/17.
 */

public class MoreAdapter extends CommonAdapter<MoreAdapter.MoreModel> {
    public MoreAdapter(Context context, int layoutId, List<MoreModel> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MoreModel moreModel, int position) {
        holder.setText(R.id.tv_content, moreModel.getContent());
    }

    @Data
    public static class MoreModel {
        String content;
        Class clz;

        public MoreModel(String content, Class clz) {
            this.content = content;
            this.clz = clz;
        }
    }
}
