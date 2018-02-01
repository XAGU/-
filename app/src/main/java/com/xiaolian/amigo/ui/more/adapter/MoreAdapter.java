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
 * 更多页面列表adapter
 *
 * @author zcd
 * @date 17/10/13
 */

public class MoreAdapter extends CommonAdapter<MoreAdapter.MoreModel> {
    private static final String DIVIDER_TAG = "divider";
    public MoreAdapter(Context context, int layoutId, List<MoreModel> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MoreModel moreModel, int position) {
        if (position == getDatas().size() - 1) {
            LinearLayout llItem = holder.getView(R.id.ll_item_more);
            if (!TextUtils.equals((CharSequence) llItem.getChildAt(llItem.getChildCount() - 1).getTag(), DIVIDER_TAG)) {
                View view = new View(llItem.getContext());
                view.setTag(DIVIDER_TAG);
                view.setBackgroundResource(R.drawable.divider);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                params.height = ScreenUtils.dpToPxInt(llItem.getContext(), 1);
                llItem.addView(view, -1);
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
