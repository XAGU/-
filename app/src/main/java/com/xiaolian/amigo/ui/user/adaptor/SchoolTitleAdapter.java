package com.xiaolian.amigo.ui.user.adaptor;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public class SchoolTitleAdapter  implements ItemViewDelegate<SchoolAdapter.SchoolWrapper>{

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_school_title;
    }

    @Override
    public boolean isForViewType(SchoolAdapter.SchoolWrapper item, int position) {
        return item.getItemType() == SchoolAdapter.TITLE;
    }

    @Override
    public void convert(ViewHolder holder, SchoolAdapter.SchoolWrapper schoolWrapper, int position) {
        holder.setText(R.id.school_title ,schoolWrapper.getContent());
    }
}
