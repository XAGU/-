package com.xiaolian.amigo.ui.user.adaptor;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import static com.xiaolian.amigo.ui.user.adaptor.SchoolAdapter.SCHOOL_NAME;

public class SchooNameAdapter implements ItemViewDelegate<SchoolAdapter.SchoolWrapper> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_school_name;
    }

    @Override
    public boolean isForViewType(SchoolAdapter.SchoolWrapper item, int position) {
        return item.getItemType()==SCHOOL_NAME;
    }

    @Override
    public void convert(ViewHolder holder, SchoolAdapter.SchoolWrapper schoolWrapper, int position) {
        holder.setText(R.id.school_name ,schoolWrapper.getContent());
    }
}
