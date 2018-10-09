package com.xiaolian.amigo.ui.device.bathroom.adapter;

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
 * @author zcd
 * @date 18/7/12
 */
public class ChooseShowerAddressAdapter extends CommonAdapter<ChooseShowerAddressAdapter.ChooseShowerAddressWrapper> {
    private static final String DIVIDER_TAG = "divider";

    public ChooseShowerAddressAdapter(Context context, int layoutId, List<ChooseShowerAddressWrapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ChooseShowerAddressWrapper chooseShowerAddressWrapper, int position) {
        if (position == 0) {
            LinearLayout llItem = holder.getView(R.id.ll_item_more);
            if (!TextUtils.equals((CharSequence) llItem.getChildAt(llItem.getChildCount() - 1).getTag(), DIVIDER_TAG)) {
                View view = new View(llItem.getContext());
                view.setTag(DIVIDER_TAG);
                view.setBackgroundResource(R.drawable.divider);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                params.height = ScreenUtils.dpToPxInt(llItem.getContext(), 1);
                llItem.addView(view, 0);
            }
        }
        holder.setText(R.id.tv_content, chooseShowerAddressWrapper.getContent());
    }

    @Data
    public static final class ChooseShowerAddressWrapper {
        private Long id;
        private String content;

        public ChooseShowerAddressWrapper(String content) {
            this.content = content;
        }
    }
}
