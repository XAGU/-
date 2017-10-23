package com.xiaolian.amigo.ui.more.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;
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
        if (position == getDatas().size() - 1) {
            LinearLayout ll_item = holder.getView(R.id.ll_item_more);
            if (!TextUtils.equals((CharSequence) ll_item.getChildAt(ll_item.getChildCount() - 1).getTag(), "divider")) {
                View view = new View(ll_item.getContext());
                view.setTag("divider");
                view.setBackgroundResource(R.drawable.divider);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                params.height = ScreenUtils.dpToPxInt(ll_item.getContext(), 1);
                ll_item.addView(view, -1);
            }
        }
        holder.setText(R.id.tv_content, moreModel.getContent());
    }

    @Data
    public static class MoreModel {
        String content;
        Class clz;
        String extra;

        public MoreModel(String content, Class clz) {
            this.content = content;
            this.clz = clz;
        }

        public MoreModel(String content, Class clz, String extra) {
            this.content = content;
            this.clz = clz;
            this.extra = extra;
        }
    }
}
