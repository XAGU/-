package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

public class TipAdapter extends CommonAdapter<TipAdapter.Tip> {
    public TipAdapter(Context context, int layoutId, List<Tip> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Tip tip, int position) {
        holder.setText(R.id.tv_tip, tip.getContent());
    }

    @Data
    public static class Tip {
        private String content;

        public Tip(String content) {
            this.content = content;
        }
    }

}
